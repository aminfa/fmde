package org.upb.fmde.de.dsl

import org.upb.fmde.de.graphconditions.ncs.NestableConstraint
import spock.lang.Specification

import static ModelLang.*
import static org.upb.fmde.de.dsl.NCSLang.*

class NCSExample3 extends Specification {

    static def typer = { String objName ->
        return ["Box", "Phone", "Headphone", "Charger", "Scissor"].find {
            typeName -> return objName.startsWith(typeName.toLowerCase())
        }
    }

    static def tG = graph {
        named "ItemTypes"
        connect "Headphone", "Phone", "Charger", "Scissor", "Box" to "Box"
    }

    def "EXAMPLE 3" () {
        given:
        def P = typedGraph {
            typeGraph = tG
            typeEach typer
        }

        def allBoxes = typedGraph {
            node "box"

            typeGraph = tG
            typeEach typer

        }

        def C1 = typedGraph {
            connect "scissor" to "box"
            connect "charger" to "box"

            typeGraph = tG
            typeEach typer

        }

        def C2 = typedGraph {
            connect "scissor" to "box"
            connect "headphone" to "box"

            typeGraph = tG
            typeEach typer
        }

        def emptyArr = arrow {
            src P
            trg allBoxes
        }
        def x1 = arrow {
            src allBoxes
            trg C1
        }
        def x2 = arrow {
            src allBoxes
            trg C2
        }
        def notX1 = negate constraint(x1)
        def notX2 = negate constraint(x2)

        NestableConstraint ncs = all x1, and( notX1, notX2 )

        expect :
        ncs.isSatisfiedByObj(G) == doesSatisfy

        where:
        G << [
                typedGraph {
                    connect "charger" to "box"

                    typeGraph = tG
                    typeEach typer
                },
                typedGraph {
                    connect "charger", "headphone" to "box"

                    typeGraph = tG
                    typeEach typer
                },
                typedGraph {
                    connect "charger", "scissor" to "box"

                    typeGraph = tG
                    typeEach typer
                }
        ]
        doesSatisfy << [
                true,
                true,
                false
        ]

    }
}
