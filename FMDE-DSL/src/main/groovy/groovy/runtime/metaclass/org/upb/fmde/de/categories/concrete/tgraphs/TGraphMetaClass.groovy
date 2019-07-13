package groovy.runtime.metaclass.org.upb.fmde.de.categories.concrete.tgraphs

import org.upb.fmde.de.categories.concrete.graphs.Graph
import org.upb.fmde.de.categories.concrete.tgraphs.TGraph

class TGraphMetaClass extends DelegatingMetaClass {

    TGraphMetaClass(MetaClass metaClass) { super(metaClass) }

    TGraphMetaClass(Class theClass) { super(theClass) }

    Object invokeMethod(Object object, String name, Object[] args) {
        switch(name) {
            case "getGraph": return getGraph(object, args)
            case "getTypeGraph": return getType(object, args)
            case "graph": return getGraph(object, args)
            case "typeGraph": return getType(object, args)
            default: return super.invokeMethod(object, name, args)
        }
    }

    Graph getGraph(TGraph graph, Object[] args) {
        return graph.type().src()
    }

    Graph getType(TGraph graph, Object[] args) {
        return graph.type().trg()
    }

}
