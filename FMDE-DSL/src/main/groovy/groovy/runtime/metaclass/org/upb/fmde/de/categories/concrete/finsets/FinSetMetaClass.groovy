package groovy.runtime.metaclass.org.upb.fmde.de.categories.concrete.finsets

import org.upb.fmde.de.categories.concrete.finsets.FinSet

class FinSetMetaClass extends DelegatingMetaClass {

    FinSetMetaClass(MetaClass metaClass) { super(metaClass) }

    FinSetMetaClass(Class theClass) { super(theClass) }

    Object invokeMethod(Object object, String name, Object[] args) {
        switch(name) {
            case "isCase": return isInside(object, args)
            case "contains": return isInside(object, args)
            default: return super.invokeMethod(object, name, args)
        }
    }

    boolean isInside(FinSet set, Object[] args) {
        if(args.length != 1) {
            throw new IllegalArgumentException()
        }
        Object o = args[0]
        return o in set.elts()
    }
}
