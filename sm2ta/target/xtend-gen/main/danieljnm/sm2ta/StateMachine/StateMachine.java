package danieljnm.sm2ta.StateMachine;

import java.util.HashMap;
import java.util.function.Function;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ListExtensions;

@SuppressWarnings("all")
public class StateMachine {
  public String name;

  public HashMap<String, State> states = CollectionLiterals.<String, State>newHashMap();

  public StateMachine name(final String name) {
    StateMachine _xblockexpression = null;
    {
      this.name = name;
      _xblockexpression = this;
    }
    return _xblockexpression;
  }

  public State getInitialState() {
    final Function1<State, Boolean> _function = (State it) -> {
      return Boolean.valueOf(it.isInitial);
    };
    return IterableExtensions.<State>findFirst(this.states.values(), _function);
  }

  public State state(final String name) {
    final Function<String, State> _function = (String it) -> {
      return new State(this, name);
    };
    return this.states.computeIfAbsent(name, _function);
  }

  public boolean hasTransitions() {
    boolean _xblockexpression = false;
    {
      boolean _isEmpty = this.states.isEmpty();
      if (_isEmpty) {
        return false;
      }
      final Function1<State, Boolean> _function = (State it) -> {
        boolean _isEmpty_1 = it.transitions.isEmpty();
        return Boolean.valueOf((!_isEmpty_1));
      };
      State _findFirst = IterableExtensions.<State>findFirst(this.states.values(), _function);
      _xblockexpression = (_findFirst != null);
    }
    return _xblockexpression;
  }

  public String toUppaal() {
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
        _builder.append("\t\t");
        final Function1<State, String> _function = (State it) -> {
          return it.name;
        };
        String _join = IterableExtensions.join(IterableExtensions.<State, String>map(this.states.values(), _function), ",\n");
        _builder.append(_join, "\t\t\t");
        _builder.append(";");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        _builder.append("init ");
        _builder.append(this.getInitialState().name, "\t");
        _builder.append(";");
        _builder.newLineIfNotEmpty();
        {
          boolean _hasTransitions = this.hasTransitions();
          if (_hasTransitions) {
            _builder.append("\t");
            _builder.append("trans");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("\t\t");
            final Function1<State, String> _function_1 = (State state) -> {
              final Function1<Transition, String> _function_2 = (Transition it) -> {
                StringConcatenation _builder_1 = new StringConcatenation();
                _builder_1.append(state.name);
                _builder_1.append(" -> ");
                _builder_1.append(it.target.name);
                _builder_1.append(" {");
                _builder_1.newLineIfNotEmpty();
                _builder_1.append("};");
                _builder_1.newLine();
                return _builder_1.toString();
              };
              return IterableExtensions.join(ListExtensions.<Transition, String>map(state.transitions, _function_2), "\n");
            };
            String _join_1 = IterableExtensions.join(IterableExtensions.<State, String>map(this.states.values(), _function_1), "");
            _builder.append(_join_1, "\t\t\t");
            _builder.newLineIfNotEmpty();
          }
        }
      }
    }
    _builder.append("}");
    _builder.newLine();
    _builder.append("system ");
    _builder.append(this.name);
    _builder.append(";");
    _builder.newLineIfNotEmpty();
    return _builder.toString();
  }
}
