package org.upb.fmde.de.dsl

import org.upb.fmde.de.categories.concrete.finsets.FinSet
import org.upb.fmde.de.categories.concrete.finsets.TotalFunction
import org.upb.fmde.de.categories.concrete.graphs.Graph
import org.upb.fmde.de.categories.concrete.graphs.GraphMorphism
import org.upb.fmde.de.categories.concrete.tgraphs.TGraph
import org.upb.fmde.de.categories.concrete.tgraphs.TGraphMorphism

import static ModelLang.*;

class ArrowCreator {
    String label = "_"

    FinSet srcSet, trgSet

    Graph srcGraph, trgGraph

    TGraph srcTGraph, trgTGraph

    Map mapping = new HashMap()

    def setLabel = { String it ->
        this.label = it
    }
    def named = setLabel,
        name = setLabel,
        called = setLabel

    def src = { it ->
        if (it instanceof FinSet) {
            srcSet = it
        } else if (it instanceof Graph) {
            srcGraph = it
        } else if (it instanceof TGraph) {
            srcTGraph = it
        } else {
            throw new IllegalArgumentException("Source type not recognized:" + it + " of class " + it.class.name)
        }
    }

    def trg = { it ->
        if (it instanceof FinSet) {
            trgSet = it
        } else if (it instanceof Graph) {
            trgGraph = it
        } else if (it instanceof TGraph) {
            trgTGraph = it
        } else {
            throw new IllegalArgumentException("Source type not recognized:" + it + " of class " + it.class.name)
        }
    }

    def id = { it ->
        itself(it)
    }

    def itself = { it ->
        src(it)
        trg(it)
    }

    def setMapping(Map mapping) {
        this.mapping = new HashMap(mapping)
    }

    def map(Object... entries) {
        [
                to  : { Object... trgEntries ->
                    for (Object trg : trgEntries) {
                        for (Object src : entries) {
                            mapping[src] = trg
                        }
                    }
                },
                from: { Object... srcEntries ->
                    connect srcEntries to entries
                }
        ]
    }


    def create() {
        if(srcSet != null && trgSet != null) {
            return createSetArrow()
        }
        if(srcGraph != null && trgGraph != null) {
            return createGraphArrow()
        }
        if(srcTGraph !=null && trgTGraph != null) {
            return createTGraphArrow()
        }
        throw new IllegalStateException("Cannot create arrow from the current state: " + this)
    }

    def createSetArrow() {
        srcSet.elts().each { elem ->
            if(! (elem in mapping) ) {
                if(elem in trgSet.elts()) {
                    mapping[elem] = elem
                } else {
                    throw new IllegalArgumentException("Mapping needs to be a total function, " +
                            "but " + elem + " was not mapped and is not contained in the target set.")
                }

            }
        }
        new HashSet(mapping.keySet()).each {
            if( !(it in srcSet.elts()) ) {
                mapping.remove(it)
            }
        }
        def arr = new TotalFunction(srcSet, label, trgSet)
        arr.setMappings(mapping)
        return arr
    }

    def createGraphArrow() {
        def vArrow = arrow {
            src this.srcGraph.vertices()
            trg this.trgGraph.vertices()
            mapping = this.mapping
        }

        srcGraph.edges().elts().each { edge ->
            if(!this.mapping.keySet().contains(edge)) {
                def (srcNode1, trgNode1) = srcGraph.endings(edge)
                def srcNode2 = vArrow[srcNode1]
                def trgNode2 = vArrow[trgNode1]
                if(srcNode2 == null || trgNode2 == null) {
                    throw new IllegalArgumentException("Cannot remove nodes...: "
                            + edge + ":  " + srcNode2 + ", " + trgNode2)
                }
                List edges2 = trgGraph.edgeFor(srcNode2, trgNode2)
                if(edges2.size() != 1) {
                    throw new IllegalArgumentException("Not sure which edge to target...: " + edges2)
                }
                def edge2 = edges2[0]
                mapping[edge] = edge2
            }
        }
        def eArrow = arrow {
            src this.srcGraph.edges()
            trg this.trgGraph.edges()
            mapping = this.mapping
        }

        def arr = new GraphMorphism(label, srcGraph, trgGraph, eArrow, vArrow)
        return arr
    }

    def createTGraphArrow() {
        GraphMorphism graphMorphism = arrow {
            src this.srcTGraph.graph()
            trg this.trgTGraph.graph()
            mapping = this.mapping
        }
        def arr = new TGraphMorphism(label, graphMorphism, srcTGraph, trgTGraph)
        return arr
    }

    @Override
    public String toString() {
        return "ArrowCreator{" +
                "label='" + label + '\'' +
                ", srcSet=" + srcSet +
                ", trgSet=" + trgSet +
                ", srcGraph=" + srcGraph +
                ", trgGraph=" + trgGraph +
                ", mapping=" + mapping +
                '}';
    }
}