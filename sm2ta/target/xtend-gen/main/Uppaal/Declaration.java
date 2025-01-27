package Uppaal;

import com.google.common.collect.Iterables;
import danieljnm.sm2ta.StateMachine.State;
import danieljnm.sm2ta.StateMachine.StateMachine;
import danieljnm.sm2ta.StateMachine.Transition;
import danieljnm.sm2ta.StateMachine.Variable;
import java.util.Collection;
import java.util.List;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ListExtensions;

@SuppressWarnings("all")
public class Declaration {
  public List<String> variables = CollectionLiterals.<String>newArrayList();

  public Boolean hasClock;

  public List<String> channels = CollectionLiterals.<String>newArrayList();

  public Declaration(final StateMachine stateMachine) {
    final Function1<Variable, String> _function = (Variable it) -> {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append(it.type);
      _builder.append(" ");
      _builder.append(it.name);
      _builder.append(" = ");
      _builder.append(it.value);
      _builder.append(";");
      return _builder.toString();
    };
    this.variables = ListExtensions.<Variable, String>map(stateMachine.variables, _function);
    this.hasClock = Boolean.valueOf(this.setClock(stateMachine.states.values()));
    this.channels = this.setChannels(stateMachine.states.values());
  }

  public boolean setClock(final Collection<State> states) {
    return (IterableExtensions.<Transition>exists(IterableExtensions.<State, Transition>flatMap(states, ((Function1<State, List<Transition>>) (State it) -> {
      return it.transitions;
    })), ((Function1<Transition, Boolean>) (Transition it) -> {
      return Boolean.valueOf((it.timeout > 0));
    })) || 
      IterableExtensions.<Transition>exists(IterableExtensions.<State, Transition>flatMap(IterableExtensions.<State, State>flatMap(states, ((Function1<State, List<State>>) (State it) -> {
        return it.nestedStates;
      })), ((Function1<State, List<Transition>>) (State it) -> {
        return it.transitions;
      })), ((Function1<Transition, Boolean>) (Transition it) -> {
        return Boolean.valueOf((it.timeout > 0));
      })));
  }

  public List<String> setChannels(final Collection<State> states) {
    Iterable<String> _whens = this.whens(states);
    Iterable<String> _signals = this.signals(states);
    Iterable<String> _plus = Iterables.<String>concat(_whens, _signals);
    Iterable<String> _nestings = this.nestings(states);
    return IterableExtensions.<String>toList(IterableExtensions.<String>toSet(Iterables.<String>concat(_plus, _nestings)));
  }

  public Iterable<String> signals(final Collection<State> states) {
    final Function1<State, List<Transition>> _function = (State it) -> {
      return it.transitions;
    };
    final Function1<Transition, Boolean> _function_1 = (Transition it) -> {
      return Boolean.valueOf((it.signal != null));
    };
    final Function1<Transition, String> _function_2 = (Transition it) -> {
      return it.signal;
    };
    return IterableExtensions.<Transition, String>map(IterableExtensions.<Transition>filter(IterableExtensions.<State, Transition>flatMap(states, _function), _function_1), _function_2);
  }

  public Iterable<String> whens(final Collection<State> states) {
    final Function1<State, List<State>> _function = (State it) -> {
      return it.nestedStates;
    };
    final Function1<State, List<Transition>> _function_1 = (State it) -> {
      return it.transitions;
    };
    Iterable<Transition> _flatMap = IterableExtensions.<State, Transition>flatMap(IterableExtensions.<State, State>flatMap(states, _function), _function_1);
    final Function1<State, List<Transition>> _function_2 = (State it) -> {
      return it.transitions;
    };
    Iterable<Transition> _flatMap_1 = IterableExtensions.<State, Transition>flatMap(states, _function_2);
    final Function1<Transition, Boolean> _function_3 = (Transition it) -> {
      return Boolean.valueOf((it.when != null));
    };
    final Function1<Transition, String> _function_4 = (Transition it) -> {
      return it.when;
    };
    return IterableExtensions.<Transition, String>map(IterableExtensions.<Transition>filter(Iterables.<Transition>concat(_flatMap, _flatMap_1), _function_3), _function_4);
  }

  public Iterable<String> nestings(final Collection<State> states) {
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
    return IterableExtensions.<State, String>map(IterableExtensions.<State>filter(states, _function), _function_1);
  }

  @Override
  public String toString() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("<declaration>");
    _builder.newLine();
    {
      boolean _isEmpty = this.variables.isEmpty();
      boolean _not = (!_isEmpty);
      if (_not) {
        _builder.append("\t");
        String _join = IterableExtensions.join(this.variables, "\n");
        _builder.append(_join, "\t");
        _builder.newLineIfNotEmpty();
      }
    }
    {
      if ((this.hasClock).booleanValue()) {
        _builder.append("\t");
        _builder.append("clock gen_clock;");
        _builder.newLine();
      }
    }
    {
      boolean _isEmpty_1 = this.channels.isEmpty();
      boolean _not_1 = (!_isEmpty_1);
      if (_not_1) {
        _builder.append("\t");
        _builder.append("chan ");
        String _join_1 = IterableExtensions.join(this.channels, ", ");
        _builder.append(_join_1, "\t");
        _builder.append(";");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.append("</declaration>");
    _builder.newLine();
    return _builder.toString();
  }
}
