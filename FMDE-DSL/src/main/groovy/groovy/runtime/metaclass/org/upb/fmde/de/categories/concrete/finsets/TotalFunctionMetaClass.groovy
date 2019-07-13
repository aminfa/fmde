package groovy.runtime.metaclass.org.upb.fmde.de.categories.concrete.finsets

import org.upb.fmde.de.categories.concrete.finsets.TotalFunction

class TotalFunctionMetaClass extends DelegatingMetaClass {

    TotalFunctionMetaClass(MetaClass metaClass) { super(metaClass) }

    TotalFunctionMetaClass(Class theClass) { super(theClass) }

    Object invokeMethod(Object object, String name, Object[] args) {
        switch(name) {
            case "isCase": return isInside(object, args)
            case "contains": return isInside(object, args)
            case "getAt": return getAt(object, args)
            case "putAt": return putAt(object, args)

            default: return super.invokeMethod(object, name, args)
        }
    }

    boolean isInside(TotalFunction func, Object[] args) {
        if(args.length != 1) {
            throw new IllegalArgumentException()
        }
        Object o = args[0]
        return o in func.mappings().keySet()
    }

    def getAt(TotalFunction func, Object[] args) {
        if(args.length != 1) {
            throw new IllegalArgumentException()
        }
        Object o = args[0]
        return func.mappings()[o]
    }

    def putAt(TotalFunction func, Object[] args) {
        if(args.length != 2) {
            throw new IllegalArgumentException()
        }
        Object o = args[0]
        Object o2 = args[0]
        return func.mappings()[o] = o2
    }


}
