package Uppaal;

import danieljnm.sm2ta.StateMachine.State;
import danieljnm.sm2ta.StateMachine.Transition;
import java.util.List;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ListExtensions;

@SuppressWarnings("all")
public class Process {
  public String name;

  public List<State> states = CollectionLiterals.<State>newArrayList();

  private State initialState;

  public Process(final String name) {
    this.name = name;
  }

  public boolean addState(final State state) {
    boolean _xblockexpression = false;
    {
      if (state.isInitial) {
        this.initialState = state;
      }
      _xblockexpression = this.states.add(state);
    }
    return _xblockexpression;
  }

  public State getInitialState() {
    State _xblockexpression = null;
    {
      if ((this.initialState == null)) {
        final Function1<State, Boolean> _function = (State it) -> {
          return null;
        };
        return IterableExtensions.<State>findFirst(this.states, _function);
      }
      _xblockexpression = this.initialState;
    }
    return _xblockexpression;
  }

  @Override
  public String toString() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("process ");
    _builder.append(this.name);
    _builder.append(" {");
    _builder.newLineIfNotEmpty();
    {
      boolean _isEmpty = this.states.isEmpty();
      boolean _not = (!_isEmpty);
      if (_not) {
        _builder.append("\t");
        _builder.append("state");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t");
        final Function1<State, String> _function = (State it) -> {
          return it.name;
        };
        String _join = IterableExtensions.join(ListExtensions.<State, String>map(this.states, _function), ",\n");
        _builder.append(_join, "\t\t");
        _builder.append(";");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        _builder.append("init ");
        _builder.append(this.initialState.name, "\t");
        _builder.append(";");
        _builder.newLineIfNotEmpty();
      }
    }
    {
      final Function1<State, Boolean> _function_1 = (State it) -> {
        boolean _isEmpty_1 = it.transitions.isEmpty();
        return Boolean.valueOf((!_isEmpty_1));
      };
      boolean _exists = IterableExtensions.<State>exists(this.states, _function_1);
      if (_exists) {
        _builder.append("\t");
        _builder.append("trans");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t");
        final Function1<State, List<String>> _function_2 = (State it) -> {
          final Function1<Transition, String> _function_3 = (Transition transition) -> {
            StringConcatenation _builder_1 = new StringConcatenation();
            _builder_1.append(it.name);
            _builder_1.append(" -> ");
            _builder_1.append(transition.target.name);
            _builder_1.append(" {");
            _builder_1.newLineIfNotEmpty();
            {
              if ((transition.when != null)) {
                _builder_1.append("\t");
                _builder_1.append("sync ");
                _builder_1.append(transition.when, "\t");
                _builder_1.append("?;");
                _builder_1.newLineIfNotEmpty();
              }
            }
            _builder_1.append("};");
            _builder_1.newLine();
            return _builder_1.toString();
          };
          return ListExtensions.<Transition, String>map(it.transitions, _function_3);
        };
        String _join_1 = IterableExtensions.join(IterableExtensions.<State, String>flatMap(this.states, _function_2), "\n");
        _builder.append(_join_1, "\t\t");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.append("}");
    _builder.newLine();
    return _builder.toString();
  }
}
