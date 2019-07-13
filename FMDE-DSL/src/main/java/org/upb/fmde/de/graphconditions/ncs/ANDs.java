package org.upb.fmde.de.graphconditions.ncs;

import java.util.Collection;

import org.upb.fmde.de.categories.ComparableArrow;

public  class ANDs<Ob, Arr extends ComparableArrow<Arr>>
	extends ConditionCollection<Ob, Arr> {
	public ANDs() {
		super(true);
	}

	public ANDs(Collection<? extends NestableConstraint<Ob, Arr>> c) {
		super(c, true);
	}

	public ANDs(int initialCapacity) {
		super(initialCapacity, true);
	}
}