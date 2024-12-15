package danieljnm.sm2ta.StateMachine;

import java.util.HashMap;
import java.util.List;
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
    String _generateGloablVariables = this.generateGloablVariables();
    _builder.append(_generateGloablVariables);
    _builder.newLineIfNotEmpty();
    _builder.append("process ");
    _builder.append(this.name);
    _builder.append(" {");
    _builder.newLineIfNotEmpty();
    {
      boolean _isEmpty = this.states.isEmpty();
      boolean _not = (!_isEmpty);
      if (_not) {
        _builder.append("\t");
        String _generateStates = this.generateStates();
        _builder.append(_generateStates, "\t");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        CharSequence _generateTransitions = this.generateTransitions();
        _builder.append(_generateTransitions, "\t");
        _builder.newLineIfNotEmpty();
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

  public String generateGloablVariables() {
    StringConcatenation _builder = new StringConcatenation();
    {
      final Function1<State, Boolean> _function = (State it) -> {
        final Function1<Transition, Boolean> _function_1 = (Transition it_1) -> {
          return Boolean.valueOf((it_1.timeout != null));
        };
        return Boolean.valueOf(IterableExtensions.<Transition>exists(it.transitions, _function_1));
      };
      boolean _exists = IterableExtensions.<State>exists(this.states.values(), _function);
      if (_exists) {
        _builder.append("clock gen_clock;");
        _builder.newLine();
      }
    }
    {
      final Function1<State, Boolean> _function_1 = (State it) -> {
        final Function1<Transition, Boolean> _function_2 = (Transition it_1) -> {
          return Boolean.valueOf((it_1.when != null));
        };
        return Boolean.valueOf(IterableExtensions.<Transition>exists(it.transitions, _function_2));
      };
      boolean _exists_1 = IterableExtensions.<State>exists(this.states.values(), _function_1);
      if (_exists_1) {
        _builder.append("chan ");
        final Function1<State, List<Transition>> _function_2 = (State it) -> {
          return it.transitions;
        };
        final Function1<Transition, Boolean> _function_3 = (Transition it) -> {
          return Boolean.valueOf((it.when != null));
        };
        final Function1<Transition, String> _function_4 = (Transition it) -> {
          return it.when;
        };
        String _join = IterableExtensions.join(IterableExtensions.<Transition, String>map(IterableExtensions.<Transition>filter(IterableExtensions.<State, Transition>flatMap(this.states.values(), _function_2), _function_3), _function_4), ", ");
        _builder.append(_join);
        _builder.append(";");
        _builder.newLineIfNotEmpty();
      }
    }
    return _builder.toString();
  }

  public String generateStates() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("state");
    _builder.newLine();
    _builder.append("\t\t");
    final Function1<State, String> _function = (State it) -> {
      return it.name;
    };
    String _join = IterableExtensions.join(IterableExtensions.<State, String>map(this.states.values(), _function), ",\n");
    _builder.append(_join, "\t\t");
    _builder.append(";");
    _builder.newLineIfNotEmpty();
    _builder.append("init ");
    _builder.append(this.getInitialState().name);
    _builder.append(";");
    _builder.newLineIfNotEmpty();
    return _builder.toString();
  }

  public CharSequence generateTransitions() {
    StringConcatenation _builder = new StringConcatenation();
    {
      boolean _hasTransitions = this.hasTransitions();
      if (_hasTransitions) {
        _builder.append("trans");
        _builder.newLine();
        _builder.append("\t\t");
        final Function1<State, Boolean> _function = (State it) -> {
          return Boolean.valueOf(it.nestedStates.isEmpty());
        };
        final Function1<State, String> _function_1 = (State state) -> {
          final Function1<Transition, String> _function_2 = (Transition it) -> {
            StringConcatenation _builder_1 = new StringConcatenation();
            _builder_1.append(state.name);
            _builder_1.append(" -> ");
            _builder_1.append(it.target.name);
            _builder_1.append(" {");
            _builder_1.newLineIfNotEmpty();
            _builder_1.append("\t");
            {
              if ((it.guard != null)) {
                _builder_1.append("[");
                _builder_1.append(it.guard, "\t");
                _builder_1.append("]");
              }
            }
            _builder_1.newLineIfNotEmpty();
            _builder_1.append("\t");
            {
              if ((it.timeout != null)) {
                _builder_1.append("timeout = ");
                _builder_1.append(it.timeout, "\t");
                _builder_1.append(";");
              }
            }
            _builder_1.newLineIfNotEmpty();
            _builder_1.append("\t");
            {
              if ((it.action != null)) {
                _builder_1.append("assign { ");
                _builder_1.append(it.action, "\t");
                _builder_1.append(" }");
              }
            }
            _builder_1.newLineIfNotEmpty();
            _builder_1.append("\t");
            {
              if ((it.when != null)) {
                _builder_1.append("sync ");
                _builder_1.append(it.when, "\t");
                _builder_1.append("?");
              }
            }
            _builder_1.newLineIfNotEmpty();
            _builder_1.append("};");
            _builder_1.newLine();
            return _builder_1.toString();
          };
          return IterableExtensions.join(ListExtensions.<Transition, String>map(state.transitions, _function_2), "\n");
        };
        String _join = IterableExtensions.join(IterableExtensions.<State, String>map(IterableExtensions.<State>filter(this.states.values(), _function), _function_1), "");
        _builder.append(_join, "\t\t");
        _builder.newLineIfNotEmpty();
      }
    }
    return _builder;
  }
}
