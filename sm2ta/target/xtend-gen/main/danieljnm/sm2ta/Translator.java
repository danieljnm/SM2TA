package danieljnm.sm2ta;

import com.google.common.collect.Iterables;
import com.google.gson.Gson;
import danieljnm.sm2ta.Dto.ClientBehaviourDto;
import danieljnm.sm2ta.Dto.ConditionDto;
import danieljnm.sm2ta.Dto.FunctionDto;
import danieljnm.sm2ta.Dto.StateDto;
import danieljnm.sm2ta.Dto.StateReactorDto;
import danieljnm.sm2ta.Dto.TransitionDto;
import danieljnm.sm2ta.Dto.VariableDto;
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
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;
import org.eclipse.xtext.xbase.lib.StringExtensions;

@SuppressWarnings("all")
public class Translator {
  private static StateMachine stateMachine = new StateMachine();

  public static void main(final String[] args) {
    Translator.translate();
    InputOutput.<String>println(Translator.stateMachine.toXml());
  }

  public static State process(final StateDto state) {
    return Translator.stateMachine.state(state.stateName);
  }

  public static void translate() {
    final Function1<StateDto, Boolean> _function = (StateDto it) -> {
      return Boolean.valueOf((it.initial && (!it.nestedInitial)));
    };
    final StateDto initial = IterableExtensions.<StateDto>findFirst(((Iterable<StateDto>)Conversions.doWrapArray(Translator.getStates())), _function);
    Translator.stateMachine.name(initial.namespace).state(initial.stateName).initial();
    Translator.setVariables(Translator.getVariables());
    Translator.setStates(initial.namespace);
  }

  public static void setStates(final String namespace) {
    final Map<String, List<TransitionDto>> transitions = Translator.getTransitions();
    final FunctionDto[] functions = Translator.getFunctions();
    final Function1<StateDto, Boolean> _function = (StateDto it) -> {
      return Boolean.valueOf(Objects.equals(it.namespace, namespace));
    };
    final Iterable<StateDto> topLevelStates = IterableExtensions.<StateDto>filter(((Iterable<StateDto>)Conversions.doWrapArray(Translator.getStates())), _function);
    final Consumer<StateDto> _function_1 = (StateDto state) -> {
      Translator.stateMachine.state(state.stateName);
      final List<TransitionDto> stateTransitions = transitions.getOrDefault(state.stateName, CollectionLiterals.<TransitionDto>newArrayList());
      final Consumer<TransitionDto> _function_2 = (TransitionDto transition) -> {
        final Function1<String, CharSequence> _function_3 = (String update) -> {
          return Translator.getAssignment(update, ((List<FunctionDto>)Conversions.doWrapArray(functions)));
        };
        final Iterable<CharSequence> updates = IterableExtensions.<CharSequence>filterNull(ListExtensions.<String, CharSequence>map(((List<String>)Conversions.doWrapArray(state.updates.split(","))), _function_3));
        final List<ClientBehaviourDto> client = Translator.getBehaviours().getOrDefault(transition.clientBehaviour, CollectionLiterals.<ClientBehaviourDto>newArrayList());
        final ArrayList<CharSequence> transitionUpdates = CollectionLiterals.<CharSequence>newArrayList();
        final Consumer<ClientBehaviourDto> _function_4 = (ClientBehaviourDto it) -> {
          final CharSequence assignment = Translator.getAssignment(it.methodName, ((List<FunctionDto>)Conversions.doWrapArray(functions)));
          if ((assignment != null)) {
            transitionUpdates.add(assignment);
          }
        };
        client.forEach(_function_4);
        final Function1<CharSequence, Boolean> _function_5 = (CharSequence guard) -> {
          final Function1<VariableDto, Boolean> _function_6 = (VariableDto variable) -> {
            return Boolean.valueOf(guard.toString().contains(variable.variable));
          };
          return Boolean.valueOf(IterableExtensions.<VariableDto>exists(Translator.getVariables(), _function_6));
        };
        final Iterable<CharSequence> guards = IterableExtensions.<CharSequence>filter(IterableExtensions.<CharSequence>filterNull(Translator.getGuards(transition)), _function_5);
        int _xifexpression = (int) 0;
        boolean _isTimer = transition.isTimer();
        if (_isTimer) {
          _xifexpression = state.timer;
        } else {
          _xifexpression = 0;
        }
        final int timeout = _xifexpression;
        String _xifexpression_1 = null;
        if (((!StringExtensions.isNullOrEmpty(state.action)) && transition.event.contains(state.action))) {
          _xifexpression_1 = state.action;
        } else {
          _xifexpression_1 = null;
        }
        final String when = _xifexpression_1;
        Translator.stateMachine.state(state.stateName).transition(transition.target).guard(IterableExtensions.join(guards, " &amp;&amp; ")).timeout(timeout).action(IterableExtensions.join(Iterables.<CharSequence>concat(updates, transitionUpdates), ", ")).when(when);
      };
      stateTransitions.forEach(_function_2);
      final String nestedNamespace = state.stateName;
      final Function1<StateDto, Boolean> _function_3 = (StateDto it) -> {
        return Boolean.valueOf(Objects.equals(it.namespace, nestedNamespace));
      };
      final Function1<StateDto, Boolean> _function_4 = (StateDto it) -> {
        return Boolean.valueOf((!it.nestedInitial));
      };
      final List<StateDto> childStates = IterableExtensions.<StateDto, Boolean>sortBy(IterableExtensions.<StateDto>filter(((Iterable<StateDto>)Conversions.doWrapArray(Translator.getStates())), _function_3), _function_4);
      boolean _isEmpty = childStates.isEmpty();
      boolean _not = (!_isEmpty);
      if (_not) {
        final Procedure1<State> _function_5 = (State it) -> {
          final Consumer<StateDto> _function_6 = (StateDto nested) -> {
            final Function1<String, CharSequence> _function_7 = (String update) -> {
              return Translator.getAssignment(update, ((List<FunctionDto>)Conversions.doWrapArray(functions)));
            };
            final Iterable<CharSequence> updates = IterableExtensions.<CharSequence>filterNull(ListExtensions.<String, CharSequence>map(((List<String>)Conversions.doWrapArray(nested.updates.split(","))), _function_7));
            final List<TransitionDto> nestedTransitions = transitions.getOrDefault(nested.stateName, CollectionLiterals.<TransitionDto>newArrayList());
            it.nestedState(nested.stateName);
            final Consumer<TransitionDto> _function_8 = (TransitionDto transition) -> {
              final Function1<StateDto, Boolean> _function_9 = (StateDto it_1) -> {
                return Boolean.valueOf(Objects.equals(it_1.stateName, transition.target));
              };
              final StateDto target = IterableExtensions.<StateDto>findFirst(((Iterable<StateDto>)Conversions.doWrapArray(Translator.getStates())), _function_9);
              if (((target != null) && Objects.equals(target.namespace, nested.namespace))) {
                final Iterable<CharSequence> guards = Translator.getGuards(transition);
                int _xifexpression = (int) 0;
                boolean _isTimer = transition.isTimer();
                if (_isTimer) {
                  _xifexpression = nested.timer;
                } else {
                  _xifexpression = 0;
                }
                final int timeout = _xifexpression;
                String _xifexpression_1 = null;
                if (((!StringExtensions.isNullOrEmpty(nested.action)) && transition.event.contains(nested.action))) {
                  _xifexpression_1 = nested.action;
                } else {
                  _xifexpression_1 = null;
                }
                final String when = _xifexpression_1;
                it.nestedState(nested.stateName).transition(transition.target).guard(IterableExtensions.join(guards, " &amp;&amp; ")).timeout(timeout).action(IterableExtensions.join(updates, ", ")).when(when);
                return;
              }
              String _xifexpression_2 = null;
              if (((!StringExtensions.isNullOrEmpty(nested.action)) && transition.event.contains(nested.action))) {
                _xifexpression_2 = nested.action;
              } else {
                _xifexpression_2 = null;
              }
              final String when_1 = _xifexpression_2;
              State _nestedState = it.nestedState(nested.stateName);
              StringConcatenation _builder = new StringConcatenation();
              _builder.append(nested.namespace);
              String _message = transition.message();
              _builder.append(_message);
              _nestedState.transition(_builder.toString()).when(when_1);
              StringConcatenation _builder_1 = new StringConcatenation();
              _builder_1.append(nested.namespace);
              String _message_1 = transition.message();
              _builder_1.append(_message_1);
              it.nestedState(_builder_1.toString()).committed();
              Translator.stateMachine.state(nested.namespace).transition(transition.target).when(transition.message()).action(IterableExtensions.join(updates, ", "));
            };
            nestedTransitions.forEach(_function_8);
          };
          childStates.forEach(_function_6);
        };
        Translator.stateMachine.state(state.stateName).nesting(_function_5);
      }
    };
    topLevelStates.forEach(_function_1);
  }

  public static Iterable<CharSequence> getGuards(final TransitionDto transition) {
    Iterable<CharSequence> _xifexpression = null;
    boolean _isReactorTransition = transition.isReactorTransition();
    if (_isReactorTransition) {
      _xifexpression = Translator.map(Translator.getConditions(transition));
    } else {
      _xifexpression = Translator.map(transition);
    }
    return _xifexpression;
  }

  public static Iterable<ConditionDto> getConditions(final TransitionDto transition) {
    final Function1<StateReactorDto, Boolean> _function = (StateReactorDto it) -> {
      return Boolean.valueOf(Objects.equals(it.name, transition.reactor));
    };
    final Function1<StateReactorDto, ConditionDto> _function_1 = (StateReactorDto it) -> {
      return new ConditionDto(it);
    };
    return IterableExtensions.<StateReactorDto, ConditionDto>map(IterableExtensions.<StateReactorDto>filter(((Iterable<StateReactorDto>)Conversions.doWrapArray(Translator.getReactors())), _function), _function_1);
  }

  public static Iterable<CharSequence> map(final Iterable<ConditionDto> conditions) {
    final Function1<ConditionDto, Iterable<CharSequence>> _function = (ConditionDto condition) -> {
      return Translator.map(Translator.getBehaviours().getOrDefault(condition.clientBehaviour, CollectionLiterals.<ClientBehaviourDto>newArrayList()), condition.requiresSuccess);
    };
    return IterableExtensions.<ConditionDto, CharSequence>flatMap(conditions, _function);
  }

  public static Iterable<CharSequence> map(final TransitionDto transition) {
    return Translator.map(Translator.getClientBehaviours(transition), transition.requiresSuccess());
  }

  public static List<ClientBehaviourDto> getClientBehaviours(final TransitionDto transition) {
    return Translator.getBehaviours().getOrDefault(transition.clientBehaviour, CollectionLiterals.<ClientBehaviourDto>newArrayList());
  }

  public static Iterable<CharSequence> map(final Iterable<ClientBehaviourDto> clientBehaviours, final boolean requiresSuccess) {
    final Function1<ClientBehaviourDto, Boolean> _function = (ClientBehaviourDto it) -> {
      boolean _equals = Objects.equals(it.event, "postSuccessEvent");
      return Boolean.valueOf((requiresSuccess == _equals));
    };
    final Function1<ClientBehaviourDto, CharSequence> _function_1 = (ClientBehaviourDto behaviour) -> {
      final Function1<FunctionDto, Boolean> _function_2 = (FunctionDto it) -> {
        return Boolean.valueOf(Objects.equals(it.function, behaviour.methodName));
      };
      FunctionDto _findFirst = IterableExtensions.<FunctionDto>findFirst(((Iterable<FunctionDto>)Conversions.doWrapArray(Translator.getFunctions())), _function_2);
      CharSequence _convertedExpression = null;
      if (_findFirst!=null) {
        _convertedExpression=_findFirst.convertedExpression(behaviour.inIf);
      }
      return _convertedExpression;
    };
    return IterableExtensions.<ClientBehaviourDto, CharSequence>map(IterableExtensions.<ClientBehaviourDto>filter(clientBehaviours, _function), _function_1);
  }

  public static StateDto[] getStates() {
    StateDto[] _xblockexpression = null;
    {
      final StateDto[] states = new Gson().<StateDto[]>fromJson(Translator.getJson("states"), StateDto[].class);
      final Consumer<StateDto> _function = (StateDto it) -> {
        it.stateName = it.convert(it.stateName);
      };
      ((List<StateDto>)Conversions.doWrapArray(states)).forEach(_function);
      _xblockexpression = states;
    }
    return _xblockexpression;
  }

  public static CharSequence getAssignment(final String action, final List<FunctionDto> functions) {
    CharSequence _xblockexpression = null;
    {
      final Function1<FunctionDto, Boolean> _function = (FunctionDto f) -> {
        return Boolean.valueOf((Objects.equals(f.function, action) && (!Objects.equals(f.type, "return"))));
      };
      FunctionDto current = IterableExtensions.<FunctionDto>findFirst(functions, _function);
      Object defaultVal = null;
      while (((current != null) && (!Objects.equals(current.type, "assignment")))) {
        {
          if ((defaultVal == null)) {
            defaultVal = current.defaultValue;
          }
          final String currentExpr = current.expression;
          final Function1<FunctionDto, Boolean> _function_1 = (FunctionDto f) -> {
            return Boolean.valueOf(Objects.equals(f.function, currentExpr));
          };
          current = IterableExtensions.<FunctionDto>findFirst(functions, _function_1);
        }
      }
      Object _elvis = null;
      if (defaultVal != null) {
        _elvis = defaultVal;
      } else {
        String _defaultValue = null;
        if (current!=null) {
          _defaultValue=current.defaultValue;
        }
        _elvis = _defaultValue;
      }
      final Object value = _elvis;
      CharSequence _xifexpression = null;
      if (((current == null) || (!((value instanceof Integer) && IterableExtensions.<VariableDto>exists(Translator.getVariables(), ((Function1<VariableDto, Boolean>) (VariableDto variable) -> {
        return Boolean.valueOf(value.toString().contains(variable.variable));
      })))))) {
        _xifexpression = null;
      } else {
        StringConcatenation _builder = new StringConcatenation();
        _builder.append(current.expression);
        _builder.append(" := ");
        Object _elvis_1 = null;
        if (defaultVal != null) {
          _elvis_1 = defaultVal;
        } else {
          _elvis_1 = current.defaultValue;
        }
        _builder.append(_elvis_1);
        _xifexpression = _builder;
      }
      _xblockexpression = _xifexpression;
    }
    return _xblockexpression;
  }

  public static List<VariableDto> getVariables() {
    final Function1<VariableDto, String> _function = (VariableDto it) -> {
      return it.variable;
    };
    final Function1<List<VariableDto>, VariableDto> _function_1 = (List<VariableDto> it) -> {
      return IterableExtensions.<VariableDto>head(it);
    };
    return IterableExtensions.<VariableDto>toList(IterableExtensions.<List<VariableDto>, VariableDto>map(IterableExtensions.<String, VariableDto>groupBy(((Iterable<? extends VariableDto>)Conversions.doWrapArray(new Gson().<VariableDto[]>fromJson(Translator.getJson("variables"), VariableDto[].class))), _function).values(), _function_1));
  }

  public static void setVariables(final List<VariableDto> variables) {
    final Consumer<VariableDto> _function = (VariableDto variableDefinition) -> {
      final Procedure1<StateMachine> _function_1 = (StateMachine it) -> {
        it.variable(variableDefinition.variable).type(variableDefinition.convertedType()).value(variableDefinition.initializedValue());
      };
      Translator.stateMachine.variables(_function_1);
    };
    variables.forEach(_function);
  }

  public static StateReactorDto[] getReactors() {
    return new Gson().<StateReactorDto[]>fromJson(Translator.getJson("state_reactors"), StateReactorDto[].class);
  }

  public static Map<String, List<ClientBehaviourDto>> getBehaviours() {
    final Function1<ClientBehaviourDto, String> _function = (ClientBehaviourDto it) -> {
      return it.name;
    };
    final Function1<List<ClientBehaviourDto>, List<ClientBehaviourDto>> _function_1 = (List<ClientBehaviourDto> it) -> {
      return IterableExtensions.<ClientBehaviourDto>toList(it);
    };
    return MapExtensions.<String, List<ClientBehaviourDto>, List<ClientBehaviourDto>>mapValues(IterableExtensions.<String, ClientBehaviourDto>groupBy(((Iterable<? extends ClientBehaviourDto>)Conversions.doWrapArray(new Gson().<ClientBehaviourDto[]>fromJson(Translator.getJson("client_behaviours"), ClientBehaviourDto[].class))), _function), _function_1);
  }

  public static FunctionDto[] getFunctions() {
    return new Gson().<FunctionDto[]>fromJson(Translator.getJson("functions"), FunctionDto[].class);
  }

  public static Map<String, List<TransitionDto>> getTransitions() {
    Map<String, List<TransitionDto>> _xblockexpression = null;
    {
      final TransitionDto[] transitions = new Gson().<TransitionDto[]>fromJson(Translator.getJson("transitions"), TransitionDto[].class);
      final Consumer<TransitionDto> _function = (TransitionDto it) -> {
        it.convert();
      };
      ((List<TransitionDto>)Conversions.doWrapArray(transitions)).forEach(_function);
      final Function1<TransitionDto, String> _function_1 = (TransitionDto it) -> {
        return it.stateName;
      };
      _xblockexpression = IterableExtensions.<String, TransitionDto>groupBy(((Iterable<? extends TransitionDto>)Conversions.doWrapArray(transitions)), _function_1);
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
