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
    this.print(initialState, 0);
  }

  public void print(final State state, final int depth) {
    boolean _containsKey = this.visited.containsKey(state.getName());
    if (_containsKey) {
      return;
    }
    this.visited.put(state.getName(), state);
    String _repeat = " ".repeat((depth * 2));
    CharSequence _string = state.toString(depth);
    String _plus = (_repeat + _string);
    InputOutput.<String>println(_plus);
    boolean _isEmpty = state.getNestedStates().isEmpty();
    boolean _not = (!_isEmpty);
    if (_not) {
      String _repeat_1 = " ".repeat((depth * 2));
      String _plus_1 = (_repeat_1 + "Nested states:");
      InputOutput.<String>println(_plus_1);
      final Consumer<State> _function = (State it) -> {
        this.print(it, (depth + 1));
      };
      state.getNestedStates().forEach(_function);
    }
    final Consumer<Transition> _function_1 = (Transition it) -> {
      this.print(it.getTarget(), depth);
    };
    state.getTransitions().forEach(_function_1);
  }
}
