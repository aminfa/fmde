package groovy.runtime.metaclass.org.upb.fmde.de.categories.concrete.graphs

import org.upb.fmde.de.categories.concrete.graphs.Graph

class GraphMetaClass extends DelegatingMetaClass {

    GraphMetaClass(MetaClass metaClass) { super(metaClass) }

    GraphMetaClass(Class theClass) { super(theClass) }

    Object invokeMethod(Object object, String name, Object[] args) {
        switch(name) {
            case "isCase": return isInside(object, args)
            case "contains": return isInside(object, args)
            case "edgeFor": return edgeFor(object, args)
            case "endings": return nodesForEdge(object, args)
            case "nodesForEdge": return nodesForEdge(object, args)

            default: return super.invokeMethod(object, name, args)
        }
    }

    boolean isInside(Graph graph, Object[] args) {
        if(args.length != 1) {
            throw new IllegalArgumentException()
        }
        Object o = args[0]
        return o in graph.vertices()
    }

    List<Object> edgeFor(Graph graph, Object[] args) {
        if(args.length != 2) {
            throw new IllegalArgumentException()
        }
        Object node1 = args[0]
        Object node2 = args[1]
        List edges = new ArrayList()
        graph.edges().elts().each {edge ->
            def (node1_, node2_) = graph.endings(edge)

            if(node1_ == node1 && node2_ == node2) {
                edges += edge
            }
        }
        return edges
    }

    Tuple2 nodesForEdge(Graph graph, Object[] args) {
        if(args.length != 1) {
            throw new IllegalArgumentException()
        }
        Object edge = args[0]
        def srcNode = graph.src().map(edge)
        def trgNode = graph.trg().map(edge)
        return new Tuple2(srcNode, trgNode)
    }


}
