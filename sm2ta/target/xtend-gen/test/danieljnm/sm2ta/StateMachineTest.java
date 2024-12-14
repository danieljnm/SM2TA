package danieljnm.sm2ta;

import danieljnm.sm2ta.StateMachine.State;
import danieljnm.sm2ta.StateMachine.StateMachine;
import danieljnm.sm2ta.StateMachine.Transition;
import java.util.List;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@SuppressWarnings("all")
public class StateMachineTest {
  private StateMachine stateMachine;

  @BeforeEach
  public void reset() {
    StateMachine _stateMachine = new StateMachine();
    this.stateMachine = _stateMachine;
  }

  @Test
  public void emptyMachine() {
    int _length = ((Object[])Conversions.unwrapArray(this.stateMachine.states.values(), Object.class)).length;
    boolean _equals = (_length == 0);
    Assertions.assertEquals(Boolean.valueOf(true), Boolean.valueOf(_equals));
  }

  @Test
  public void oneState() {
    this.stateMachine.state("Idle");
    Assertions.assertEquals(1, ((Object[])Conversions.unwrapArray(this.stateMachine.states.values(), Object.class)).length);
    Assertions.assertEquals("Idle", (((State[])Conversions.unwrapArray(this.stateMachine.states.values(), State.class))[0]).name);
  }

  @Test
  public void initialState() {
    this.stateMachine.state("Idle").initial();
    State initialState = this.stateMachine.getInitialState();
    Assertions.assertNotNull(initialState);
    Assertions.assertEquals("Idle", initialState.name);
  }

  @Test
  public void transition() {
    this.stateMachine.state("Idle").initial().transition("Ready", "Planning").state("Planning");
    List<Transition> transitions = this.stateMachine.getInitialState().transitions;
    final List<Transition> _converted_transitions = (List<Transition>)transitions;
    Assertions.assertEquals(1, ((Object[])Conversions.unwrapArray(_converted_transitions, Object.class)).length);
    Transition transition = transitions.get(0);
    Assertions.assertEquals("Planning", transition.target.name);
    Assertions.assertEquals("Ready", transition.event);
  }

  @Test
  public void transitionHasGuard() {
    final String guard = "x > 1";
    this.stateMachine.state("Idle").initial().transition("Ready", "Planning").guard(guard);
    Transition transition = this.stateMachine.getInitialState().transitions.get(0);
    Assertions.assertNotNull(transition);
    Assertions.assertEquals(guard, transition.guard);
  }

  @Test
  public void transitionHasAction() {
    final String action = "x = 0";
    this.stateMachine.state("Idle").initial().transition("Ready", "Planning").action(action);
    Transition transition = this.stateMachine.getInitialState().transitions.get(0);
    Assertions.assertNotNull(transition);
    Assertions.assertEquals(action, transition.action);
  }

  @Test
  public void transitionHasTimeout() {
    final String timeout = "500";
    this.stateMachine.state("Idle").initial().transition("Ready", "Planning").timeout(timeout);
    Transition transition = this.stateMachine.getInitialState().transitions.get(0);
    Assertions.assertNotNull(transition);
    Assertions.assertEquals(timeout, transition.timeout);
  }

  @Test
  public void nestedMachineStates() {
    final Procedure1<State> _function = (State it) -> {
      it.nestedState("Testing").initial().transition("Processed", "Evaluating").guard("x > 1").action("x = 0");
      it.nestedState("Evaluating").transition("Done");
    };
    this.stateMachine.state("Idle").initial().nesting(_function);
    List<State> nestedStates = this.stateMachine.getInitialState().nestedStates;
    final List<State> _converted_nestedStates = (List<State>)nestedStates;
    Assertions.assertEquals(2, ((Object[])Conversions.unwrapArray(_converted_nestedStates, Object.class)).length);
    final Function1<State, Boolean> _function_1 = (State it) -> {
      return Boolean.valueOf(it.isInitial);
    };
    State initialState = IterableExtensions.<State>findFirst(nestedStates, _function_1);
    Assertions.assertNotNull(initialState);
    Assertions.assertEquals("Testing", initialState.name);
    Assertions.assertFalse(initialState.transitions.isEmpty());
  }

  @Test
  public void nestedMachineTransitions() {
    final Procedure1<State> _function = (State it) -> {
      it.nestedState("Testing").initial().transition("Processed", "Evaluating").guard("x > 1").action("x = 0");
      it.nestedState("Evaluating").transition("Done");
    };
    this.stateMachine.state("Idle").initial().nesting(_function).transition("Ready", "Planning").state("Planning").transition("Done");
    List<State> nestedStates = this.stateMachine.getInitialState().nestedStates;
    final Function1<State, Boolean> _function_1 = (State it) -> {
      return Boolean.valueOf(it.isInitial);
    };
    State initialState = IterableExtensions.<State>findFirst(nestedStates, _function_1);
    Transition transition = initialState.transitions.get(0);
    Assertions.assertEquals("Evaluating", transition.target.name);
    Assertions.assertEquals("Processed", transition.event);
    Assertions.assertEquals("x > 1", transition.guard);
    Assertions.assertEquals("x = 0", transition.action);
    Printer printer = new Printer();
    printer.print(this.stateMachine);
  }
}
