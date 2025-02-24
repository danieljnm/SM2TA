package danieljnm.sm2ta;

import com.google.gson.Gson;
import danieljnm.sm2ta.StateMachine.State;
import danieljnm.sm2ta.StateMachine.StateMachine;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.function.Consumer;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.InputOutput;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class Translator {
  private static StateMachine stateMachine = new StateMachine();

  public static void main(final String[] args) {
    Translator.stateMachine = Translator.regular();
    Translator.translateTransitions();
  }

  public static void translateTransitions() {
    try {
      byte[] _readAllBytes = Files.readAllBytes(Paths.get("src/main/java/Data/transitions.json"));
      String json = new String(_readAllBytes);
      final Transition[] data = new Gson().<Transition[]>fromJson(json, Transition[].class);
      InputOutput.<Transition[]>println(data);
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }

  public static StateMachine regular() {
    StateMachine _xblockexpression = null;
    {
      final Procedure1<StateMachine> _function = (StateMachine it) -> {
        it.variable("error").type("bool").value("false");
        it.variable("hasControl").type("bool").value("false");
        it.variable("missionIndex").type("int").value("-1");
        it.variable("waypoints").type("int").value("10");
      };
      Translator.stateMachine.name("FOD").variables(_function).state("Idle").initial().transition("PositionAcquisition").action("error := false, hasControl := true").state("PositionAcquisition").transition("GlobalPlanning").when("Ready").transition("Idle").when("LostControl").action("hasControl := false").transition("Idle").when("Abort").action("error := true, hasControl := false").transition("Idle").when("FailedEstimation").action("hasControl := false").state("GlobalPlanning").transition("NextPosition").when("Success").action("missionIndex := 0").transition("Idle").when("LostControl").action("hasControl := false").transition("Idle").when("Abort").action("error := true, hasControl := false").state("NextPosition").transition("CaptureState").when("ContinueLoop").guard("missionIndex < waypoints").transition("MissionCompleted").when("Success").guard("missionIndex >= waypoints").transition("Idle").when("LostControl").action("hasControl := false").state("CaptureState").transition("ValidateState").when("Success").transition("Idle").when("LostControl").action("hasControl := false").state("ValidateState").transition("NextPosition").when("Success").action("missionIndex++").transition("Idle").when("LostControl").action("hasControl := false").state("MissionCompleted").transition("Idle").when("Success").transition("Idle").when("Abort").action("error := true, hasControl := false").transition("Idle").when("LostControl").action("hasControl := false");
      _xblockexpression = Translator.stateMachine;
    }
    return _xblockexpression;
  }

  public static StateMachine withNesting() {
    StateMachine _xblockexpression = null;
    {
      final Procedure1<StateMachine> _function = (StateMachine it) -> {
        it.variable("bool hasControl = false");
      };
      final Procedure1<State> _function_1 = (State it) -> {
        it.nestedState("NextPosition").transition("CaptureState").transition("MissionCompleted");
        it.nestedState("CaptureState").transition("ValidateState").transition("LostControl").signal("NestedLostControl");
        it.nestedState("ValidateState").transition("NextPosition").transition("LostControl").signal("NestedLostControl");
        it.nestedState("MissionCompleted").transition("LostControl").signal("NestedLostControl").transition("Abort").signal("NestedAbort").transition("Success").signal("NestedSuccess");
        it.nestedState("Abort");
        it.nestedState("LostControl");
        it.nestedState("Success");
      };
      Translator.stateMachine.name("FOD").variables(_function).state("Idle").initial().transition("PositionAcquisition").when("Ready").action("hasControl := true").state("PositionAcquisition").transition("GlobalPlanning").transition("Idle").when("LostControl").transition("Idle").when("Abort").transition("Idle").when("FailedEstimation").state("GlobalPlanning").nesting(_function_1).transition("Idle").when("NestedLostControl").transition("Idle").when("NestedAbort").transition("Idle").when("NestedSuccess");
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
