package danieljnm.sm2ta;

import danieljnm.sm2ta.StateMachine.State;
import danieljnm.sm2ta.StateMachine.StateMachine;
import java.util.function.Consumer;
import org.eclipse.xtext.xbase.lib.InputOutput;

@SuppressWarnings("all")
public class Translator {
  private static StateMachine stateMachine = new StateMachine();

  public static void main(final String[] args) {
    Translator.stateMachine.name("FOD").state("Idle").initial().transition("PositionAcquisition").state("PositionAcquisition").transition("GlobalPlanning").transition("LostControl").state("GlobalPlanning").transition("NextPosition").transition("LostControl").state("NextPosition").transition("CaptureState").transition("MissionCompleted").transition("LostControl").state("CaptureState").transition("ValidateState").transition("LostControl").state("ValidateState").transition("NextPosition").transition("LostControl").state("MissionCompleted").transition("Success");
    Printer printer = new Printer();
    printer.print(Translator.stateMachine);
    InputOutput.<String>println(Translator.stateMachine.toUppaal());
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
