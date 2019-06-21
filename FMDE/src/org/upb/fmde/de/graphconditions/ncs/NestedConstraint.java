package org.upb.fmde.de.graphconditions.ncs;

import java.util.List;
import java.util.function.BiFunction;

import org.upb.fmde.de.categories.Category;
import org.upb.fmde.de.categories.PatternMatcher;
import org.upb.fmde.de.graphconditions.ComplexGraphCondition;

public interface NestedConstraint<Ob, Arr> extends ComplexGraphCondition<Ob, Arr> {

	public boolean isSatisfiedByObj(Ob o, BiFunction<Ob, Ob, PatternMatcher<Ob, Arr>> creator);

}

class NBasicConstraint<Ob, Arr> implements NestedConstraint<Ob, Arr> {
	
	protected Category<Ob, Arr> cat;
	protected Arr p;

	public NBasicConstraint(Category<Ob, Arr> cat, Arr p) {
		this.cat = cat;
		this.p = p;
	}
	
	@Override
	public boolean isSatisfiedByArrow(Arr m, BiFunction<Ob, Ob, PatternMatcher<Ob, Arr>> creator) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isSatisfiedByObj(Ob G, BiFunction<Ob, Ob, PatternMatcher<Ob, Arr>> creator) {
		Ob P = cat.target(p);
		
	}
	
}
