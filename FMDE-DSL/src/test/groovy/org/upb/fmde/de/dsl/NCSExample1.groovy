package org.upb.fmde.de.dsl

import org.upb.fmde.de.graphconditions.ncs.BasicConstraint
import spock.lang.Specification

import static ModelLang.*
import static org.upb.fmde.de.dsl.NCSLang.constraint

class NCSExample1 extends Specification {

    static def typer = { String objName ->
        return ["Box", "Phone", "Headphone", "Charger", "Scissor"].find {
            typeName -> return objName.startsWith(typeName.toLowerCase())
        }
    }

    static def tG = graph {
        named "ItemTypes"
        connect "Headphone", "Phone", "Charger", "Scissor", "Box" to "Box"
    }

    def "EXAMPLE 1" () {
        given:
        def P = typedGraph {
            connect "phone" to "box"

            typeGraph = tG
            typeEach typer
        }
        def C = typedGraph {
            connect "phone" to "box"
            connect "headphone" to "box"

            typeGraph = tG
            typeEach typer

        }
        def x = arrow {
            src P
            trg C
        }
        BasicConstraint bc = constraint x


        expect:
        bc.isSatisfiedByObj(G) == doesSatisfy

        where:
        G << [
                typedGraph {
                    connect "phone1" to "box1"

                    connect "headphone" to "box2"
                    connect "box1" to "box2"

                    typeGraph = tG
                    typeEach typer
                },
                typedGraph {
                    connect "phone1" to "box1"

                    connect "headphone" to "box1"
                    connect "box1" to "box2"

                    typeGraph = tG
                    typeEach typer
                }
        ]
        doesSatisfy << [
                false,
                true
        ]
    }
}
