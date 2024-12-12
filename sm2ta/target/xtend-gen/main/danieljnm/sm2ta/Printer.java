package danieljnm.sm2ta;

import danieljnm.sm2ta.StateMachine.State;
import danieljnm.sm2ta.StateMachine.StateMachine;
import danieljnm.sm2ta.StateMachine.Transition;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.InputOutput;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ListExtensions;

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
    InputOutput.<String>println(this.prettify(state.toString(), depth));
    boolean _isEmpty = state.getNestedStates().isEmpty();
    boolean _not = (!_isEmpty);
    if (_not) {
      String _repeat = " ".repeat((depth * 2));
      String _plus = (_repeat + "Nested states:");
      InputOutput.<String>println(_plus);
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

  public String prettify(final String output, final int depth) {
    final Function1<String, String> _function = (String it) -> {
      String _repeat = " ".repeat((depth * 2));
      return (_repeat + it);
    };
    String _join = IterableExtensions.join(ListExtensions.<String, String>map(((List<String>)Conversions.doWrapArray(output.split("\n"))), _function), "\n");
    return (_join + "\n");
  }
}
