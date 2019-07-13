package org.upb.fmde.de.dsl

import org.upb.fmde.de.categories.concrete.graphs.Graph
import org.upb.fmde.de.categories.concrete.graphs.GraphMorphism
import org.upb.fmde.de.categories.concrete.tgraphs.TGraphMorphism
import spock.lang.Specification

import static org.upb.fmde.de.dsl.ModelLang.*
import static org.upb.fmde.de.categories.concrete.finsets.FinSets.FinSets;

class ModelLangTest extends Specification {
    def "test sets"() {
        given:
        def set1 = set named: "set1", with: [0, 1, 2, 3]

        def set2 = set named: "set2", with: [0, 1, 2, 3, 4, 5, 6]

        def arrowCreator = setArrow.curry named: "up",
                itself: set2

        def  up1 = arrowCreator
                {
                    (it + 1) % 6
                }
        def  up2 = arrowCreator
                {
                    (it + 2) % 6
                }
        def  up3 = arrowCreator
                {
                    (it + 3) % 6
                }
        expect:
        FinSets.compose(up1, up2).isTheSameAs(up3)
    }

    def "test graphs"() {
        when:
        Graph g1 = graph {
            named "g1"
            node "a", {
                to "b"
            }

            connect "b" to "a", "b"

            connect "d", "c" to "a", "b"


        }
        then:
        g1.contains("a")
        g1.contains("d")
        !g1.contains("z")
        g1.label() == "g1"
        g1.edgeFor("a", "b") == ["a-b"]
        g1.edgeFor("b", "d") == []
    }

    def "test graph morphism"() {
        when:
        def g1 = graph {
            connect("a") to("b")
        }
        def g2 = graph {
            add g1
            connect("a") to("e")
        }
        then:
        !("e" in g1)
        ("e" in g2)
        g2.trg()["a-e"] == "e"

        when:
        GraphMorphism arr = arrow {
            src g1
            trg g2
        }
        then:
        arr._E()["a-b"] == "a-b"
    }

    def "test named edges"() {
        when:
        def g1 = graph {
            connect "a" to "b", "c"
            connect "b" to "c"
        }

        def g2 = graph {
            connect "a" to "b"
            connect "selfEdge" from "b" to "b"
        }
        then:
        g2.edgeFor("a", "b") == ["a-b"]
        g2.edgeFor("b", "b") == ["selfEdge"]

        when:
        GraphMorphism arr = arrow {
            src g1
            trg g2
            map "c" to "b"
            map "b-c" to "selfEdge"
        }
        then:
        arr._E()["b-c"] == "selfEdge"

    }

    def "test typed graph"() {
        when:
        def tg = graph {
            connect "A" to "B", "C"
            connect "B" to "B"
        }
        def g1 = graph {
            connect "a" to "b", "c"
            connect "b" to "b"
        }
        def t_g1 = typedGraph {
            typeGraph = tg
            add g1
            typeEach {
                node -> node.toUpperCase()
            }
        }
        then:
        "a" in t_g1.getGraph()
        "A" in t_g1.getTypeGraph()

        when:
        TGraphMorphism arr = arrow {
            src typedGraph {
                typeGraph = tg
                add graph {
                    connect "a" to "c"
                    connect "a-b1" from "a" to "b1"
                    connect "b1" to "b2"
                    connect "b2" to "b1"
                }
                typeEach {
                    node -> String.valueOf(node.charAt(0).toUpperCase())
                }
            }
            trg t_g1
            map "b1" to "b"
            map "b2" to "b"
            map "a-b1" to "a-b"
            map "b1-b2" to "b-b"
            map "b2-b1" to "b-b"
        }
        then:
        arr.f._E()["a-b1"] == "a-b"
    }



}
