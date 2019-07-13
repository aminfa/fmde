package org.upb.fmde.de.dsl

import org.upb.fmde.de.categories.concrete.finsets.FinSetPatternMatcher
import org.upb.fmde.de.categories.concrete.finsets.FinSets
import org.upb.fmde.de.categories.concrete.finsets.TotalFunction
import org.upb.fmde.de.categories.concrete.graphs.GraphMorphism
import org.upb.fmde.de.categories.concrete.graphs.GraphPatternMatcher
import org.upb.fmde.de.categories.concrete.graphs.Graphs
import org.upb.fmde.de.categories.concrete.tgraphs.TGraphMorphism
import org.upb.fmde.de.categories.concrete.tgraphs.TGraphs
import org.upb.fmde.de.categories.concrete.tgraphs.TPatternMatcher
import org.upb.fmde.de.graphconditions.ncs.ANDs
import org.upb.fmde.de.graphconditions.ncs.AllCondConst
import org.upb.fmde.de.graphconditions.ncs.BasicConstraint
import org.upb.fmde.de.graphconditions.ncs.ConditionalConstraint
import org.upb.fmde.de.graphconditions.ncs.Contradiction
import org.upb.fmde.de.graphconditions.ncs.ExistCondConst
import org.upb.fmde.de.graphconditions.ncs.InverseCondition
import org.upb.fmde.de.graphconditions.ncs.NestableConstraint
import org.upb.fmde.de.graphconditions.ncs.ORs
import org.upb.fmde.de.graphconditions.ncs.Tautology

import java.util.function.BiFunction

import static java.util.Objects.requireNonNull

class NCSLang {

    static def getCat(arrow) {
        if(arrow instanceof TotalFunction) {
            return FinSets.FinSets
        } else if(arrow instanceof GraphMorphism) {
            return Graphs.Graphs
        } else if(arrow instanceof  TGraphMorphism) {
            return TGraphs.TGraphsFor(arrow.src().type().trg())
        }
        throw new IllegalArgumentException("Arrow unrecognizable: " + arrow.class)
    }

    static def getPM(arrow) {
        if(arrow instanceof TotalFunction) {
            return new BiFunction() {
                @Override
                Object apply(Object o, Object o2) {
                    return new FinSetPatternMatcher(o, o2)
                }
            }
        } else if(arrow instanceof GraphMorphism) {
            return new BiFunction() {
                @Override
                Object apply(Object o, Object o2) {
                    return new GraphPatternMatcher(o,o2)
                }
            }
        } else if(arrow instanceof  TGraphMorphism) {
            return new BiFunction() {
                @Override
                Object apply(Object o, Object o2) {
                    return new TPatternMatcher(o, o2)
                }
            }
        }
        throw new IllegalArgumentException("Arrow unrecognizable: " + arrow.class)
    }

    static BasicConstraint constraint(arrow) {
        requireNonNull arrow
        def pm = getPM(arrow)
        def cat = getCat(arrow)
        BasicConstraint constraint = new BasicConstraint(cat, pm, arrow)
        return constraint
    }


    static AllCondConst all(arrow, NestableConstraint c) {
        requireNonNull arrow
        requireNonNull c
        def pm = getPM(arrow)
        def cat = getCat(arrow)
        return new AllCondConst(cat, pm, arrow, c)
    }

    static ExistCondConst exist(arrow, NestableConstraint c) {
        requireNonNull arrow
        requireNonNull c
        def pm = getPM(arrow)
        def cat = getCat(arrow)
        return new ExistCondConst(cat, pm, arrow, c)
    }

    static InverseCondition negate(NestableConstraint c) {
        requireNonNull c
        return new InverseCondition(c)
    }

    static ORs or(NestableConstraint... cs) {
        List<NestableConstraint> constraintList = new ArrayList<>()
        cs.each {constraintList.add(it)}
        return new ORs(constraintList)
    }

    static ANDs and(NestableConstraint... cs) {
        List<NestableConstraint> constraintList = new ArrayList<>()
        cs.each {constraintList.add(it)}
        return new ANDs(constraintList)
    }

    static Tautology true_() {
        return new Tautology()
    }

    static Contradiction false_() {
        return new Contradiction()
    }
}
