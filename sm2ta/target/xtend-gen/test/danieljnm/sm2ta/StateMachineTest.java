package danieljnm.sm2ta;

import java.util.List;
import java.util.function.Consumer;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.InputOutput;
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
  public void basicMachineWithOneTransition() {
    this.stateMachine.state("Idle").initial().transition("Ready", "Planning").state("Planning");
    List<Transition> transitions = this.stateMachine.state("Idle").getTransitions();
    final List<Transition> _converted_transitions = (List<Transition>)transitions;
    Assertions.assertEquals(1, ((Object[])Conversions.unwrapArray(_converted_transitions, Object.class)).length);
    Transition transition = transitions.get(0);
    Assertions.assertEquals("Planning", transition.getTarget().getName());
    Assertions.assertEquals("Ready", transition.getEvent());
  }

  @Test
  public void simpleMachineTest() {
    this.stateMachine.state("Idle").initial().transition("Ready", "Position acquisition").state("Position acquisition").transition("Systems ready", "Global planning").transition("Lost control").state("Global planning").transition("Success", "Next position").transition("Lost control").state("Next position").transition("Continue loop", "Capture state").transition("Done", "Mission completed").transition("Lost control").state("Capture state").transition("Success", "Validate state").transition("Lost control").state("Validate state").transition("Success", "Next position").transition("Lost control").state("Mission completed").transition("Success");
    final Consumer<State> _function = (State it) -> {
      InputOutput.<State>println(it);
    };
    this.stateMachine.getStates().values().forEach(_function);
  }
}
