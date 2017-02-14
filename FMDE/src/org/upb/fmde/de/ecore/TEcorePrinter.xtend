package org.upb.fmde.de.ecore

import org.upb.fmde.de.categories.concrete.graphs.GraphDiagram
import org.upb.fmde.de.categories.concrete.tgraphs.TGraphDiagram
import org.upb.fmde.de.categories.concrete.pgraphs.PGraphDiagram

class TEcorePrinter extends EcorePrinter {
	
	new (TGraphDiagram td){
		super(new PGraphDiagram)
		fillGraphDiagram(td)
	}
	
	def fillGraphDiagram(TGraphDiagram td){
		td.objects.forEach[o | d.objects(o.type.src)]
		td.arrows.forEach[a | d.arrows(a.untyped)]
	}
}
