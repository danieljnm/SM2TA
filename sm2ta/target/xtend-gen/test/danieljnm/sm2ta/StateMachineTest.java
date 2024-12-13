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
    int _length = ((Object[])Conversions.unwrapArray(this.stateMachine.getStates().values(), Object.class)).length;
    boolean _equals = (_length == 0);
    Assertions.assertEquals(Boolean.valueOf(true), Boolean.valueOf(_equals));
  }

  @Test
  public void oneState() {
    this.stateMachine.state("Idle");
    Assertions.assertEquals(1, ((Object[])Conversions.unwrapArray(this.stateMachine.getStates().values(), Object.class)).length);
    Assertions.assertEquals("Idle", (((State[])Conversions.unwrapArray(this.stateMachine.getStates().values(), State.class))[0]).getName());
  }

  @Test
  public void initialState() {
    this.stateMachine.state("Idle").initial();
    State initialState = this.stateMachine.getInitialState();
    Assertions.assertNotNull(initialState);
    Assertions.assertEquals("Idle", initialState.getName());
  }

  @Test
  public void transition() {
    this.stateMachine.state("Idle").initial().transition("Ready", "Planning").state("Planning");
    List<Transition> transitions = this.stateMachine.getInitialState().getTransitions();
    final List<Transition> _converted_transitions = (List<Transition>)transitions;
    Assertions.assertEquals(1, ((Object[])Conversions.unwrapArray(_converted_transitions, Object.class)).length);
    Transition transition = transitions.get(0);
    Assertions.assertEquals("Planning", transition.getTarget().getName());
    Assertions.assertEquals("Ready", transition.getEvent());
  }

  @Test
  public void stateHasGuard() {
    final String guard = "x > 1";
    this.stateMachine.state("Idle").initial().transition("Ready", "Planning").guard(guard);
    Transition transition = this.stateMachine.getInitialState().getTransitions().get(0);
    Assertions.assertNotNull(transition);
    Assertions.assertEquals(guard, transition.getGuard());
  }

  @Test
  public void stateHasAction() {
    final String action = "x = 0";
    this.stateMachine.state("Idle").initial().transition("Ready", "Planning").action(action);
    Transition transition = this.stateMachine.getInitialState().getTransitions().get(0);
    Assertions.assertNotNull(transition);
    Assertions.assertEquals(action, transition.getAction());
  }

  @Test
  public void nestedMachineStates() {
    final Procedure1<State> _function = (State it) -> {
      it.nestedState("Testing").initial().transition("Processed", "Evaluating").guard("x > 1").action("x = 0");
      it.nestedState("Evaluating").transition("Done");
    };
    this.stateMachine.state("Idle").initial().nesting(_function);
    List<State> nestedStates = this.stateMachine.getInitialState().getNestedStates();
    final List<State> _converted_nestedStates = (List<State>)nestedStates;
    Assertions.assertEquals(2, ((Object[])Conversions.unwrapArray(_converted_nestedStates, Object.class)).length);
    final Function1<State, Boolean> _function_1 = (State it) -> {
      return Boolean.valueOf(it.getIsInitial());
    };
    State initialState = IterableExtensions.<State>findFirst(nestedStates, _function_1);
    Assertions.assertNotNull(initialState);
    Assertions.assertEquals("Testing", initialState.getName());
    Assertions.assertFalse(initialState.getTransitions().isEmpty());
  }

  @Test
  public void nestedMachineTransitions() {
    final Procedure1<State> _function = (State it) -> {
      it.nestedState("Testing").initial().transition("Processed", "Evaluating").guard("x > 1").action("x = 0");
      it.nestedState("Evaluating").transition("Done");
    };
    this.stateMachine.state("Idle").initial().nesting(_function).transition("Ready", "Planning").state("Planning").transition("Done");
    List<State> nestedStates = this.stateMachine.getInitialState().getNestedStates();
    final Function1<State, Boolean> _function_1 = (State it) -> {
      return Boolean.valueOf(it.getIsInitial());
    };
    State initialState = IterableExtensions.<State>findFirst(nestedStates, _function_1);
    Transition transition = initialState.getTransitions().get(0);
    Assertions.assertEquals("Evaluating", transition.getTarget().getName());
    Assertions.assertEquals("Processed", transition.getEvent());
    Assertions.assertEquals("x > 1", transition.getGuard());
    Assertions.assertEquals("x = 0", transition.getAction());
    Printer printer = new Printer();
    printer.print(this.stateMachine);
  }
}
