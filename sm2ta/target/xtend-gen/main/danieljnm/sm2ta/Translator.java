package danieljnm.sm2ta;

import java.util.function.Consumer;
import org.eclipse.xtext.xbase.lib.InputOutput;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class Translator {
  private static StateMachine stateMachine = new StateMachine();

  public static void main(final String[] args) {
  }

  public static void buildStateMachine() {
    StateMachine _stateMachine = new StateMachine();
    final Procedure1<StateMachine> _function = (StateMachine it) -> {
      it.addState("Idle");
      it.addTransition("Idle", "Planning", "Ready");
      it.addTransition("Planning", "Idle", "Lost control");
      it.addTransition("Planning", "Next position", "Success");
      it.addTransition("Next position", "Idle", "Lost control");
    };
    ObjectExtensions.<StateMachine>operator_doubleArrow(_stateMachine, _function);
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

  public static State addState(final String name) {
    return Translator.stateMachine.addState(name);
  }

  public static State addTransition(final String source, final String target, final String event) {
    return Translator.stateMachine.addTransition(source, target, event);
  }
}
