package Uppaal;

import com.google.common.collect.Iterables;
import danieljnm.sm2ta.StateMachine.State;
import danieljnm.sm2ta.StateMachine.Transition;
import java.util.Collections;
import java.util.List;
import java.util.Set;
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

  public String stateNames() {
    final Function1<State, List<String>> _function = (State it) -> {
      List<String> _xifexpression = null;
      boolean _isEmpty = it.nestedStates.isEmpty();
      if (_isEmpty) {
        _xifexpression = Collections.<String>unmodifiableList(CollectionLiterals.<String>newArrayList(it.name));
      } else {
        StringConcatenation _builder = new StringConcatenation();
        _builder.append("gen_pre_");
        _builder.append(it.name);
        _xifexpression = Collections.<String>unmodifiableList(CollectionLiterals.<String>newArrayList(_builder.toString(), it.name));
      }
      return _xifexpression;
    };
    return IterableExtensions.join(IterableExtensions.<String>toSet(IterableExtensions.<State, String>flatMap(this.states, _function)), ",\n");
  }

  public Set<String> nestedStateNames() {
    final Function1<State, Boolean> _function = (State it) -> {
      boolean _isEmpty = it.nestedStates.isEmpty();
      return Boolean.valueOf((!_isEmpty));
    };
    final Function1<State, String> _function_1 = (State it) -> {
      return it.name;
    };
    return IterableExtensions.<String>toSet(IterableExtensions.<State, String>map(IterableExtensions.<State>filter(this.states, _function), _function_1));
  }

  public String transitions() {
    Iterable<String> _actualTransitions = this.actualTransitions();
    Iterable<String> _nestedStateTransitions = this.nestedStateTransitions();
    return IterableExtensions.join(Iterables.<String>concat(_actualTransitions, _nestedStateTransitions), ",\n");
  }

  public Iterable<String> actualTransitions() {
    final Function1<State, List<String>> _function = (State state) -> {
      final Function1<Transition, String> _function_1 = (Transition transition) -> {
        StringConcatenation _builder = new StringConcatenation();
        _builder.append(state.name);
        _builder.append(" -> ");
        CharSequence _targetName = transition.targetName(state.isNested);
        _builder.append(_targetName);
        _builder.append(" {");
        _builder.newLineIfNotEmpty();
        {
          if ((transition.when != null)) {
            _builder.append("\t");
            _builder.append("sync ");
            _builder.append(transition.when, "\t");
            _builder.append("?;");
            _builder.newLineIfNotEmpty();
          }
        }
        _builder.append("}");
        return _builder.toString();
      };
      return ListExtensions.<Transition, String>map(state.transitions, _function_1);
    };
    return IterableExtensions.<State, String>flatMap(this.states, _function);
  }

  public Iterable<String> nestedStateTransitions() {
    final Function1<String, String> _function = (String it) -> {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("gen_pre_");
      _builder.append(it);
      _builder.append(" -> ");
      _builder.append(it);
      _builder.append(" {");
      _builder.newLineIfNotEmpty();
      _builder.append("\t");
      _builder.append("sync gen_");
      _builder.append(it, "\t");
      _builder.append("_inner_start!;");
      _builder.newLineIfNotEmpty();
      _builder.append("}");
      return _builder.toString();
    };
    return IterableExtensions.<String, String>map(this.nestedStateNames(), _function);
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
        String _stateNames = this.stateNames();
        _builder.append(_stateNames, "\t\t");
        _builder.append(";");
        _builder.newLineIfNotEmpty();
        {
          boolean _isEmpty_1 = this.nestedStateNames().isEmpty();
          boolean _not_1 = (!_isEmpty_1);
          if (_not_1) {
            _builder.append("\t");
            _builder.append("commit ");
            final Function1<String, String> _function = (String it) -> {
              StringConcatenation _builder_1 = new StringConcatenation();
              _builder_1.append("gen_pre_");
              _builder_1.append(it);
              return _builder_1.toString();
            };
            String _join = IterableExtensions.join(IterableExtensions.<String, String>map(this.nestedStateNames(), _function), ", ");
            _builder.append(_join, "\t");
            _builder.append(";");
            _builder.newLineIfNotEmpty();
          }
        }
        _builder.append("\t");
        _builder.append("init ");
        _builder.append(this.initialState.name, "\t");
        _builder.append(";");
        _builder.newLineIfNotEmpty();
      }
    }
    {
      if ((IterableExtensions.<State>exists(this.states, ((Function1<State, Boolean>) (State it) -> {
        boolean _isEmpty_2 = it.transitions.isEmpty();
        return Boolean.valueOf((!_isEmpty_2));
      })) || (!this.nestedStateNames().isEmpty()))) {
        _builder.append("\t");
        _builder.append("trans");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t");
        String _transitions = this.transitions();
        _builder.append(_transitions, "\t\t");
        _builder.append(";");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.append("}");
    _builder.newLine();
    return _builder.toString();
  }
}
