package org.upb.fmde.de.dsl

import org.upb.fmde.de.categories.concrete.graphs.Graph
import org.upb.fmde.de.categories.concrete.graphs.GraphMorphism
import org.upb.fmde.de.categories.concrete.tgraphs.TGraph

import static ModelLang.arrow

class TGraphCreator extends GraphCreator {

    Graph typeGraph

    Map types = new HashMap()

    TGraph create() {
        Graph graph = super.create()
        GraphMorphism typeArr = arrow {
            src graph
            trg Objects.requireNonNull(this.typeGraph)
            mapping = this.types
        }
        return new TGraph(graph.label(), typeArr)
    }

    Graph getTypeGraph() {
        return typeGraph
    }

    void setTypeGraph(Graph typeGraph) {
        this.typeGraph = typeGraph
    }

    Map getTypes() {
        return types
    }

    void setTypes(Map instanceTypes) {
        this.types.putAll(instanceTypes)
    }

    def nodeType(Object... nodes) {
        [
                equals : { nodeT ->
                    nodes.each{node ->
                        types[node] = nodeT
                    }
                },
                getFrom : { Closure typeGenertor ->
                    nodes.each { node ->
                        def actualType = typeGenertor.call(node)
                        if(actualType != null)
                            types[node] = actualType
                    }
                }
        ]
    }

    def typeEach(Closure typeGenerator) {
        this.vertices.each { node ->
            nodeType node getFrom typeGenerator
        }
    }
}
