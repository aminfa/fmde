package org.upb.fmde.de.tests;

import static org.upb.fmde.de.categories.concrete.tgraphs.TGraphs.TGraphsFor;

import java.io.IOException;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.junit.BeforeClass;
import org.junit.Test;
import org.upb.fmde.de.categories.colimits.CoLimit;
import org.upb.fmde.de.categories.colimits.pushouts.CoSpan;
import org.upb.fmde.de.categories.colimits.pushouts.Span;
import org.upb.fmde.de.categories.concrete.finsets.FinSet;
import org.upb.fmde.de.categories.concrete.finsets.FinSetDiagram;
import org.upb.fmde.de.categories.concrete.finsets.FinSets;
import org.upb.fmde.de.categories.concrete.finsets.TotalFunction;
import org.upb.fmde.de.categories.concrete.graphs.Graph;
import org.upb.fmde.de.categories.concrete.graphs.GraphMorphism;
import org.upb.fmde.de.categories.concrete.tgraphs.TGraphDiagram;
import org.upb.fmde.de.categories.concrete.tgraphs.TPatternMatcher;
import org.upb.fmde.de.categories.slice.Triangle;
import org.upb.fmde.de.ecore.EMFUtil;

public class TestsEx4 {
	private static final String diagrams = "diagrams/ex4/";

	@BeforeClass
	public static void clear() {
		TestUtil.clear(diagrams);
	}
	
	@Test
	public void colimitsFinSets() throws IOException {
		FinSet L = new FinSet("L", "existingCard");
		FinSet R = new FinSet("R", "existingCard", "buy beer");
		FinSet G = new FinSet("G", "install new sink", "buy some paint");
		
		TotalFunction r = new TotalFunction(L, "r", R).addMapping(L.get("existingCard"), R.get("existingCard"));
		TotalFunction m = new TotalFunction(L, "m", G).addMapping(L.get("existingCard"), G.get("install new sink"));
		
		CoLimit<CoSpan<TotalFunction>, TotalFunction> pushout = FinSets.FinSets.pushout(new Span<TotalFunction>(FinSets.FinSets, r, m));
		
		FinSet wrong = new FinSet("X", "foo");
		TotalFunction v = new TotalFunction(R, "v", wrong);
		TotalFunction h = new TotalFunction(G, "h", wrong);
		R.elts().forEach(x -> v.addMapping(x, wrong.get("foo")));
		G.elts().forEach(x -> h.addMapping(x, wrong.get("foo")));
	
		CoSpan<TotalFunction> wrong_po = new CoSpan<TotalFunction>(FinSets.FinSets, h, v);
		TotalFunction u = pushout.up.apply(wrong_po);
		
		new FinSetDiagram()
				.objects(L, R, G, pushout.obj.left.trg(), wrong)
				.arrows(pushout.obj.left, pushout.obj.right, r, m, u, h, v)
				.saveAsDot(diagrams, "poFinSets")
				.prettyPrint(diagrams, "poFinSets");
	}

	@Test
	public void colimitsGraphs() throws IOException{
		ResourceSet rs = EMFUtil.createDefaultResourceSet();
		EObject root = TestUtil.loadSimpleTrello(rs);
		GraphMorphism[] L_TG_Ecore = TestUtil.loadBoardAsTGraphs(rs, "models/ex3/graphCondition/L.xmi", "L");
		GraphMorphism L = L_TG_Ecore[0];
		Graph TG = L_TG_Ecore[1].src();
		GraphMorphism R = TestUtil.loadBoardAsTGraph(rs, "models/ex3/graphCondition/P.xmi", "R", L_TG_Ecore[1], L_TG_Ecore[2]);
		GraphMorphism G = TestUtil.loadBoardAsTGraph(rs, "models/ex3/Board.xmi", "G", L_TG_Ecore[1], L_TG_Ecore[2]);

		TPatternMatcher pm = new TPatternMatcher(L, R);
		Triangle<Graph, GraphMorphism> r = pm.getMonicMatches().get(0);
		r.label("r");
		
		TPatternMatcher pm_ = new TPatternMatcher(L, G);
		
		List<Triangle<Graph, GraphMorphism>> m = pm_.getMonicMatches();
		for(int i = 0; i < m.size(); i++){
			m.get(i).label("m"); 
			CoLimit<CoSpan<Triangle<Graph, GraphMorphism>>, Triangle<Graph, GraphMorphism>> po = TGraphsFor(TG).pushout(new Span<>(TGraphsFor(TG), r, m.get(i)));
			try {
				TGraphDiagram d = new TGraphDiagram(TG);
				d.objects(L, R, G, po.obj.left.trg())
				 .arrows(r, m.get(i), po.obj.left, po.obj.right);
				TestUtil.prettyPrintTEcore(d, "po_TGraphs_" + i, diagrams);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
