package danieljnm.sm2ta;

import com.google.gson.Gson;
import danieljnm.sm2ta.Model.StateDefinition;
import danieljnm.sm2ta.Model.Transition;
import danieljnm.sm2ta.Model.Variable;
import danieljnm.sm2ta.StateMachine.State;
import danieljnm.sm2ta.StateMachine.StateMachine;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.InputOutput;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class Translator {
  private static StateMachine stateMachine = new StateMachine();

  public static void main(final String[] args) {
    Translator.translate();
    InputOutput.<String>println(Translator.stateMachine.toXml());
  }

  public static void translate() {
    final StateDefinition[] states = Translator.getStates();
    final Function1<StateDefinition, Boolean> _function = (StateDefinition it) -> {
      return Boolean.valueOf((it.initial && (!it.nestedInitial)));
    };
    final StateDefinition initial = IterableExtensions.<StateDefinition>findFirst(((Iterable<StateDefinition>)Conversions.doWrapArray(states)), _function);
    Translator.stateMachine.name(initial.namespace).state(initial.stateName).initial();
    Translator.setVariables(((List<Variable>)Conversions.doWrapArray(Translator.getVariables())));
    Translator.setStates(((List<StateDefinition>)Conversions.doWrapArray(states)), initial.namespace);
  }

  public static StateDefinition[] getStates() {
    StateDefinition[] _xblockexpression = null;
    {
      final StateDefinition[] states = new Gson().<StateDefinition[]>fromJson(Translator.getJson("states"), StateDefinition[].class);
      final Consumer<StateDefinition> _function = (StateDefinition it) -> {
        it.convertName();
      };
      ((List<StateDefinition>)Conversions.doWrapArray(states)).forEach(_function);
      _xblockexpression = states;
    }
    return _xblockexpression;
  }

  public static void setStates(final List<StateDefinition> states, final String namespace) {
    final Function1<StateDefinition, Boolean> _function = (StateDefinition it) -> {
      return Boolean.valueOf(Objects.equals(it.namespace, namespace));
    };
    final Iterable<StateDefinition> topLevelStates = IterableExtensions.<StateDefinition>filter(states, _function);
    final Consumer<StateDefinition> _function_1 = (StateDefinition state) -> {
      Translator.stateMachine.state(state.stateName);
      final String nestedNamespace = state.stateName;
      final Function1<StateDefinition, Boolean> _function_2 = (StateDefinition it) -> {
        return Boolean.valueOf(Objects.equals(it.namespace, nestedNamespace));
      };
      final Function1<StateDefinition, Boolean> _function_3 = (StateDefinition it) -> {
        return Boolean.valueOf((!it.nestedInitial));
      };
      final List<StateDefinition> childStates = IterableExtensions.<StateDefinition, Boolean>sortBy(IterableExtensions.<StateDefinition>filter(states, _function_2), _function_3);
      boolean _isEmpty = childStates.isEmpty();
      boolean _not = (!_isEmpty);
      if (_not) {
        final Procedure1<State> _function_4 = (State it) -> {
          final Consumer<StateDefinition> _function_5 = (StateDefinition nested) -> {
            it.nestedState(nested.stateName);
          };
          childStates.forEach(_function_5);
        };
        Translator.stateMachine.state(state.stateName).nesting(_function_4);
      }
    };
    topLevelStates.forEach(_function_1);
  }

  public static Variable[] getVariables() {
    return new Gson().<Variable[]>fromJson(Translator.getJson("variables"), Variable[].class);
  }

  public static void setVariables(final List<Variable> variables) {
    final Consumer<Variable> _function = (Variable variableDefinition) -> {
      final Procedure1<StateMachine> _function_1 = (StateMachine it) -> {
        it.variable(variableDefinition.variable).type(variableDefinition.convertedType()).value(variableDefinition.value);
      };
      Translator.stateMachine.variables(_function_1);
    };
    variables.forEach(_function);
  }

  public static String getJson(final String file) {
    try {
      String _xblockexpression = null;
      {
        StringConcatenation _builder = new StringConcatenation();
        _builder.append("src/main/java/Data/");
        _builder.append(file);
        _builder.append(".json");
        final byte[] bytes = Files.readAllBytes(Paths.get(_builder.toString()));
        _xblockexpression = new String(bytes);
      }
      return _xblockexpression;
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }

  public static void translateTransitions() {
    try {
      byte[] _readAllBytes = Files.readAllBytes(Paths.get("src/main/java/Data/transitions.json"));
      String json = new String(_readAllBytes);
      final Transition[] transitions = new Gson().<Transition[]>fromJson(json, Transition[].class);
      Translator.reset();
      Translator.stateMachine.name("test");
      final Consumer<Transition> _function = (Transition it) -> {
        Translator.stateMachine.state(it.stateName).transition(it.target).when(it.message());
      };
      ((List<Transition>)Conversions.doWrapArray(transitions)).forEach(_function);
      InputOutput.<String>println(Translator.stateMachine.toXml());
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

  public static StateMachine withBasicNesting() {
    StateMachine _xblockexpression = null;
    {
      final Procedure1<StateMachine> _function = (StateMachine it) -> {
        it.variable("bool hasControl = false");
      };
      final Procedure1<State> _function_1 = (State it) -> {
        it.nestedState("NextPosition").transition("MissionCompleted");
        it.nestedState("MissionCompleted").transition("LostControl").signal("NestedLostControl").transition("Abort").signal("NestedAbort").transition("Success").signal("NestedSuccess");
        it.nestedState("Abort");
        it.nestedState("LostControl");
        it.nestedState("Success");
      };
      Translator.stateMachine.name("FOD").variables(_function).state("Idle").initial().transition("PositionAcquisition").when("Ready").action("hasControl := true").state("PositionAcquisition").transition("GlobalPlanning").transition("Idle").when("LostControl").state("GlobalPlanning").nesting(_function_1).transition("Idle").when("NestedLostControl").transition("Idle").when("NestedAbort").transition("Idle").when("NestedSuccess");
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
