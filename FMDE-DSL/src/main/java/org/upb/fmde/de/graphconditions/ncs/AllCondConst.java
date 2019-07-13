package org.upb.fmde.de.graphconditions.ncs;

import java.util.function.BiFunction;

import org.upb.fmde.de.categories.Category;
import org.upb.fmde.de.categories.ComparableArrow;
import org.upb.fmde.de.categories.PatternMatcher;

public  class AllCondConst<Ob, Arr extends ComparableArrow<Arr>>
		extends ConditionalConstraint<Ob, Arr> {

	public AllCondConst(Arr x, BiFunction<Ob, Ob, PatternMatcher<Ob, Arr>> patternMatcher,
			NestableConstraint<Ob, Arr> condition) {
		super(x, patternMatcher, condition, false);
	}

	public AllCondConst(Category<Ob, Arr> cat, BiFunction<Ob, Ob, PatternMatcher<Ob, Arr>> patternMatcher, Arr x,
			NestableConstraint<Ob, Arr> condition) {
		super(cat, patternMatcher, x, condition, false);
	}
}