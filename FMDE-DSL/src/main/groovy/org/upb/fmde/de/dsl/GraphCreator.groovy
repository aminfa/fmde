package org.upb.fmde.de.dsl

import org.upb.fmde.de.categories.concrete.graphs.Graph

import static ModelLang.*;

class GraphCreator {
    String label

    List vertices = new ArrayList()

    List edges = new ArrayList()

    Map srcMapping = new HashMap(),
        trgMapping = new HashMap()


    def create() {
        def verticeSet = set(named: "vertices", this.vertices)
        def edgeSet = set(named: "edges", this.edges)
        def srcMapping = arrow {
            called "edgeInput"
            src edgeSet
            trg verticeSet
            mapping = this.srcMapping
        }
        def trgMapping = arrow {
            called "edgeOutput"
            src edgeSet
            trg verticeSet
            mapping = this.trgMapping
        }
        return graph(label: label, vertices: verticeSet, edges: edgeSet, src: srcMapping, trg: trgMapping)
    }

    def setLabel = { String it ->
        this.label = it
    }
    def named = setLabel,
        name = setLabel,
        called = setLabel


    def node(Closure closure) {
        return node(null, closure)
    }

    def node (content, Closure closure=null) {
        NodeCreator nodeCreator = new NodeCreator(content)
        if(closure != null) {
            closure.delegate = nodeCreator
            closure.resolveStrategy = Closure.DELEGATE_ONLY
            closure.run()
        }
        def nodeObj = nodeCreator.create()
        if(!vertices.contains(nodeObj))
            vertices.add(nodeObj)
        return nodeCreator
    }

    def vertice = this.&node

    def edge (Closure closure){
        EdgeCreator creator = new EdgeCreator()
        closure.delegate = closure
        closure.run()
        connect(creator.edge, creator.node1, creator.node2)
    }

    def connectFromAll(List nodes, node2) {
        connectFromAll(null, nodes, node2)
    }

    def connectFromAll(List edges, List nodes, node2) {
        if (edges == null)
            edges = nodes.collect({ node1 ->
                node1.toString() + "-" + node2.toString()
            })

        nodes.eachWithIndex{ def node1, int i ->
            def edge = null
            if(i < edges.size()) {
                edge = edges[i]
            }
            connect(edge, node1, node2)
        }
    }

    def connectToAll(node1, List nodes) {
        connectToAll(null, node1, nodes)
    }
    def connectToAll(List edges, node1, List nodes) {
        if (edges == null)
            edges = nodes.collect({ node2 ->
                node1.toString() + "-" + node2.toString()
            })

        nodes.eachWithIndex{ def node2, int i ->
            def edge = null
            if(i < edges.size()) {
                edge = edges[i]
            }
            connect(edge, node1, node2)
        }
    }

//    def connect(node1) {
//        def nc = node node1
//        return [
//            from: nc.from,
//            to: nc.to,
//            itself: nc.itself,
//            toAll: nc.toAll,
//            fromAll: nc.fromAll
//        ].collectEntries {word, closure ->
//            [word, {nc.create()} << closure ]
//        }
//    }

    def connect(Object... entries) {
        [
                to  : { Object... trgEntries ->
                    for (Object trg : trgEntries) {
                        for (Object src : entries) {
                            connect(null, src, trg)
                        }
                    }
                },
                from: { Object srcEntry ->
                    [
                            to: { Object... trgEntries ->
                                trgEntries.eachWithIndex { Object trgEntry,
                                                           int i ->
                                    def edge = null
                                    if (i < entries.size()) {
                                        edge = entries[i]
                                    }
                                    connect(edge, srcEntry, trgEntry)
                                }
                            }
                    ]
                }
        ]
    }

    def connect(edge, node1, node2) {
        if (edge == null)
            edge = node1.toString() + "-" + node2.toString()

        if(edge in edges) {
            throw new IllegalArgumentException("Edge already defined")
        }

        if(! (node1 in vertices))
            vertices.add(node1)

        if(! (node2 in vertices))
            vertices.add(node2)

        edges.add(edge)
        srcMapping[edge] = node1
        trgMapping[edge] = node2
    }

    def add(Graph otherGraph) {
        otherGraph.edges().elts().each {edge ->
            def srcNode = otherGraph.src().map(edge)
            def trgNode = otherGraph.trg().map(edge)
            connect(edge, srcNode, trgNode)
        }
    }

    def clear() {
        vertices.clear()
        edges.clear()
        srcMapping.clear()
        trgMapping.clear()
    }

    def set(Graph otherGraph) {
        clear()
        add(otherGraph)
    }

    class EdgeCreator {
        def node1, node2, edge

        def from = {
            node1 = it
        }

        def to = {
            node2 = it
        }

        def called = {
            edge = it
        }
        def label = called,
            name = called

    }

    class NodeCreator {

        def content = null

        def nodesConnectedTo = [],
            nodesConnectedFrom = []

        NodeCreator(content) {
            this.content = content
        }

        def from = { node, edge = null ->
            nodesConnectedFrom += [new Tuple(edge, node)]
        }

        def to = { node, edge = null ->
            nodesConnectedTo += [new Tuple(edge, node)]
        }

        def itself = { edge = null ->
            nodesConnectedTo += [new Tuple(edge, this)]
        }

        def toAll = { List nodes ->
            nodes.each {
                to it
            }
        }

        def fromAll = { List nodes ->
            nodes.each {
                from it
            }
        }

        def connection = { Closure action ->
            if (![from, to, itself, toAll, fromAll].contains(action)) {
                throw new IllegalArgumentException("Action not understood " + action)
            }
            return action
        }

        def edge = this.&connect

        def Object create() {
            nodesConnectedTo.each {Tuple it ->
                connect(it[0], content, it[1])
            }
            nodesConnectedFrom.each {Tuple it ->
                connect(it[0], it[1], content)
            }
            return content
        }
    }

}