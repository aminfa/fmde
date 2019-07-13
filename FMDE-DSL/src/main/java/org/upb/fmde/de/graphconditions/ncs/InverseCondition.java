package org.upb.fmde.de.graphconditions.ncs;

import org.upb.fmde.de.categories.ComparableArrow;

public  class InverseCondition<Ob, Arr extends ComparableArrow<Arr>>
		implements 	NestableConstraint<Ob, Arr> {
	

	protected NestableConstraint<Ob,Arr> innerCondition;
	
	public InverseCondition(NestableConstraint<Ob,Arr> innerCondition) {
		this.innerCondition =  innerCondition;
	}

	@Override
	public boolean isSatisfiedByArrow(Arr m) {
		return ! innerCondition.isSatisfiedByArrow(m);
	}

	@Override
	public boolean isSatisfiedByObj(Ob o) {
		return ! innerCondition.isSatisfiedByObj(o);
	}

}