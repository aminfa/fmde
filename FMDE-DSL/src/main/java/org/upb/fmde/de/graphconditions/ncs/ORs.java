package org.upb.fmde.de.graphconditions.ncs;

import java.util.Collection;

import org.upb.fmde.de.categories.ComparableArrow;

public class ORs<Ob, Arr extends ComparableArrow<Arr>>
	extends ConditionCollection<Ob, Arr> {
	public ORs() {
		super(false);
	}
	
	public ORs(Collection<? extends NestableConstraint<Ob, Arr>> c) {
		super(c, false);
	}
	
	public ORs(int initialCapacity) {
		super(initialCapacity, false);
	}
}