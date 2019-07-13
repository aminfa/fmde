package org.upb.fmde.de.categories.concrete.graphs;

import java.util.Collection;
import java.util.List;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.upb.fmde.de.categories.concrete.graphs.Graph;
import org.upb.fmde.de.categories.concrete.graphs.GraphDiagram;
import org.upb.fmde.de.categories.concrete.graphs.GraphMorphism;
import org.upb.fmde.de.categories.diagrams.DotPrinter;

@SuppressWarnings("all")
public class GraphPrinter implements DotPrinter {
  private GraphDiagram d;
  
  public GraphPrinter(final GraphDiagram d) {
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
      Collection<Graph> _objects = this.d.getObjects();
      for(final Graph graph : _objects) {
        {
          List<Object> _elts = graph.vertices().elts();
          for(final Object o : _elts) {
            _builder.append("class \"");
            String _label = graph.label();
            _builder.append(_label);
            _builder.append(".");
            _builder.append(o);
            _builder.append("\"");
            _builder.newLineIfNotEmpty();
          }
        }
        {
          List<Object> _elts_1 = graph.edges().elts();
          for(final Object e : _elts_1) {
            _builder.append("\"");
            String _label_1 = graph.label();
            _builder.append(_label_1);
            _builder.append(".");
            Object _map = graph.src().map(e);
            _builder.append(_map);
            _builder.append("\"-->\"");
            String _label_2 = graph.label();
            _builder.append(_label_2);
            _builder.append(".");
            Object _map_1 = graph.trg().map(e);
            _builder.append(_map_1);
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
      Collection<GraphMorphism> _arrows = this.d.getArrows();
      for(final GraphMorphism f : _arrows) {
        {
          List<Object> _elts_2 = f._V().src().elts();
          for(final Object v : _elts_2) {
            _builder.append("\"");
            String _label_3 = f.src().label();
            _builder.append(_label_3);
            _builder.append(".");
            _builder.append(v);
            _builder.append("\" --> \"");
            String _label_4 = f.trg().label();
            _builder.append(_label_4);
            _builder.append(".");
            Object _map_2 = f._V().map(v);
            _builder.append(_map_2);
            _builder.append("\" : \"");
            String _label_5 = f.label();
            _builder.append(_label_5);
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
