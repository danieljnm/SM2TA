package Uppaal;

import danieljnm.sm2ta.StateMachine.State;
import danieljnm.sm2ta.StateMachine.StateMachine;
import danieljnm.sm2ta.StateMachine.Transition;
import java.util.List;
import java.util.function.Consumer;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.StringExtensions;

@SuppressWarnings("all")
public class Query {
  public List<String> queries;

  public Query(final StateMachine stateMachine) {
    this.queries = CollectionLiterals.<String>newArrayList();
    this.queries.add("A[] not deadlock");
    final Consumer<String> _function = (String it) -> {
      this.queries.add(it);
    };
    this.errorStates(stateMachine).forEach(_function);
    final Consumer<State> _function_1 = (State state) -> {
      final Function1<Transition, Boolean> _function_2 = (Transition it) -> {
        boolean _isNullOrEmpty = StringExtensions.isNullOrEmpty(it.guard);
        return Boolean.valueOf((!_isNullOrEmpty));
      };
      final Consumer<Transition> _function_3 = (Transition t) -> {
        final String negatedGuard = (("not (" + t.guard) + ")");
        StringConcatenation _builder = new StringConcatenation();
        _builder.append("A[] (");
        _builder.append(stateMachine.name);
        _builder.append(".");
        _builder.append(state.name);
        _builder.append(" and ");
        String _format = this.format(negatedGuard);
        _builder.append(_format);
        _builder.append(" imply (");
        _builder.append(stateMachine.name);
        _builder.append(".");
        _builder.append(state.name);
        _builder.append("))");
        this.queries.add(_builder.toString());
      };
      IterableExtensions.<Transition>filter(state.transitions, _function_2).forEach(_function_3);
    };
    stateMachine.states.values().forEach(_function_1);
  }

  public Iterable<String> errorStates(final StateMachine stateMachine) {
    final Function1<State, Boolean> _function = (State it) -> {
      return Boolean.valueOf(it.transitions.isEmpty());
    };
    final Function1<State, String> _function_1 = (State it) -> {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("A[] not ");
      _builder.append(stateMachine.name);
      _builder.append(".");
      _builder.append(it.name);
      return _builder.toString();
    };
    return IterableExtensions.<State, String>map(IterableExtensions.<State>filter(stateMachine.states.values(), _function), _function_1);
  }

  public String format(final String guard) {
    return guard.replace("&&", "and").replace(">", "&gt;").replace("<", "&lt;");
  }

  @Override
  public String toString() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("<queries>");
    _builder.newLine();
    {
      for(final String query : this.queries) {
        _builder.append("<query>");
        _builder.newLine();
        _builder.append("\t\t\t    \t");
        _builder.append("<formula>");
        _builder.append(query, "\t\t\t    \t");
        _builder.append("</formula>");
        _builder.newLineIfNotEmpty();
        _builder.append("\t\t\t    ");
        _builder.append("</query>");
        _builder.newLine();
      }
    }
    _builder.append("</queries>");
    _builder.newLine();
    return _builder.toString();
  }
}
