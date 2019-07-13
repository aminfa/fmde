package org.upb.fmde.de.ecore;

import java.util.function.Consumer;
import org.upb.fmde.de.categories.concrete.graphs.GraphDiagram;
import org.upb.fmde.de.categories.concrete.tgraphs.TGraph;
import org.upb.fmde.de.categories.concrete.tgraphs.TGraphDiagram;
import org.upb.fmde.de.categories.concrete.tgraphs.TGraphMorphism;
import org.upb.fmde.de.ecore.EcorePrinter;

@SuppressWarnings("all")
public class TEcorePrinter extends EcorePrinter {
  public TEcorePrinter(final TGraphDiagram td) {
    super(new GraphDiagram());
    this.fillGraphDiagram(td);
  }
  
  public void fillGraphDiagram(final TGraphDiagram td) {
    final Consumer<TGraph> _function = (TGraph o) -> {
      this.d.objects(o.type().src());
    };
    td.getObjects().forEach(_function);
    final Consumer<TGraphMorphism> _function_1 = (TGraphMorphism a) -> {
      this.d.arrows(a.untyped());
    };
    td.getArrows().forEach(_function_1);
  }
}
