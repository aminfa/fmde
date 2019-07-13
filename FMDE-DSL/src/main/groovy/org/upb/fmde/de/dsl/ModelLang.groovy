package org.upb.fmde.de.dsl

import org.upb.fmde.de.categories.concrete.finsets.FinSet
import org.upb.fmde.de.categories.concrete.finsets.FinSets
import org.upb.fmde.de.categories.concrete.finsets.TotalFunction
import org.upb.fmde.de.categories.concrete.graphs.Graph
import org.upb.fmde.de.categories.concrete.graphs.GraphMorphism
import org.upb.fmde.de.categories.concrete.graphs.Graphs
import org.upb.fmde.de.categories.concrete.tgraphs.TGraph

class ModelLang {

    static <T> T extract(Map conf, T defaultVal, List options) {
        for(def opt : options) {
            if (opt in conf) {
                return (T) conf[opt]
            }
        }
        return defaultVal
    }

    def static extractLabel(Map it) {
        extract(it,
                "_",
                ["label", "name", "named", "called"])
    }

    def static set(Map it, List content = null) {
        String label = extractLabel(it)
        content = extract(
                it, content, ["content", "entries", "with"])
        new FinSet(label, content)
    }

    def static setArrow = { it, mapping = null ->
        String label = extractLabel it

        FinSet from = extract(
                it, null, ["src", "from", "in"])
        FinSet to = extract(
                it, null, ["trg", "to", "out"])
        mapping = extract(
                it, mapping, ["mapping"])

        FinSet itself = extract(
                it, null, ["itself"])

        if (itself != null) {
            to = itself
            from = itself
        }

        if (to == null) {
            throw new IllegalArgumentException("Cannot create an arrow called " + label + " without a destination set.")
        }

        if (from == null) {
            def init = FinSets.FinSets.initialObject()
            from = init.obj
            mapping = init.up.apply(to)
        } else if (mapping != null) {
            if (mapping instanceof Closure) {
                def mappingFunc = mapping
                mapping = [:]
                from.elts().each {
                    def out = mappingFunc(it)
                    if (!(out in to.elts())) {
                        throw new IllegalArgumentException("Given mapping does not map to the target map: " + out)
                    }
                    mapping[it] = out
                }
            }
            if (mapping instanceof Map) {
                mapping = new HashMap(mapping)
                from.elts().each { elem ->
                    if (!elem in mapping) {
                        if (elem in to.elts()) {
                            mapping[elem] = elem
                        } else {
                            throw new IllegalArgumentException("Mapping needs to be a total function, but " + it + " was not mapped and is not contained in the target set.")
                        }

                    }
                }
            } else {
                throw new IllegalArgumentException("Mapping needs to be either a closure or  a mapping");
            }
        } else {
            throw new IllegalArgumentException("Mapping needs to be degined")
        }
        def arr = new TotalFunction(from, label, to)
        arr.setMappings(mapping)
        return arr
    }

    def static graph(conf, edges = null) {
        String label = extractLabel(conf)

        FinSet nodes = extract(conf, null, ["nodes", "vertices"])

        edges = extract(conf, edges, ["edges"])

        def srcMapping = extract(conf, null, ["src"])
        def trgMapping = extract(conf, null, ["trg"])

        if(nodes == null) {
            return Graphs.Graphs.initialObject().obj
        }

        if(edges instanceof FinSet && srcMapping instanceof TotalFunction && trgMapping instanceof  TotalFunction) {
            return new Graph(label, edges, nodes, srcMapping, trgMapping)
        }
        else if(edges instanceof Map){
            def newEdges = set(named: "edges", new ArrayList())

            srcMapping = new HashMap()
            trgMapping = new HashMap()

            def checkTrg = { it ->
                if(! it in nodes.elts()) {
                    throw new IllegalArgumentException("Maps to entries outside of graph: " + key + " -> " + it )
                }
            }

            def addEdge = { srcNode, edge, trgNode ->
                newEdges.elts().add(edge)
                srcMapping[edge] = srcNode
                trgMapping[edge] = trgNode
            }

            edges.each{ key, value ->
                if(value in nodes.elts()) {
                    checkTrg value
                    addEdge key, key + "-" + value , value
                }
                else if(value instanceof List) {
                    value.each { it ->
                        checkTrg it
                        addEdge key, key + "-" + it , it
                    }
                }
                else if(value instanceof Map) {
                    value.each {edgeName, trgNode ->
                        checkTrg trgNode
                        addEdge key, edgeName, trgNode
                    }
                }
                else {
                    throw new IllegalArgumentException("Not sure how to map " + key + " to " + value)
                }

            }
            return graph(called: label, nodes: nodes, edges: newEdges,
                    src: arrow(called: "src", from: newEdges, to: nodes, srcMapping),
                    trg: arrow(called: "trg", from: newEdges, to: nodes, trgMapping))
        }
        else {
            throw new IllegalArgumentException("Cannot create a graph from " + conf)
        }
    }



    def static GraphMorphism gArrow(conf, Graph srcGraph, Graph trgGraph, TotalFunction f_E, TotalFunction f_V) {
        def label = extractLabel(conf)
        return new GraphMorphism(label, srcGraph, trgGraph, f_E, f_V)
    }

    def static arrow(Closure closure) {
        ArrowCreator arrowCreator = new ArrowCreator()
        closure.resolveStrategy = Closure.DELEGATE_FIRST
        closure.delegate = arrowCreator
        closure.call()
        return arrowCreator.create()
    }

    static def Graph graph(Closure closure) {
        GraphCreator graphCreator = new GraphCreator()
        closure.resolveStrategy = Closure.DELEGATE_FIRST
        closure.delegate = graphCreator
        closure.run()
        return graphCreator.create()
    }

    static def TGraph typedGraph(Closure closure) {
        TGraphCreator graphCreator = new TGraphCreator()
        closure.resolveStrategy = Closure.DELEGATE_FIRST
        closure.delegate = graphCreator
        closure.run()
        return graphCreator.create()
    }
}