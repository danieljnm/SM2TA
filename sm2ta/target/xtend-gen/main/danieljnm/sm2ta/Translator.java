package danieljnm.sm2ta;

import danieljnm.sm2ta.StateMachine.State;
import danieljnm.sm2ta.StateMachine.StateMachine;
import java.util.function.Consumer;
import org.eclipse.xtext.xbase.lib.InputOutput;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class Translator {
  private static StateMachine stateMachine = new StateMachine();

  public static void main(final String[] args) {
    Translator.stateMachine = Translator.regular();
    InputOutput.<String>println(Translator.stateMachine.toUppaal());
  }

  public static StateMachine regular() {
    StateMachine _xblockexpression = null;
    {
      Translator.stateMachine.name("FOD").state("Idle").initial().transition("PositionAcquisition").state("PositionAcquisition").transition("GlobalPlanning").when("Ready").transition("Idle").when("LostControl").transition("Idle").when("Abort").transition("Idle").when("FailedEstimation").state("GlobalPlanning").transition("NextPosition").when("Success").transition("Idle").when("LostControl").transition("Idle").when("Abort").state("NextPosition").transition("CaptureState").when("ContinueLoop").transition("MissionCompleted").when("Success").transition("Idle").when("LostControl").state("CaptureState").transition("ValidateState").when("Success").transition("Idle").when("LostControl").state("ValidateState").transition("NextPosition").when("Success").transition("Idle").when("LostControl").state("MissionCompleted").transition("Idle").when("Success").transition("Idle").when("Abort").transition("Idle").when("LostControl");
      _xblockexpression = Translator.stateMachine;
    }
    return _xblockexpression;
  }

  public static StateMachine withNesting() {
    StateMachine _xblockexpression = null;
    {
      final Procedure1<State> _function = (State it) -> {
        it.nestedState("NextPosition").transition("CaptureState").transition("MissionCompleted");
        it.nestedState("CaptureState").transition("ValidateState").transition("LostControl").signal("NestedLostControl");
        it.nestedState("ValidateState").transition("NextPosition").transition("LostControl").signal("NestedLostControl");
        it.nestedState("MissionCompleted").transition("LostControl").signal("NestedLostControl").transition("Abort").signal("NestedAbort").transition("Success").signal("NestedSuccess");
        it.nestedState("Abort");
        it.nestedState("LostControl");
        it.nestedState("Success");
      };
      Translator.stateMachine.name("FOD").state("Idle").initial().transition("PositionAcquisition").when("Ready").state("PositionAcquisition").transition("GlobalPlanning").transition("Idle").when("LostControl").transition("Idle").when("Abort").transition("Idle").when("FailedEstimation").state("GlobalPlanning").nesting(_function).transition("Idle").when("NestedLostControl").transition("Idle").when("NestedAbort").transition("Idle").when("NestedSuccess");
      _xblockexpression = Translator.stateMachine;
    }
    return _xblockexpression;
  }

  public static StateMachine getStateMachine() {
    return Translator.stateMachine;
  }

  public static StateMachine reset() {
    StateMachine _stateMachine = new StateMachine();
    return Translator.stateMachine = _stateMachine;
  }

  public static void printMachine() {
    final Consumer<State> _function = (State it) -> {
      InputOutput.<State>println(it);
    };
    Translator.stateMachine.states.values().forEach(_function);
  }
}
