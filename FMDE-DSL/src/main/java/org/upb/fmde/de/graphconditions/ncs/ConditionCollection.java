package org.upb.fmde.de.graphconditions.ncs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

import org.upb.fmde.de.categories.Category;
import org.upb.fmde.de.categories.ComparableArrow;

public  class ConditionCollection<Ob, Arr extends ComparableArrow<Arr>>
	extends ArrayList<NestableConstraint<Ob, Arr>>
	implements 	NestableConstraint<Ob, Arr> {
	
	boolean conjunction;

	public ConditionCollection(boolean conjunction) {
		super();
		this.conjunction = conjunction;
	}

	public ConditionCollection(Collection<? extends NestableConstraint<Ob, Arr>> c, boolean conjunction) {
		super(c);
		this.conjunction = conjunction;
	}

	public ConditionCollection(int initialCapacity, boolean conjunction) {
		super(initialCapacity);
		this.conjunction = conjunction;
	}

	public boolean isConjunction() {
		return conjunction;
	}
	
	@Override
	public boolean isSatisfiedByArrow(Arr p) {
		if(conjunction) {
			return this.stream().allMatch(cond -> cond.isSatisfiedByArrow(p));
		} else {
			return this.stream().anyMatch(cond -> cond.isSatisfiedByArrow(p));
		}
	}

	@Override
	public boolean isSatisfiedByObj(Ob o) {
		if(conjunction) {
			return this.stream().allMatch(cond -> cond.isSatisfiedByObj(o));
		} else {
			return this.stream().anyMatch(cond -> cond.isSatisfiedByObj(o));
		}
	}
	
	@Override
	public Category<Ob, Arr> getCat() {
		for(NestableConstraint<Ob, Arr> elem : this) {
			try {
				return Objects.requireNonNull(elem.getCat());
			} catch(Exception ex) {}
		}
		throw new IllegalStateException("No element had a category.");
	}
}