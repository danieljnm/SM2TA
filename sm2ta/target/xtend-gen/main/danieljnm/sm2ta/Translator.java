package danieljnm.sm2ta;

import com.google.gson.Gson;
import danieljnm.sm2ta.Model.ClientBehaviour;
import danieljnm.sm2ta.Model.Condition;
import danieljnm.sm2ta.Model.Function;
import danieljnm.sm2ta.Model.StateDefinition;
import danieljnm.sm2ta.Model.StateReactor;
import danieljnm.sm2ta.Model.Transition;
import danieljnm.sm2ta.Model.Variable;
import danieljnm.sm2ta.StateMachine.State;
import danieljnm.sm2ta.StateMachine.StateMachine;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.InputOutput;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ListExtensions;
import org.eclipse.xtext.xbase.lib.MapExtensions;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;
import org.eclipse.xtext.xbase.lib.StringExtensions;

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
    final Function1<Transition, String> _function = (Transition it) -> {
      return it.stateName;
    };
    final Map<String, List<Transition>> transitions = IterableExtensions.<String, Transition>groupBy(((Iterable<? extends Transition>)Conversions.doWrapArray(Translator.getTransitions())), _function);
    final StateReactor[] reactors = Translator.getReactors();
    final Function1<ClientBehaviour, String> _function_1 = (ClientBehaviour it) -> {
      return it.name;
    };
    final Function1<List<ClientBehaviour>, List<ClientBehaviour>> _function_2 = (List<ClientBehaviour> it) -> {
      return IterableExtensions.<ClientBehaviour>toList(it);
    };
    final Map<String, List<ClientBehaviour>> behaviours = MapExtensions.<String, List<ClientBehaviour>, List<ClientBehaviour>>mapValues(IterableExtensions.<String, ClientBehaviour>groupBy(((Iterable<? extends ClientBehaviour>)Conversions.doWrapArray(Translator.getBehaviours())), _function_1), _function_2);
    final Function[] functions = Translator.getFunctions();
    final Function1<StateDefinition, Boolean> _function_3 = (StateDefinition it) -> {
      return Boolean.valueOf(Objects.equals(it.namespace, namespace));
    };
    final Iterable<StateDefinition> topLevelStates = IterableExtensions.<StateDefinition>filter(states, _function_3);
    final Consumer<StateDefinition> _function_4 = (StateDefinition state) -> {
      Translator.stateMachine.state(state.stateName);
      final List<Transition> stateTransitions = transitions.getOrDefault(state.stateName, CollectionLiterals.<Transition>newArrayList());
      final Consumer<Transition> _function_5 = (Transition transition) -> {
        boolean _isNullOrEmpty = StringExtensions.isNullOrEmpty(state.actions);
        boolean _not = (!_isNullOrEmpty);
        if (_not) {
          final Function1<String, String> _function_6 = (String action) -> {
            final Function1<Function, Boolean> _function_7 = (Function function) -> {
              return Boolean.valueOf(Objects.equals(function.function, action));
            };
            Function _findFirst = IterableExtensions.<Function>findFirst(((Iterable<Function>)Conversions.doWrapArray(functions)), _function_7);
            String _assignment = null;
            if (_findFirst!=null) {
              _assignment=_findFirst.assignment();
            }
            return _assignment;
          };
          final Function1<String, Boolean> _function_7 = (String it) -> {
            boolean _isNullOrEmpty_1 = StringExtensions.isNullOrEmpty(it);
            return Boolean.valueOf((!_isNullOrEmpty_1));
          };
          final Consumer<String> _function_8 = (String it) -> {
            InputOutput.<String>println(it);
          };
          IterableExtensions.<String>filter(ListExtensions.<String, String>map(((List<String>)Conversions.doWrapArray(state.actions.split(","))), _function_6), _function_7).forEach(_function_8);
        }
        final ArrayList<Object> actions = CollectionLiterals.<Object>newArrayList();
        boolean _startsWith = transition.event.startsWith("EvAll");
        if (_startsWith) {
          final Function1<StateReactor, Boolean> _function_9 = (StateReactor it) -> {
            String _reactor = transition.getReactor();
            return Boolean.valueOf(Objects.equals(it.name, _reactor));
          };
          final Function1<StateReactor, Condition> _function_10 = (StateReactor it) -> {
            return new Condition(it);
          };
          final Iterable<Condition> conditions = IterableExtensions.<StateReactor, Condition>map(IterableExtensions.<StateReactor>filter(((Iterable<StateReactor>)Conversions.doWrapArray(reactors)), _function_9), _function_10);
          final Function1<Condition, Iterable<CharSequence>> _function_11 = (Condition condition) -> {
            final Function1<ClientBehaviour, Boolean> _function_12 = (ClientBehaviour it) -> {
              boolean _equals = Objects.equals(it.event, "postSuccessEvent");
              return Boolean.valueOf((condition.requiresSuccess == _equals));
            };
            final Function1<ClientBehaviour, CharSequence> _function_13 = (ClientBehaviour behaviour) -> {
              final Function1<Function, Boolean> _function_14 = (Function function) -> {
                return Boolean.valueOf(Objects.equals(function.function, behaviour.methodName));
              };
              Function _findFirst = IterableExtensions.<Function>findFirst(((Iterable<Function>)Conversions.doWrapArray(functions)), _function_14);
              CharSequence _convertedExpression = null;
              if (_findFirst!=null) {
                _convertedExpression=_findFirst.convertedExpression(condition.requiresSuccess);
              }
              return _convertedExpression;
            };
            return IterableExtensions.<ClientBehaviour, CharSequence>map(IterableExtensions.<ClientBehaviour>filter(behaviours.getOrDefault(condition.clientBehaviour, CollectionLiterals.<ClientBehaviour>newArrayList()), _function_12), _function_13);
          };
          final Iterable<CharSequence> guards = IterableExtensions.<Condition, CharSequence>flatMap(conditions, _function_11);
          Translator.stateMachine.state(state.stateName).transition(transition.target).guard(IterableExtensions.join(guards, " &amp;&amp; ")).action(IterableExtensions.join(actions, ", "));
          return;
        }
        final Function1<ClientBehaviour, Boolean> _function_12 = (ClientBehaviour it) -> {
          boolean _startsWith_1 = transition.event.startsWith("EvCbSuccess");
          boolean _equals = Objects.equals(it.event, "postSuccessEvent");
          return Boolean.valueOf((_startsWith_1 == _equals));
        };
        final Function1<ClientBehaviour, CharSequence> _function_13 = (ClientBehaviour behaviour) -> {
          final Function1<Function, Boolean> _function_14 = (Function it) -> {
            return Boolean.valueOf(Objects.equals(it.function, behaviour.methodName));
          };
          Function _findFirst = IterableExtensions.<Function>findFirst(((Iterable<Function>)Conversions.doWrapArray(functions)), _function_14);
          CharSequence _convertedExpression = null;
          if (_findFirst!=null) {
            _convertedExpression=_findFirst.convertedExpression(behaviour.inIf);
          }
          return _convertedExpression;
        };
        final Iterable<CharSequence> conditions_1 = IterableExtensions.<ClientBehaviour, CharSequence>map(IterableExtensions.<ClientBehaviour>filter(behaviours.getOrDefault(transition.getClientBehaviour(), CollectionLiterals.<ClientBehaviour>newArrayList()), _function_12), _function_13);
        Translator.stateMachine.state(state.stateName).transition(transition.target).guard(IterableExtensions.join(conditions_1, " &amp;&amp; ")).action(IterableExtensions.join(actions, ", "));
      };
      stateTransitions.forEach(_function_5);
      final String nestedNamespace = state.stateName;
      final Function1<StateDefinition, Boolean> _function_6 = (StateDefinition it) -> {
        return Boolean.valueOf(Objects.equals(it.namespace, nestedNamespace));
      };
      final Function1<StateDefinition, Boolean> _function_7 = (StateDefinition it) -> {
        return Boolean.valueOf((!it.nestedInitial));
      };
      final List<StateDefinition> childStates = IterableExtensions.<StateDefinition, Boolean>sortBy(IterableExtensions.<StateDefinition>filter(states, _function_6), _function_7);
      boolean _isEmpty = childStates.isEmpty();
      boolean _not = (!_isEmpty);
      if (_not) {
        final Procedure1<State> _function_8 = (State it) -> {
          final Consumer<StateDefinition> _function_9 = (StateDefinition nested) -> {
            final List<Transition> nestedTransitions = transitions.getOrDefault(nested.stateName, CollectionLiterals.<Transition>newArrayList());
            it.nestedState(nested.stateName);
            final Consumer<Transition> _function_10 = (Transition transition) -> {
              final Function1<StateDefinition, Boolean> _function_11 = (StateDefinition it_1) -> {
                return Boolean.valueOf(Objects.equals(it_1.stateName, transition.target));
              };
              final StateDefinition target = IterableExtensions.<StateDefinition>findFirst(states, _function_11);
              boolean _equals = Objects.equals(target.namespace, nested.namespace);
              if (_equals) {
                it.nestedState(nested.stateName).transition(transition.target);
                return;
              }
              State _nestedState = it.nestedState(nested.stateName);
              StringConcatenation _builder = new StringConcatenation();
              _builder.append(nested.namespace);
              String _message = transition.message();
              _builder.append(_message);
              _nestedState.transition(_builder.toString()).signal(transition.message());
              StringConcatenation _builder_1 = new StringConcatenation();
              _builder_1.append(nested.namespace);
              String _message_1 = transition.message();
              _builder_1.append(_message_1);
              it.nestedState(_builder_1.toString()).committed();
              Translator.stateMachine.state(nested.namespace).transition(transition.target).when(transition.message());
            };
            nestedTransitions.forEach(_function_10);
          };
          childStates.forEach(_function_9);
        };
        Translator.stateMachine.state(state.stateName).nesting(_function_8);
      }
    };
    topLevelStates.forEach(_function_4);
  }

  public static Variable[] getVariables() {
    return new Gson().<Variable[]>fromJson(Translator.getJson("variables"), Variable[].class);
  }

  public static void setVariables(final List<Variable> variables) {
    final Consumer<Variable> _function = (Variable variableDefinition) -> {
      final Procedure1<StateMachine> _function_1 = (StateMachine it) -> {
        it.variable(variableDefinition.variable).type(variableDefinition.convertedType()).value(variableDefinition.initializedValue());
      };
      Translator.stateMachine.variables(_function_1);
    };
    variables.forEach(_function);
  }

  public static StateReactor[] getReactors() {
    return new Gson().<StateReactor[]>fromJson(Translator.getJson("state_reactors"), StateReactor[].class);
  }

  public static ClientBehaviour[] getBehaviours() {
    return new Gson().<ClientBehaviour[]>fromJson(Translator.getJson("client_behaviours"), ClientBehaviour[].class);
  }

  public static Function[] getFunctions() {
    return new Gson().<Function[]>fromJson(Translator.getJson("functions"), Function[].class);
  }

  public static Transition[] getTransitions() {
    Transition[] _xblockexpression = null;
    {
      Transition[] transitions = new Gson().<Transition[]>fromJson(Translator.getJson("transitions"), Transition[].class);
      final Transition[] _converted_transitions = (Transition[])transitions;
      final Consumer<Transition> _function = (Transition it) -> {
        it.convert();
      };
      ((List<Transition>)Conversions.doWrapArray(_converted_transitions)).forEach(_function);
      _xblockexpression = transitions;
    }
    return _xblockexpression;
  }

  public static StateMachine setTransitions(final List<Transition> transitions) {
    StateMachine _xblockexpression = null;
    {
      final Function1<StateReactor, String> _function = (StateReactor it) -> {
        return it.stateName;
      };
      final Function1<List<StateReactor>, Map<String, List<StateReactor>>> _function_1 = (List<StateReactor> it) -> {
        final Function1<StateReactor, String> _function_2 = (StateReactor it_1) -> {
          return it_1.name;
        };
        final Function1<List<StateReactor>, List<StateReactor>> _function_3 = (List<StateReactor> it_1) -> {
          return IterableExtensions.<StateReactor>toList(it_1);
        };
        return MapExtensions.<String, List<StateReactor>, List<StateReactor>>mapValues(IterableExtensions.<String, StateReactor>groupBy(it, _function_2), _function_3);
      };
      final Map<String, Map<String, List<StateReactor>>> reactorMap = MapExtensions.<String, List<StateReactor>, Map<String, List<StateReactor>>>mapValues(IterableExtensions.<String, StateReactor>groupBy(((Iterable<? extends StateReactor>)Conversions.doWrapArray(Translator.getReactors())), _function), _function_1);
      final Function1<ClientBehaviour, String> _function_2 = (ClientBehaviour it) -> {
        return it.name;
      };
      final Function1<List<ClientBehaviour>, List<ClientBehaviour>> _function_3 = (List<ClientBehaviour> it) -> {
        return IterableExtensions.<ClientBehaviour>toList(it);
      };
      final Map<String, List<ClientBehaviour>> behaviourMap = MapExtensions.<String, List<ClientBehaviour>, List<ClientBehaviour>>mapValues(IterableExtensions.<String, ClientBehaviour>groupBy(((Iterable<? extends ClientBehaviour>)Conversions.doWrapArray(Translator.getBehaviours())), _function_2), _function_3);
      final Function1<Transition, String> _function_4 = (Transition it) -> {
        return it.stateName;
      };
      final Map<String, List<Transition>> transitionMap = IterableExtensions.<String, Transition>groupBy(transitions, _function_4);
      final Procedure1<StateMachine> _function_5 = (StateMachine it) -> {
        final Consumer<Transition> _function_6 = (Transition it_1) -> {
          Translator.stateMachine.state(it_1.stateName).transition(it_1.target);
        };
        transitions.forEach(_function_6);
      };
      _xblockexpression = ObjectExtensions.<StateMachine>operator_doubleArrow(
        Translator.stateMachine, _function_5);
    }
    return _xblockexpression;
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
