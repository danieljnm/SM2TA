package danieljnm.sm2ta;

import danieljnm.sm2ta.StateMachine.State;
import danieljnm.sm2ta.StateMachine.StateMachine;
import java.util.function.Consumer;
import org.eclipse.xtext.xbase.lib.InputOutput;

@SuppressWarnings("all")
public class Translator {
  private static StateMachine stateMachine = new StateMachine();

  public static void main(final String[] args) {
    Translator.stateMachine.state("Idle").initial().transition("Ready", "Position acquisition").state("Position acquisition").transition("Systems ready", "Global planning").transition("Lost control").state("Global planning").transition("Success", "Next position").transition("Lost control").state("Next position").transition("Continue loop", "Capture state").transition("Done", "Mission completed").transition("Lost control").state("Capture state").transition("Success", "Validate state").transition("Lost control").state("Validate state").transition("Success", "Next position").transition("Lost control").state("Mission completed").transition("Success");
    Printer printer = new Printer();
    printer.print(Translator.stateMachine);
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
    Translator.stateMachine.getStates().values().forEach(_function);
  }
}
