package danieljnm.sm2ta;

import danieljnm.sm2ta.StateMachine.State;
import danieljnm.sm2ta.StateMachine.StateMachine;
import danieljnm.sm2ta.StateMachine.Transition;
import java.util.HashMap;
import java.util.function.Consumer;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.InputOutput;

@SuppressWarnings("all")
public class Printer {
  private HashMap<String, State> visited = CollectionLiterals.<String, State>newHashMap();

  public void print(final StateMachine stateMachine) {
    State initialState = stateMachine.getInitialState();
    if ((initialState == null)) {
      final Consumer<State> _function = (State it) -> {
        InputOutput.<State>println(it);
      };
      stateMachine.getStates().values().forEach(_function);
      return;
    }
    InputOutput.<State>println(initialState);
    this.visited.put(initialState.getName(), initialState);
    this.print(initialState, 0);
  }

  public void print(final State state, final int depth) {
    final Consumer<Transition> _function = (Transition it) -> {
      boolean _containsKey = this.visited.containsKey(it.getTarget().getName());
      boolean _not = (!_containsKey);
      if (_not) {
        this.visited.put(it.getTarget().getName(), it.getTarget());
        InputOutput.<State>println(it.getTarget());
        this.print(it.getTarget(), depth);
      }
    };
    state.getTransitions().forEach(_function);
  }
}
