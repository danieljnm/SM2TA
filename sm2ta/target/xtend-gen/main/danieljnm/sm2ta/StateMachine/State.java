package danieljnm.sm2ta.StateMachine;

import java.util.List;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class State {
  private StateMachine stateMachine;

  private String name;

  private List<Transition> transitions = CollectionLiterals.<Transition>newArrayList();

  private List<State> nestedStates = CollectionLiterals.<State>newArrayList();

  private boolean isInitial;

  private boolean isNested;

  public State(final String name) {
    this.name = name;
    this.isNested = true;
  }

  public State(final StateMachine stateMachine, final String name) {
    this.stateMachine = stateMachine;
    this.name = name;
  }

  public State state(final String name) {
    return this.stateMachine.state(name);
  }

  public State nestedState(final String name) {
    State _xblockexpression = null;
    {
      final State nestedState = new State(name);
      this.nestedStates.add(nestedState);
      _xblockexpression = nestedState;
    }
    return _xblockexpression;
  }

  public State nesting(final Procedure1<? super State> configure) {
    State _xblockexpression = null;
    {
      configure.apply(this);
      _xblockexpression = this;
    }
    return _xblockexpression;
  }

  public String getName() {
    return this.name;
  }

  public String setName(final String name) {
    return this.name = name;
  }

  public List<State> getNestedStates() {
    return this.nestedStates;
  }

  public List<Transition> getTransitions() {
    return this.transitions;
  }

  public State transition(final String event) {
    State _xblockexpression = null;
    {
      State _initialState = this.stateMachine.getInitialState();
      Transition _transition = new Transition(event, _initialState);
      this.transitions.add(_transition);
      _xblockexpression = this;
    }
    return _xblockexpression;
  }

  public State transition(final String event, final String target) {
    State _xblockexpression = null;
    {
      if (this.isNested) {
        return this;
      }
      State targetState = this.stateMachine.state(target);
      Transition _transition = new Transition(event, targetState);
      this.transitions.add(_transition);
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

  public State initial() {
    State _xblockexpression = null;
    {
      this.isInitial = true;
      _xblockexpression = this;
    }
    return _xblockexpression;
  }

  public boolean getIsInitial() {
    return this.isInitial;
  }

  public boolean getIsNested() {
    return this.isNested;
  }

  public boolean setIsNested(final boolean isNested) {
    return this.isNested = isNested;
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
