package Uppaal;

import danieljnm.sm2ta.StateMachine.StateMachine;
import java.util.List;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;

@SuppressWarnings("all")
public class Xml {
  public Declaration declaration;

  public List<Template> templates = CollectionLiterals.<Template>newArrayList();

  public List<Transition> transitions = CollectionLiterals.<Transition>newArrayList();

  public Uppaal.System system;

  public Xml(final StateMachine stateMachine) {
    Declaration _declaration = new Declaration(stateMachine);
    this.declaration = _declaration;
    Uppaal.System _system = new Uppaal.System(this.templates);
    this.system = _system;
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
    _builder.append("\t");
    _builder.append(this.system, "\t");
    _builder.newLineIfNotEmpty();
    _builder.append("</nta>");
    _builder.newLine();
    return _builder.toString();
  }
}
