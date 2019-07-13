package org.upb.fmde.de.graphconditions.ncs;

import java.util.Optional;
import java.util.function.BiFunction;
import java.util.stream.Stream;

import org.upb.fmde.de.categories.Category;
import org.upb.fmde.de.categories.ComparableArrow;
import org.upb.fmde.de.categories.PatternMatcher;

public class BasicConstraint<Ob, Arr extends ComparableArrow<Arr>>
		extends BaseCondiditional<Ob, Arr> 
		implements NestableConstraint<Ob, Arr> {
	

	public BasicConstraint(Category<Ob, Arr> cat, BiFunction<Ob, Ob, PatternMatcher<Ob, Arr>> patternMatcher, Arr x) {
		super(cat, patternMatcher, x);
	}
	
	@Override
	public boolean isSatisfiedByArrow(Arr p) {
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