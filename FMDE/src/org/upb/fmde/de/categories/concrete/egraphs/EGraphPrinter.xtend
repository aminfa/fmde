package org.upb.fmde.de.categories.concrete.egraphs

import org.upb.fmde.de.categories.diagrams.DotPrinter

class EGraphPrinter implements DotPrinter {

    EGraphDiagram d

    new (EGraphDiagram d){
        this.d = d
    }

    override print() { // TODO: EGraphMorphisms; separaten Sub-Graph für Attribute, da wir diese ja nur einmal haben wollen
        return '''
digraph G
{
    subgraph cluster_attributes
    {
        «FOR o : d.attributes»
             "«o»" [shape=rect, color=lightgray, style=filled, width=0, height=0, margin=.05]
        «ENDFOR»
        label = "Attributes"
    }

    «FOR graph : d.objects»
        subgraph cluster_«graph.label»
        {
        	label = "«graph.label»"
        	color = cornflowerblue
        	fontcolor = cornflowerblue
        	style = bold
            «FOR e : graph.Eg.elts»
                "«graph.label».«graph.sourceG.map(e)»" -> "«graph.label».«e»" [arrowhead=none]
                "«graph.label».«e»" -> "«graph.label».«graph.targetG.map(e)»"
            «ENDFOR»
            «FOR e : graph.Ena.elts»
                "«graph.label».«graph.sourceNA.map(e)»" -> "«graph.targetNA.map(e)»" [label="«e»", color=lightgray, fontcolor=gray, style=dashed]
            «ENDFOR»
            «FOR e : graph.Eea.elts»
                "«graph.label».«graph.sourceEA.map(e)»" -> "«graph.targetEA.map(e)»" [label="«e»", color=lightgray, fontcolor=gray, style=dashed]
            «ENDFOR»
            «FOR e : graph.Eg.elts»
                "«graph.label».«e»" [label="«e»", shape=none, width=0, height=0, margin=0]
            «ENDFOR»
            «FOR v : graph.Vg.elts»
                "«graph.label».«v»" [label="«v»"]
	        «ENDFOR»
        }
    «ENDFOR»

    «FOR f : d.arrows»
        «FOR v : f.fVg.src.elts»
            "«f.src.label».«v»" -> "«f.trg.label».«f.fVg.map(v)»" [label="«f.label»", style=bold, fontname="times-bold", color="#3E5C91", fontcolor="#3E5C91"]
        «ENDFOR»
        «FOR e : f.fEg.src.elts»
            "«f.src.label».«e»" -> "«f.trg.label».«f.fEg.map(e)»" [label="«f.label»", style=bold, fontname="times-bold", color="#3E5C91", fontcolor="#3E5C91"]
        «ENDFOR»
    «ENDFOR»
}
        '''
    }
}
