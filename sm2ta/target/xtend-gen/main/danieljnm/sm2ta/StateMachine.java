package danieljnm.sm2ta;

import java.util.HashMap;
import java.util.function.Function;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;

@SuppressWarnings("all")
public class StateMachine {
  private HashMap<String, State> states = CollectionLiterals.<String, State>newHashMap();

  public HashMap<String, State> getStates() {
    return this.states;
  }

  public State addState(final String name) {
    final Function<String, State> _function = (String it) -> {
      return new State(name);
    };
    return this.states.computeIfAbsent(name, _function);
  }

  public State addTransition(final String source, final String target, final String event) {
    State _xblockexpression = null;
    {
      State targetState = this.addState(target);
      _xblockexpression = this.addState(source).addTransition(event, TransitionType.Direct, targetState);
    }
    return _xblockexpression;
  }
}
