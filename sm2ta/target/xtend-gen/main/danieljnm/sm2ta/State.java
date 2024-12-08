package danieljnm.sm2ta;

import java.util.List;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.IterableExtensions;

@SuppressWarnings("all")
public class State {
  private String name;

  private List<Transition> transitions = CollectionLiterals.<Transition>newArrayList();

  private List<State> nestedStates = CollectionLiterals.<State>newArrayList();

  public State(final String name) {
    this.name = name;
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

  public State addTransition(final String event, final TransitionType type, final State target) {
    State _xblockexpression = null;
    {
      Transition _transition = new Transition(event, type, target);
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
        String _join = IterableExtensions.join(this.transitions, ", ");
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
