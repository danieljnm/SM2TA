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
  public void emptyMachineTest() {
    int _length = ((Object[])Conversions.unwrapArray(this.stateMachine.getStates().values(), Object.class)).length;
    boolean _equals = (_length == 0);
    Assertions.assertEquals(Boolean.valueOf(true), Boolean.valueOf(_equals));
  }

  @Test
  public void oneStateTest() {
    this.stateMachine.state("Idle");
    Assertions.assertEquals(1, ((Object[])Conversions.unwrapArray(this.stateMachine.getStates().values(), Object.class)).length);
    State _get = this.stateMachine.getStates().get("Idle");
    boolean _tripleNotEquals = (_get != null);
    Assertions.assertEquals(Boolean.valueOf(true), Boolean.valueOf(_tripleNotEquals));
  }

  @Test
  public void initialStateTest() {
    this.stateMachine.state("Idle").initial();
    State initialState = this.stateMachine.getInitialState();
    Assertions.assertNotNull(initialState);
    Assertions.assertEquals("Idle", initialState.getName());
  }

  @Test
  public void basicMachineWithOneTransitionTest() {
    this.stateMachine.state("Idle").initial().transition("Ready", "Planning").state("Planning");
    List<Transition> transitions = this.stateMachine.state("Idle").getTransitions();
    final List<Transition> _converted_transitions = (List<Transition>)transitions;
    Assertions.assertEquals(1, ((Object[])Conversions.unwrapArray(_converted_transitions, Object.class)).length);
    Transition transition = transitions.get(0);
    Assertions.assertEquals("Planning", transition.getTarget().getName());
    Assertions.assertEquals("Ready", transition.getEvent());
  }

  @Test
  public void stateHasGuardTest() {
    final String guard = "x > 1";
    this.stateMachine.state("Idle").initial().transition("Ready", "Planning").guard(guard);
    final Function1<Transition, Boolean> _function = (Transition it) -> {
      String _event = it.getEvent();
      return Boolean.valueOf((_event == "Ready"));
    };
    Transition transition = IterableExtensions.<Transition>findFirst(this.stateMachine.getInitialState().getTransitions(), _function);
    Assertions.assertNotNull(transition);
    Assertions.assertEquals(guard, transition.getGuard());
  }

  @Test
  public void stateHasActionTest() {
    final String action = "x = 0";
    this.stateMachine.state("Idle").initial().transition("Ready", "Planning").action(action);
    final Function1<Transition, Boolean> _function = (Transition it) -> {
      String _event = it.getEvent();
      return Boolean.valueOf((_event == "Ready"));
    };
    Transition transition = IterableExtensions.<Transition>findFirst(this.stateMachine.getInitialState().getTransitions(), _function);
    Assertions.assertNotNull(transition);
    Assertions.assertEquals(action, transition.getAction());
  }

  @Test
  public void nestedMachineTest() {
    final Procedure1<State> _function = (State it) -> {
      it.nestedState("Testing").initial().transition("Processed", "Evaluating");
      it.nestedState("Evaluating").transition("Done").guard("x > 1").action("x = 0");
    };
    this.stateMachine.state("Idle").initial().nesting(_function).transition("Ready", "Planning").state("Planning").transition("Done").action("x = 0");
    Printer printer = new Printer();
    printer.print(this.stateMachine);
    List<State> nestedStates = this.stateMachine.getInitialState().getNestedStates();
    Assertions.assertEquals(1, 1);
  }

  @Test
  public void simpleMachineTest() {
    this.stateMachine.state("Idle").initial().transition("Ready", "Position acquisition").state("Position acquisition").transition("Systems ready", "Global planning").transition("Lost control").state("Global planning").transition("Success", "Next position").transition("Lost control").state("Next position").transition("Continue loop", "Capture state").transition("Done", "Mission completed").transition("Lost control").state("Capture state").transition("Success", "Validate state").transition("Lost control").state("Validate state").transition("Success", "Next position").transition("Lost control").state("Mission completed").transition("Success");
    Printer printer = new Printer();
    printer.print(this.stateMachine);
  }
}
