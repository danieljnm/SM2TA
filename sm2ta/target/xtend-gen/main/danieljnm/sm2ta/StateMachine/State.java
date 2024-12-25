package danieljnm.sm2ta.StateMachine;

import java.util.List;
import java.util.Objects;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class State {
  private StateMachine stateMachine;

  public int index;

  public State parent;

  public String name;

  public List<Transition> transitions = CollectionLiterals.<Transition>newArrayList();

  public List<State> nestedStates = CollectionLiterals.<State>newArrayList();

  public boolean isInitial;

  public boolean isNested;

  public State(final State parent, final String name) {
    this.parent = parent;
    this.name = name;
    this.isNested = true;
  }

  public State(final StateMachine stateMachine, final String name) {
    this.stateMachine = stateMachine;
    this.name = name;
    int _plusPlus = stateMachine.index++;
    this.index = _plusPlus;
  }

  public State state(final String name) {
    return this.stateMachine.state(name);
  }

  public State nestedState(final String name) {
    State _xblockexpression = null;
    {
      final Function1<State, Boolean> _function = (State it) -> {
        return Boolean.valueOf(Objects.equals(it.name, name));
      };
      final State existingState = IterableExtensions.<State>findFirst(this.nestedStates, _function);
      if ((existingState != null)) {
        return existingState;
      }
      final State nestedState = new State(this, name);
      this.nestedStates.add(nestedState);
      _xblockexpression = nestedState;
    }
    return _xblockexpression;
  }

  public State nesting(final Procedure1<? super State> context) {
    State _xblockexpression = null;
    {
      context.apply(this);
      _xblockexpression = this;
    }
    return _xblockexpression;
  }

  public State transition(final String target) {
    State _xblockexpression = null;
    {
      if (this.isNested) {
        final Function1<State, Boolean> _function = (State it) -> {
          return Boolean.valueOf((it.name == target));
        };
        State targetState = IterableExtensions.<State>findFirst(this.parent.nestedStates, _function);
        if ((targetState == null)) {
          State _state = new State(this.parent, target);
          targetState = _state;
          this.parent.nestedStates.add(targetState);
        }
        Transition _transition = new Transition(targetState);
        this.transitions.add(_transition);
        return this;
      }
      State targetState_1 = this.stateMachine.state(target);
      Transition _transition_1 = new Transition(targetState_1);
      this.transitions.add(_transition_1);
      _xblockexpression = this;
    }
    return _xblockexpression;
  }

  public State removeTransition(final Transition transition) {
    State _xblockexpression = null;
    {
      this.transitions.remove(transition);
      _xblockexpression = this;
    }
    return _xblockexpression;
  }

  public State guard(final String guard) {
    State _xblockexpression = null;
    {
      boolean _isEmpty = this.transitions.isEmpty();
      if (_isEmpty) {
        return this;
      }
      Transition _lastOrNull = IterableExtensions.<Transition>lastOrNull(this.transitions);
      _lastOrNull.guard = guard;
      _xblockexpression = this;
    }
    return _xblockexpression;
  }

  public State action(final String action) {
    State _xblockexpression = null;
    {
      boolean _isEmpty = this.transitions.isEmpty();
      if (_isEmpty) {
        return this;
      }
      Transition _lastOrNull = IterableExtensions.<Transition>lastOrNull(this.transitions);
      _lastOrNull.action = action;
      _xblockexpression = this;
    }
    return _xblockexpression;
  }

  public State timeout(final int timeout) {
    State _xblockexpression = null;
    {
      boolean _isEmpty = this.transitions.isEmpty();
      if (_isEmpty) {
        return this;
      }
      Transition _lastOrNull = IterableExtensions.<Transition>lastOrNull(this.transitions);
      _lastOrNull.timeout = timeout;
      _xblockexpression = this;
    }
    return _xblockexpression;
  }

  public State when(final String when) {
    State _xblockexpression = null;
    {
      boolean _isEmpty = this.transitions.isEmpty();
      if (_isEmpty) {
        return this;
      }
      Transition _lastOrNull = IterableExtensions.<Transition>lastOrNull(this.transitions);
      _lastOrNull.when = when;
      _xblockexpression = this;
    }
    return _xblockexpression;
  }

  public State signal(final String signal) {
    State _xblockexpression = null;
    {
      boolean _isEmpty = this.transitions.isEmpty();
      if (_isEmpty) {
        return this;
      }
      Transition _lastOrNull = IterableExtensions.<Transition>lastOrNull(this.transitions);
      _lastOrNull.signal = signal;
      _xblockexpression = this;
    }
    return _xblockexpression;
  }

  public State initial() {
    State _xblockexpression = null;
    {
      this.isInitial = true;
      _xblockexpression = this;
    }
    return _xblockexpression;
  }

  @Override
  public String toString() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("State: ");
    _builder.append(this.name);
    _builder.newLineIfNotEmpty();
    {
      int _length = ((Object[])Conversions.unwrapArray(this.transitions, Object.class)).length;
      boolean _greaterThan = (_length > 0);
      if (_greaterThan) {
        _builder.append("Transitions: ");
        String _join = IterableExtensions.join(this.transitions);
        _builder.append(_join);
        _builder.newLineIfNotEmpty();
      }
    }
    return _builder.toString();
  }
}
