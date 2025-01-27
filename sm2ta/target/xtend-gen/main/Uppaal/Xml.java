package Uppaal;

import danieljnm.sm2ta.StateMachine.StateMachine;
import java.util.List;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.IterableExtensions;

@SuppressWarnings("all")
public class Xml {
  public Declaration declaration;

  public List<Template> templates = CollectionLiterals.<Template>newArrayList();

  public List<Transition> transitions = CollectionLiterals.<Transition>newArrayList();

  public List<String> systems = CollectionLiterals.<String>newArrayList();

  public Xml(final StateMachine stateMachine) {
    Declaration _declaration = new Declaration(stateMachine);
    this.declaration = _declaration;
  }

  @Override
  public String toString() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
    _builder.newLine();
    _builder.append("<nta>");
    _builder.newLine();
    _builder.append("\t");
    _builder.append(this.declaration, "\t");
    _builder.newLineIfNotEmpty();
    {
      for(final Template template : this.templates) {
        _builder.append("\t");
        _builder.append(template, "\t");
        _builder.newLineIfNotEmpty();
      }
    }
    {
      boolean _isEmpty = this.systems.isEmpty();
      boolean _not = (!_isEmpty);
      if (_not) {
        _builder.append("\t");
        _builder.append("<system>");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t");
        String _join = IterableExtensions.join(this.systems, ", ");
        _builder.append(_join, "\t\t");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        _builder.append("</system>");
        _builder.newLine();
      }
    }
    _builder.append("</nta>");
    _builder.newLine();
    return _builder.toString();
  }
}
