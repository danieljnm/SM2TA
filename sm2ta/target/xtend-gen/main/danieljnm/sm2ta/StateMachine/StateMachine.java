package danieljnm.sm2ta.StateMachine;

import com.google.common.collect.Iterables;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ListExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class StateMachine {
  public int index;

  public String name;

  public HashMap<String, State> states = CollectionLiterals.<String, State>newHashMap();

  public List<Variable> variables = CollectionLiterals.<Variable>newArrayList();

  public StateMachine name(final String name) {
    StateMachine _xblockexpression = null;
    {
      this.name = name;
      _xblockexpression = this;
    }
    return _xblockexpression;
  }

  public State getInitialState() {
    final Function1<State, Boolean> _function = (State it) -> {
      return Boolean.valueOf(it.isInitial);
    };
    return IterableExtensions.<State>findFirst(this.states.values(), _function);
  }

  public State state(final String name) {
    final Function<String, State> _function = (String it) -> {
      return new State(this, name);
    };
    return this.states.computeIfAbsent(name, _function);
  }

  public boolean hasTransitions() {
    boolean _xblockexpression = false;
    {
      boolean _isEmpty = this.states.isEmpty();
      if (_isEmpty) {
        return false;
      }
      final Function1<State, Boolean> _function = (State it) -> {
        boolean _isEmpty_1 = it.transitions.isEmpty();
        return Boolean.valueOf((!_isEmpty_1));
      };
      State _findFirst = IterableExtensions.<State>findFirst(this.states.values(), _function);
      _xblockexpression = (_findFirst != null);
    }
    return _xblockexpression;
  }

  public String toUppaal() {
    StringConcatenation _builder = new StringConcatenation();
    {
      boolean _isEmpty = this.variables.isEmpty();
      boolean _not = (!_isEmpty);
      if (_not) {
        final Function1<Variable, String> _function = (Variable it) -> {
          StringConcatenation _builder_1 = new StringConcatenation();
          _builder_1.append(it.type);
          _builder_1.append(" ");
          _builder_1.append(it.name);
          _builder_1.append(" = ");
          _builder_1.append(it.value);
          return _builder_1.toString();
        };
        String _join = IterableExtensions.join(ListExtensions.<Variable, String>map(this.variables, _function), ";\n");
        _builder.append(_join);
        _builder.append(";");
        _builder.newLineIfNotEmpty();
      }
    }
    {
      boolean _hasClock = this.hasClock();
      if (_hasClock) {
        _builder.append("clock gen_clock;");
        _builder.newLine();
      }
    }
    {
      if (((!this.channels().isEmpty()) || (!this.nestings().isEmpty()))) {
        _builder.append("chan ");
        Set<String> _channels = this.channels();
        Set<String> _nestings = this.nestings();
        String _join_1 = IterableExtensions.join(Iterables.<String>concat(_channels, _nestings), ", ");
        _builder.append(_join_1);
        _builder.append(";");
        _builder.newLineIfNotEmpty();
      }
    }
    {
      ArrayList<Uppaal.Process> _processes = this.processes();
      for(final Uppaal.Process process : _processes) {
        _builder.append(process);
        _builder.newLineIfNotEmpty();
      }
    }
    {
      Set<String> _whenChannels = this.whenChannels();
      for(final String channel : _whenChannels) {
        String _channelToUppaal = this.channelToUppaal(channel, "!");
        _builder.append(_channelToUppaal);
        _builder.newLineIfNotEmpty();
      }
    }
    {
      Set<String> _signalChannels = this.signalChannels();
      for(final String channel_1 : _signalChannels) {
        String _channelToUppaal_1 = this.channelToUppaal(channel_1, "?");
        _builder.append(_channelToUppaal_1);
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.append("system ");
    final Function1<Uppaal.Process, String> _function_1 = (Uppaal.Process it) -> {
      return it.name;
    };
    List<String> _map = ListExtensions.<Uppaal.Process, String>map(this.processes(), _function_1);
    final Function1<String, String> _function_2 = (String it) -> {
      StringConcatenation _builder_1 = new StringConcatenation();
      _builder_1.append("gen_sync_");
      _builder_1.append(it);
      return _builder_1.toString();
    };
    Iterable<String> _map_1 = IterableExtensions.<String, String>map(this.uppaalChannels(), _function_2);
    String _join_2 = IterableExtensions.join(Iterables.<String>concat(_map, _map_1), ", ");
    _builder.append(_join_2);
    _builder.append(";");
    _builder.newLineIfNotEmpty();
    return _builder.toString();
  }

  public String channelToUppaal(final String channel, final String sign) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("process gen_sync_");
    _builder.append(channel);
    _builder.append(" {");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("state");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("initSync;");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("init initSync;");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("trans");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("initSync -> initSync {");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("sync ");
    _builder.append(channel, "\t\t\t");
    _builder.append(sign, "\t\t\t");
    _builder.append(";");
    _builder.newLineIfNotEmpty();
    _builder.append("\t\t");
    _builder.append("};");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    return _builder.toString();
  }

  public ArrayList<Uppaal.Process> processes() {
    ArrayList<Uppaal.Process> _xblockexpression = null;
    {
      final ArrayList<Uppaal.Process> processes = CollectionLiterals.<Uppaal.Process>newArrayList();
      final Uppaal.Process process = new Uppaal.Process(this.name);
      final ArrayList<State> nestings = CollectionLiterals.<State>newArrayList();
      final Function1<State, Integer> _function = (State it) -> {
        return Integer.valueOf(it.index);
      };
      final Consumer<State> _function_1 = (State state) -> {
        if ((!state.isNested)) {
          process.addState(state);
        }
        boolean _isEmpty = state.nestedStates.isEmpty();
        boolean _not = (!_isEmpty);
        if (_not) {
          nestings.add(state);
        }
      };
      IterableExtensions.<State, Integer>sortBy(this.states.values(), _function).forEach(_function_1);
      processes.add(process);
      final Consumer<State> _function_2 = (State nesting) -> {
        processes.add(this.toProcess(nesting));
      };
      nestings.forEach(_function_2);
      _xblockexpression = processes;
    }
    return _xblockexpression;
  }

  public Uppaal.Process toProcess(final State nesting) {
    Uppaal.Process _xblockexpression = null;
    {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append(nesting.name);
      _builder.append("_inner");
      final Uppaal.Process nestedProcess = new Uppaal.Process(_builder.toString());
      State _transition = new State(nesting, "gen_init").initial().transition(nesting.nestedStates.get(0).name);
      StringConcatenation _builder_1 = new StringConcatenation();
      _builder_1.append("gen_");
      _builder_1.append(nesting.name);
      _builder_1.append("_inner_start");
      State initial = _transition.when(_builder_1.toString());
      nestedProcess.addState(initial);
      final Consumer<State> _function = (State it) -> {
        nestedProcess.addState(it);
      };
      nesting.nestedStates.forEach(_function);
      _xblockexpression = nestedProcess;
    }
    return _xblockexpression;
  }

  public Set<String> uppaalChannels() {
    Set<String> _whenChannels = this.whenChannels();
    Set<String> _signalChannels = this.signalChannels();
    return IterableExtensions.<String>toSet(Iterables.<String>concat(_whenChannels, _signalChannels));
  }

  public Set<String> whenChannels() {
    final Function1<State, Boolean> _function = (State it) -> {
      return Boolean.valueOf(it.nestedStates.isEmpty());
    };
    final Function1<State, List<Transition>> _function_1 = (State it) -> {
      return it.transitions;
    };
    final Function1<Transition, Boolean> _function_2 = (Transition it) -> {
      return Boolean.valueOf((it.when != null));
    };
    final Function1<Transition, String> _function_3 = (Transition it) -> {
      return it.when;
    };
    return IterableExtensions.<String>toSet(IterableExtensions.<Transition, String>map(IterableExtensions.<Transition>filter(IterableExtensions.<State, Transition>flatMap(IterableExtensions.<State>filter(this.states.values(), _function), _function_1), _function_2), _function_3));
  }

  public Set<String> signalChannels() {
    final Function1<State, Boolean> _function = (State it) -> {
      return Boolean.valueOf(it.nestedStates.isEmpty());
    };
    final Function1<State, List<Transition>> _function_1 = (State it) -> {
      return it.transitions;
    };
    final Function1<Transition, Boolean> _function_2 = (Transition it) -> {
      return Boolean.valueOf((it.signal != null));
    };
    final Function1<Transition, String> _function_3 = (Transition it) -> {
      return it.signal;
    };
    return IterableExtensions.<String>toSet(IterableExtensions.<Transition, String>map(IterableExtensions.<Transition>filter(IterableExtensions.<State, Transition>flatMap(IterableExtensions.<State>filter(this.states.values(), _function), _function_1), _function_2), _function_3));
  }

  public Set<String> channels() {
    Iterable<String> _whens = this.whens();
    Iterable<String> _signals = this.signals();
    return IterableExtensions.<String>toSet(Iterables.<String>concat(_whens, _signals));
  }

  public Iterable<String> signals() {
    final Function1<State, List<Transition>> _function = (State it) -> {
      return it.transitions;
    };
    final Function1<Transition, Boolean> _function_1 = (Transition it) -> {
      return Boolean.valueOf((it.signal != null));
    };
    final Function1<Transition, String> _function_2 = (Transition it) -> {
      return it.signal;
    };
    return IterableExtensions.<Transition, String>map(IterableExtensions.<Transition>filter(IterableExtensions.<State, Transition>flatMap(this.states.values(), _function), _function_1), _function_2);
  }

  public Iterable<String> whens() {
    final Function1<Transition, Boolean> _function = (Transition it) -> {
      return Boolean.valueOf((it.when != null));
    };
    final Function1<Transition, String> _function_1 = (Transition it) -> {
      return it.when;
    };
    return IterableExtensions.<Transition, String>map(IterableExtensions.<Transition>filter(this.transitions(), _function), _function_1);
  }

  public Iterable<Transition> transitions() {
    final Function1<State, List<State>> _function = (State it) -> {
      return it.nestedStates;
    };
    final Function1<State, List<Transition>> _function_1 = (State it) -> {
      return it.transitions;
    };
    Iterable<Transition> _flatMap = IterableExtensions.<State, Transition>flatMap(IterableExtensions.<State, State>flatMap(this.states.values(), _function), _function_1);
    final Function1<State, List<Transition>> _function_2 = (State it) -> {
      return it.transitions;
    };
    Iterable<Transition> _flatMap_1 = IterableExtensions.<State, Transition>flatMap(this.states.values(), _function_2);
    return Iterables.<Transition>concat(_flatMap, _flatMap_1);
  }

  public Set<String> nestings() {
    final Function1<State, Boolean> _function = (State it) -> {
      boolean _isEmpty = it.nestedStates.isEmpty();
      return Boolean.valueOf((!_isEmpty));
    };
    final Function1<State, String> _function_1 = (State it) -> {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("gen_");
      _builder.append(it.name);
      _builder.append("_inner_start");
      return _builder.toString();
    };
    return IterableExtensions.<String>toSet(IterableExtensions.<State, String>map(IterableExtensions.<State>filter(this.states.values(), _function), _function_1));
  }

  public boolean hasClock() {
    return (IterableExtensions.<Transition>exists(IterableExtensions.<State, Transition>flatMap(this.states.values(), ((Function1<State, List<Transition>>) (State it) -> {
      return it.transitions;
    })), ((Function1<Transition, Boolean>) (Transition it) -> {
      return Boolean.valueOf((it.timeout > 0));
    })) || 
      IterableExtensions.<Transition>exists(IterableExtensions.<String, Transition>flatMap(IterableExtensions.<State, String>flatMap(this.states.values(), ((Function1<State, Set<String>>) (State it) -> {
        return this.nestings();
      })), ((Function1<String, Iterable<Transition>>) (String it) -> {
        return this.transitions();
      })), ((Function1<Transition, Boolean>) (Transition it) -> {
        return Boolean.valueOf((it.timeout > 0));
      })));
  }

  public Iterable<String> clocks() {
    final Function1<State, List<Transition>> _function = (State it) -> {
      return it.transitions;
    };
    final Function1<Transition, Boolean> _function_1 = (Transition it) -> {
      return Boolean.valueOf((it.timeout > 0));
    };
    final Function1<Transition, String> _function_2 = (Transition it) -> {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append(it.target.name);
      _builder.append("_gen_clock");
      return _builder.toString();
    };
    return IterableExtensions.<Transition, String>map(IterableExtensions.<Transition>filter(IterableExtensions.<State, Transition>flatMap(this.states.values(), _function), _function_1), _function_2);
  }

  public StateMachine variables(final Procedure1<? super StateMachine> context) {
    StateMachine _xblockexpression = null;
    {
      context.apply(this);
      _xblockexpression = this;
    }
    return _xblockexpression;
  }

  public StateMachine variable(final String name) {
    StateMachine _xblockexpression = null;
    {
      Variable _variable = new Variable(name);
      this.variables.add(_variable);
      _xblockexpression = this;
    }
    return _xblockexpression;
  }

  public StateMachine type(final String type) {
    StateMachine _xblockexpression = null;
    {
      boolean _isEmpty = this.variables.isEmpty();
      if (_isEmpty) {
        return this;
      }
      Variable _lastOrNull = IterableExtensions.<Variable>lastOrNull(this.variables);
      _lastOrNull.type = type;
      _xblockexpression = this;
    }
    return _xblockexpression;
  }

  public StateMachine value(final String value) {
    StateMachine _xblockexpression = null;
    {
      boolean _isEmpty = this.variables.isEmpty();
      if (_isEmpty) {
        return this;
      }
      Variable _lastOrNull = IterableExtensions.<Variable>lastOrNull(this.variables);
      _lastOrNull.value = value;
      _xblockexpression = this;
    }
    return _xblockexpression;
  }
}
