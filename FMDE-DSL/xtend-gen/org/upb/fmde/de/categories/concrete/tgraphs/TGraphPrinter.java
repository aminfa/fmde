package org.upb.fmde.de.categories.concrete.tgraphs;

import java.util.Collection;
import java.util.List;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.upb.fmde.de.categories.concrete.tgraphs.TGraph;
import org.upb.fmde.de.categories.concrete.tgraphs.TGraphDiagram;
import org.upb.fmde.de.categories.concrete.tgraphs.TGraphMorphism;
import org.upb.fmde.de.categories.diagrams.DotPrinter;

@SuppressWarnings("all")
public class TGraphPrinter implements DotPrinter {
  private TGraphDiagram d;
  
  public TGraphPrinter(final TGraphDiagram d) {
    this.d = d;
  }
  
  @Override
  public String print() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("@startuml");
    _builder.newLine();
    _builder.append("skinparam shadowing false");
    _builder.newLine();
    _builder.append("hide members");
    _builder.newLine();
    _builder.append("hide circle");
    _builder.newLine();
    {
      Collection<TGraph> _objects = this.d.getObjects();
      for(final TGraph graph : _objects) {
        {
          List<Object> _elts = graph.type().src().vertices().elts();
          for(final Object o : _elts) {
            _builder.append("class \"");
            String _showOb = this.d.getCat().showOb(graph);
            _builder.append(_showOb);
            _builder.append(".");
            _builder.append(o);
            _builder.append(" : ");
            Object _map = graph.type()._V().map(o);
            _builder.append(_map);
            _builder.append("\"");
            _builder.newLineIfNotEmpty();
          }
        }
        {
          List<Object> _elts_1 = graph.type().src().edges().elts();
          for(final Object e : _elts_1) {
            _builder.append("\"");
            String _showOb_1 = this.d.getCat().showOb(graph);
            _builder.append(_showOb_1);
            _builder.append(".");
            Object _map_1 = graph.type().src().src().map(e);
            _builder.append(_map_1);
            _builder.append(" : ");
            Object _map_2 = graph.type()._V().map(graph.type().src().src().map(e));
            _builder.append(_map_2);
            _builder.append("\"-->\"");
            String _showOb_2 = this.d.getCat().showOb(graph);
            _builder.append(_showOb_2);
            _builder.append(".");
            Object _map_3 = graph.type().src().trg().map(e);
            _builder.append(_map_3);
            _builder.append(" : ");
            Object _map_4 = graph.type()._V().map(graph.type().src().trg().map(e));
            _builder.append(_map_4);
            _builder.append("\" : \"");
            _builder.append(e);
            _builder.append("\"");
            _builder.newLineIfNotEmpty();
          }
        }
      }
    }
    _builder.newLine();
    {
      Collection<TGraphMorphism> _arrows = this.d.getArrows();
      for(final TGraphMorphism f : _arrows) {
        {
          List<Object> _elts_2 = f.untyped()._V().src().elts();
          for(final Object v : _elts_2) {
            _builder.append("\"");
            String _showOb_3 = this.d.getCat().showOb(f.src());
            _builder.append(_showOb_3);
            _builder.append(".");
            _builder.append(v);
            _builder.append(" : ");
            Object _map_5 = f.src().type()._V().map(v);
            _builder.append(_map_5);
            _builder.append("\" --> \"");
            String _showOb_4 = this.d.getCat().showOb(f.trg());
            _builder.append(_showOb_4);
            _builder.append(".");
            Object _map_6 = f.untyped()._V().map(v);
            _builder.append(_map_6);
            _builder.append(" : ");
            Object _map_7 = f.trg().type()._V().map(f.untyped()._V().map(v));
            _builder.append(_map_7);
            _builder.append("\" : \"");
            String _label = f.label();
            _builder.append(_label);
            _builder.append("\"");
            _builder.newLineIfNotEmpty();
          }
        }
      }
    }
    _builder.append("@enduml");
    _builder.newLine();
    return _builder.toString();
  }
}
