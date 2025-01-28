package Uppaal;

import danieljnm.sm2ta.StateMachine.State;
import danieljnm.sm2ta.StateMachine.StateMachine;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure2;

@SuppressWarnings("all")
public class Xml {
  public Declaration declaration;

  public List<Template> templates = CollectionLiterals.<Template>newArrayList();

  public Uppaal.System system;

  public Xml(final StateMachine stateMachine) {
    Declaration _declaration = new Declaration(stateMachine);
    this.declaration = _declaration;
    this.templates = this.setTemplates(stateMachine);
    Uppaal.System _system = new Uppaal.System(this.templates);
    this.system = _system;
  }

  public ArrayList<Template> setTemplates(final StateMachine stateMachine) {
    ArrayList<Template> _xblockexpression = null;
    {
      final ArrayList<Template> templates = CollectionLiterals.<Template>newArrayList();
      final Template template = new Template(stateMachine.name);
      final Function1<State, Integer> _function = (State it) -> {
        return Integer.valueOf(it.index);
      };
      final Procedure2<State, Integer> _function_1 = (State state, Integer index) -> {
        if (state.isInitial) {
          template.initial = state.name;
        }
        if ((!state.isNested)) {
          template.location(state);
        }
        templates.add(template);
      };
      IterableExtensions.<State>forEach(IterableExtensions.<State, Integer>sortBy(stateMachine.states.values(), _function), _function_1);
      _xblockexpression = templates;
    }
    return _xblockexpression;
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
