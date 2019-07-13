package org.upb.fmde.de.graphconditions.ncs;

import java.util.Objects;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.stream.Stream;

import org.upb.fmde.de.categories.Category;
import org.upb.fmde.de.categories.ComparableArrow;
import org.upb.fmde.de.categories.PatternMatcher;

public class ConditionalConstraint<Ob, Arr extends ComparableArrow<Arr>>
		extends BaseCondiditional<Ob, Arr> 
		implements NestableConstraint<Ob, Arr> {
	
	
	protected NestableConstraint<Ob,Arr> condition;
	
	boolean existential = true;

	public ConditionalConstraint(Category<Ob, Arr> cat, BiFunction<Ob, Ob, PatternMatcher<Ob, Arr>> patternMatcher, Arr x, NestableConstraint<Ob,Arr> condition, boolean isExistQuantifier) {
		super(cat, patternMatcher, x);
		this.condition = condition;
		this.existential = isExistQuantifier;
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
                .filter(q -> condition.isSatisfiedByArrow(q))
				.findAny();
			return commuteMatch.isPresent(); // satisfied if one arrow that commutes sat the c
		} else {
			Optional<Arr> commuteNonmatch = m_pi
					.filter(q -> p.isTheSameAs(cat.compose(x, q)))
                    .filter(q -> !condition.isSatisfiedByArrow(q))
                    .findAny();
			return !commuteNonmatch.isPresent(); // satisfied if one arrow that commutes doesnt match sat the c
		}
	}


	@Override
	boolean isUniversal() {
		return !existential;
	}
	
}