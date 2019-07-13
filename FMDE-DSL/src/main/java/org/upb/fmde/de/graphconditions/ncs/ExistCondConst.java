package org.upb.fmde.de.graphconditions.ncs;

import java.util.function.BiFunction;

import org.upb.fmde.de.categories.Category;
import org.upb.fmde.de.categories.ComparableArrow;
import org.upb.fmde.de.categories.PatternMatcher;

public class ExistCondConst<Ob, Arr extends ComparableArrow<Arr>>
		extends ConditionalConstraint<Ob, Arr> {

	public ExistCondConst(Arr x, BiFunction<Ob, Ob, PatternMatcher<Ob, Arr>> patternMatcher,
			NestableConstraint<Ob, Arr> condition) {
		super(x, patternMatcher, condition, true);
	}

	public ExistCondConst(Category<Ob, Arr> cat, BiFunction<Ob, Ob, PatternMatcher<Ob, Arr>> patternMatcher, Arr x,
			NestableConstraint<Ob, Arr> condition) {
		super(cat, patternMatcher, x, condition, true);
	}
	
}