package org.upb.fmde.de.graphconditions.ncs;

import java.util.Optional;
import java.util.function.BiFunction;
import java.util.stream.Stream;

import org.upb.fmde.de.categories.Category;
import org.upb.fmde.de.categories.ComparableArrow;
import org.upb.fmde.de.categories.PatternMatcher;

public  abstract class BaseCondiditional <Ob, Arr extends ComparableArrow<Arr>> implements NestableConstraint<Ob, Arr> {
	
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