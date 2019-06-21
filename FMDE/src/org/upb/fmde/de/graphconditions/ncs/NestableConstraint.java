package org.upb.fmde.de.graphconditions.ncs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.stream.Stream;

import org.upb.fmde.de.categories.Category;
import org.upb.fmde.de.categories.ComparableArrow;
import org.upb.fmde.de.categories.PatternMatcher;

public interface NestableConstraint<Ob, Arr extends ComparableArrow<Arr>> {

	public boolean isSatisfiedByArrow(Arr p);
	
	public boolean isSatisfiedByObj(Ob o);
	
	public default Category<Ob, Arr> getCat() {
		throw new UnsupportedOperationException("Category not defined..");
	}
	
}

abstract class BaseCondiditional <Ob, Arr extends ComparableArrow<Arr>> implements NestableConstraint<Ob, Arr> {
	
	protected final Category<Ob, Arr> cat;
	
	protected final Arr x;
	
	protected final BiFunction<Ob, Ob, PatternMatcher<Ob, Arr>> patternMatcher;
	
	public BaseCondiditional(Category<Ob, Arr> cat, BiFunction<Ob, Ob, PatternMatcher<Ob, Arr>> patternMatcher, Arr x) {
		this.cat = cat;
		this.patternMatcher = patternMatcher;
		this.x = x;
	}
	
	
	@Override
	public boolean isSatisfiedByObj(Ob G) {
		Ob P = getCat().source(x);
		// determine all m_p and filter for commutativity
		PatternMatcher<Ob, Arr> premise = patternMatcher.apply(P, G);
		
		Stream<Arr> ps = premise.getMonicMatches()
				.stream();
		
		if(!isUniversal()) {
			Optional<Arr> notMatched = ps
					.filter(arr_P_G -> !isSatisfiedByArrow(arr_P_G))
					.findAny();
			return !notMatched.isPresent();
		} else {
			Optional<Arr> notMatched = ps
					.filter(arr_P_G -> isSatisfiedByArrow(arr_P_G))
					.findAny();
			return notMatched.isPresent();
		}
		
	}
	
	abstract boolean isUniversal();
	

	@Override
	public final Category<Ob, Arr> getCat() {
		return cat;
	}
	
	public final Arr getX() {
		return x;
	}
}


class BasicConstraint<Ob, Arr extends ComparableArrow<Arr>> 
		extends BaseCondiditional<Ob, Arr> 
		implements NestableConstraint<Ob, Arr> {
	

	public BasicConstraint(Category<Ob, Arr> cat, BiFunction<Ob, Ob, PatternMatcher<Ob, Arr>> patternMatcher, Arr x) {
		super(cat, patternMatcher, x);
	}
	
	@Override
	public boolean isSatisfiedByArrow(Arr p) {
		if(cat.source(p) != cat.source(x)) {
			throw new IllegalArgumentException("Sources dont match: source(" + p + ") ≠ source(" + x + ")");
		}
		
		Ob G = cat.target(p);
		
		Ob C = cat.target(x);
		
		// determine all m_p and filter for commutativity
		PatternMatcher<Ob, Arr> premise = patternMatcher.apply(C, G);
		Stream<Arr> m_pi = premise.getMonicMatches()
				.stream()
				.filter(q -> p.isTheSameAs(cat.compose(x, q)));
		
		Optional<Arr> q = m_pi.findAny();
		
		return q.isPresent();
	}


	@Override
	boolean isUniversal() {
		return false;
	}
}

class ConditionalConstraint<Ob, Arr extends ComparableArrow<Arr>> 
		extends BaseCondiditional<Ob, Arr> 
		implements NestableConstraint<Ob, Arr> {
	
	
	protected NestableConstraint<Ob,Arr> condition;
	
	boolean existential = true;

	public ConditionalConstraint(Category<Ob, Arr> cat, BiFunction<Ob, Ob, PatternMatcher<Ob, Arr>> patternMatcher, Arr x, NestableConstraint<Ob,Arr> condition, boolean isExistQuantifier) {
		super(cat, patternMatcher, x);
		this.condition = condition;
		this.existential = isExistQuantifier;
		if(condition.getCat() != cat) {
			throw new IllegalArgumentException("Categories dont match: " 
						+ condition.getCat().getClass().getSimpleName() 
						+ "≠" 
						+ cat);
		}
	}

	public ConditionalConstraint(Arr x,BiFunction<Ob, Ob, PatternMatcher<Ob, Arr>> patternMatcher,  NestableConstraint<Ob,Arr> condition, boolean isExistQuantifier) {
		super(Objects.requireNonNull(condition.getCat()), patternMatcher, x);
		this.condition = condition;
		this.existential = isExistQuantifier;
	}
	

	public boolean isExistQuantifier() {
		return existential;
	}

	public void setExistQuantifier(boolean isExistQuantifier) {
		this.existential = isExistQuantifier;
	}

	@Override
	public boolean isSatisfiedByArrow(Arr p) {
		if(cat.source(p) != cat.source(x)) {
			throw new IllegalArgumentException("Sources dont match: source(" + p + ") ≠ source(" + x + ")");
		}
		
		Ob G = cat.target(p);
		Ob C = cat.target(x);
		
		// determine all m_p and filter for commutativity
		PatternMatcher<Ob, Arr> premise = patternMatcher.apply(C, G);
		Stream<Arr> m_pi = premise.getMonicMatches()
				.stream();
		if(existential) {
			Optional<Arr> commuteMatch = m_pi
				.filter(q -> p.isTheSameAs(cat.compose(x, q)))
				.findAny();
			return commuteMatch.isPresent(); // satisfied if one arrow commutes
		} else {
			Optional<Arr> commuteNonmatch = m_pi
					.filter(q -> !p.isTheSameAs(cat.compose(x, q)))
					.findAny();
			return !commuteNonmatch.isPresent(); // satisfied if all arrow commute
		}
	}


	@Override
	boolean isUniversal() {
		return !existential;
	}
	
}

class AllCondConst<Ob, Arr extends ComparableArrow<Arr>>  
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


class ExistCondConst<Ob, Arr extends ComparableArrow<Arr>>  
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

class Tautology <Ob, Arr extends ComparableArrow<Arr>> 
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

class Contradiction<Ob, Arr extends ComparableArrow<Arr>> 
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

class InverseCondition<Ob, Arr extends ComparableArrow<Arr>> 
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

class ConditionCollection<Ob, Arr extends ComparableArrow<Arr>> 
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
}