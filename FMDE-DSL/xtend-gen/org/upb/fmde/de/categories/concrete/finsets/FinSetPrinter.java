package org.upb.fmde.de.categories.concrete.finsets;

import java.util.Collection;
import java.util.List;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.upb.fmde.de.categories.concrete.finsets.FinSet;
import org.upb.fmde.de.categories.concrete.finsets.FinSetDiagram;
import org.upb.fmde.de.categories.concrete.finsets.TotalFunction;
import org.upb.fmde.de.categories.diagrams.DotPrinter;

@SuppressWarnings("all")
public class FinSetPrinter implements DotPrinter {
  private FinSetDiagram d;
  
  public FinSetPrinter(final FinSetDiagram d) {
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
      Collection<FinSet> _objects = this.d.getObjects();
      for(final FinSet set : _objects) {
        {
          List<Object> _elts = set.elts();
          for(final Object o : _elts) {
            _builder.append("class \"");
            String _label = set.label();
            _builder.append(_label);
            _builder.append(".");
            _builder.append(o);
            _builder.append("\"");
            _builder.newLineIfNotEmpty();
          }
        }
      }
    }
    _builder.newLine();
    {
      Collection<TotalFunction> _arrows = this.d.getArrows();
      for(final TotalFunction f : _arrows) {
        {
          List<Object> _elts_1 = f.src().elts();
          for(final Object v : _elts_1) {
            _builder.append("\"");
            String _label_1 = f.src().label();
            _builder.append(_label_1);
            _builder.append(".");
            _builder.append(v);
            _builder.append("\" --> \"");
            String _label_2 = f.trg().label();
            _builder.append(_label_2);
            _builder.append(".");
            Object _map = f.map(v);
            _builder.append(_map);
            _builder.append("\" : \"");
            String _label_3 = f.label();
            _builder.append(_label_3);
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
