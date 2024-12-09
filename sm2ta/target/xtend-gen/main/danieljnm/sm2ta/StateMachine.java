package danieljnm.sm2ta;

import java.util.HashMap;
import java.util.function.Function;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;

@SuppressWarnings("all")
public class StateMachine {
  private HashMap<String, State> states = CollectionLiterals.<String, State>newHashMap();

  public HashMap<String, State> getStates() {
    return this.states;
  }

  public State getInitialState() {
    final Function1<State, Boolean> _function = (State it) -> {
      return Boolean.valueOf(it.getIsInitial());
    };
    return IterableExtensions.<State>findFirst(this.states.values(), _function);
  }

  public State state(final String name) {
    final Function<String, State> _function = (String it) -> {
      return new State(this, name);
    };
    return this.states.computeIfAbsent(name, _function);
  }
}
