package org.upb.fmde.de.graphconditions.ncs;

import org.upb.fmde.de.categories.Category;
import org.upb.fmde.de.categories.ComparableArrow;

public interface NestableConstraint<Ob, Arr extends ComparableArrow<Arr>> {

	public boolean isSatisfiedByArrow(Arr p);
	
	public boolean isSatisfiedByObj(Ob o);
	
	public default Category<Ob, Arr> getCat() {
		throw new UnsupportedOperationException("Category not defined..");
	}
	
}