package danieljnm.sm2ta.StateMachine;

import com.google.common.collect.Iterables;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ListExtensions;

@SuppressWarnings("all")
public class StateMachine {
  public String name;

  public HashMap<String, State> states = CollectionLiterals.<String, State>newHashMap();

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
      boolean _isEmpty = IterableExtensions.isEmpty(this.channels());
      boolean _not = (!_isEmpty);
      if (_not) {
        _builder.append("chan ");
        String _join = IterableExtensions.join(this.channels(), ", ");
        _builder.append(_join);
        _builder.append(";");
        _builder.newLineIfNotEmpty();
      }
    }
    {
      boolean _isEmpty_1 = IterableExtensions.isEmpty(this.clocks());
      boolean _not_1 = (!_isEmpty_1);
      if (_not_1) {
        _builder.append("clock ");
        String _join_1 = IterableExtensions.join(this.clocks(), ", ");
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
      Iterable<String> _channels = this.channels();
      for(final String channel : _channels) {
        String _channelToUppaal = this.channelToUppaal(channel);
        _builder.append(_channelToUppaal);
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.append("system ");
    final Function1<Uppaal.Process, String> _function = (Uppaal.Process it) -> {
      return it.name;
    };
    List<String> _map = ListExtensions.<Uppaal.Process, String>map(this.processes(), _function);
    final Function1<String, String> _function_1 = (String it) -> {
      StringConcatenation _builder_1 = new StringConcatenation();
      _builder_1.append("gen_sync_");
      _builder_1.append(this.name);
      return _builder_1.toString();
    };
    Iterable<String> _map_1 = IterableExtensions.<String, String>map(this.channels(), _function_1);
    String _join_2 = IterableExtensions.join(Iterables.<String>concat(_map, _map_1), ", ");
    _builder.append(_join_2);
    _builder.append(";");
    _builder.newLineIfNotEmpty();
    return _builder.toString();
  }

  public String channelToUppaal(final String channel) {
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
    _builder.append("!;");
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
      final Consumer<State> _function = (State state) -> {
        if ((!state.isNested)) {
          process.addState(state);
        }
      };
      this.states.values().forEach(_function);
      processes.add(process);
      _xblockexpression = processes;
    }
    return _xblockexpression;
  }

  public Iterable<String> channels() {
    final Function1<State, List<Transition>> _function = (State it) -> {
      return it.transitions;
    };
    final Function1<Transition, Boolean> _function_1 = (Transition it) -> {
      return Boolean.valueOf((it.when != null));
    };
    final Function1<Transition, String> _function_2 = (Transition it) -> {
      return it.when;
    };
    return IterableExtensions.<Transition, String>map(IterableExtensions.<Transition>filter(IterableExtensions.<State, Transition>flatMap(this.states.values(), _function), _function_1), _function_2);
  }

  public Iterable<String> clocks() {
    final Function1<State, List<Transition>> _function = (State it) -> {
      return it.transitions;
    };
    final Function1<Transition, Boolean> _function_1 = (Transition it) -> {
      return Boolean.valueOf((it.timeout != null));
    };
    final Function1<Transition, String> _function_2 = (Transition it) -> {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append(it.target.name);
      _builder.append("_gen_clock");
      return _builder.toString();
    };
    return IterableExtensions.<Transition, String>map(IterableExtensions.<Transition>filter(IterableExtensions.<State, Transition>flatMap(this.states.values(), _function), _function_1), _function_2);
  }
}
