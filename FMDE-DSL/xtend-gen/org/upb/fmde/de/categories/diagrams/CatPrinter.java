package org.upb.fmde.de.categories.diagrams;

import java.util.Collection;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.upb.fmde.de.categories.diagrams.Diagram;
import org.upb.fmde.de.categories.diagrams.DotPrinter;

@SuppressWarnings("all")
public class CatPrinter<Ob extends Object, Arr extends Object> implements DotPrinter {
  private Diagram<Ob, Arr> d;
  
  public CatPrinter(final Diagram<Ob, Arr> d) {
    this.d = d;
  }
  
  @Override
  public String print() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("@startuml");
    _builder.newLine();
    _builder.append("digraph Diagram {");
    _builder.newLine();
    {
      Collection<Ob> _objects = this.d.getObjects();
      for(final Ob o : _objects) {
        _builder.append("  ");
        _builder.append("\"");
        String _showOb = this.d.getCat().showOb(o);
        _builder.append(_showOb, "  ");
        _builder.append("\";");
        _builder.newLineIfNotEmpty();
      }
    }
    {
      Collection<Arr> _arrows = this.d.getArrows();
      for(final Arr a : _arrows) {
        _builder.append("  ");
        _builder.append("\"");
        String _showOb_1 = this.d.getCat().showOb(this.d.getCat().source(a));
        _builder.append(_showOb_1, "  ");
        _builder.append("\"->\"");
        String _showOb_2 = this.d.getCat().showOb(this.d.getCat().target(a));
        _builder.append(_showOb_2, "  ");
        _builder.append("\" [label=\" ");
        String _showArr = this.d.getCat().showArr(a);
        _builder.append(_showArr, "  ");
        _builder.append("\"];");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.append("}");
    _builder.newLine();
    _builder.append("@enduml");
    _builder.newLine();
    return _builder.toString();
  }
}
