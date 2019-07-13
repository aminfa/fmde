package org.upb.fmde.de.graphconditions.ncs;

import org.upb.fmde.de.categories.ComparableArrow;

public class Tautology <Ob, Arr extends ComparableArrow<Arr>>
		implements 	NestableConstraint<Ob, Arr> {

	@Override
	public boolean isSatisfiedByArrow(Arr m) {
		return true;
	}

	@Override
	public boolean isSatisfiedByObj(Ob o) {
		return true;
	}

}