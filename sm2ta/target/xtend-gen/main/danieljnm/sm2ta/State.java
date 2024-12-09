package danieljnm.sm2ta;

import java.util.List;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.IterableExtensions;

@SuppressWarnings("all")
public class State {
  private StateMachine stateMachine;

  private String name;

  private List<Transition> transitions = CollectionLiterals.<Transition>newArrayList();

  private List<State> nestedStates = CollectionLiterals.<State>newArrayList();

  private boolean isInitial;

  public State(final StateMachine stateMachine, final String name) {
    this.stateMachine = stateMachine;
    this.name = name;
  }

  public State state(final String name) {
    return this.stateMachine.state(name);
  }

  public String getName() {
    return this.name;
  }

  public String setName(final String name) {
    return this.name = name;
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
    {
      int _length_1 = ((Object[])Conversions.unwrapArray(this.nestedStates, Object.class)).length;
      boolean _greaterThan_1 = (_length_1 > 0);
      if (_greaterThan_1) {
        _builder.append("Nedsted states: ");
        String _join_1 = IterableExtensions.join(this.nestedStates, ", ");
        _builder.append(_join_1);
        _builder.newLineIfNotEmpty();
      }
    }
    return _builder.toString();
  }
}
