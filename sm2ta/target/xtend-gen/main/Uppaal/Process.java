package Uppaal;

import com.google.common.collect.Iterables;
import danieljnm.sm2ta.StateMachine.State;
import danieljnm.sm2ta.StateMachine.Transition;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ListExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure2;

@SuppressWarnings("all")
public class Process {
  public String name;

  public List<State> states = CollectionLiterals.<State>newArrayList();

  private State initialState;

  private int spacing = 15;

  private int increment = 150;

  private int currentY = 0;

  public Process(final String name) {
    this.name = name;
  }

  public void addState(final State state) {
    this.currentY = 0;
    if (state.isInitial) {
      this.initialState = state;
    }
    this.states.add(state);
    final Procedure2<Transition, Integer> _function = (Transition transition, Integer index) -> {
      transition.x = (state.x + this.increment);
      if (((index).intValue() == 0)) {
        transition.y = state.y;
        int _currentY = this.currentY;
        Integer _properties = transition.properties();
        int _multiply = (this.spacing * (_properties).intValue());
        this.currentY = (_currentY + _multiply);
        return;
      }
      transition.y = (this.currentY + this.spacing);
      Integer _properties_1 = transition.properties();
      int _multiply_1 = (this.spacing * (_properties_1).intValue());
      int _plus = (transition.y + _multiply_1);
      this.currentY = _plus;
    };
    IterableExtensions.<Transition>forEach(state.transitions, _function);
  }

  public State getInitialState() {
    State _xblockexpression = null;
    {
      if ((this.initialState == null)) {
        final Function1<State, Boolean> _function = (State it) -> {
          return null;
        };
        return IterableExtensions.<State>findFirst(this.states, _function);
      }
      _xblockexpression = this.initialState;
    }
    return _xblockexpression;
  }

  public String stateNames() {
    final Function1<State, List<String>> _function = (State it) -> {
      List<String> _xifexpression = null;
      boolean _isEmpty = it.nestedStates.isEmpty();
      if (_isEmpty) {
        String _format = this.format(it);
        _xifexpression = Collections.<String>unmodifiableList(CollectionLiterals.<String>newArrayList(_format));
      } else {
        StringConcatenation _builder = new StringConcatenation();
        _builder.append("gen_pre_");
        _builder.append(it.name);
        _xifexpression = Collections.<String>unmodifiableList(CollectionLiterals.<String>newArrayList(_builder.toString(), it.name));
      }
      return _xifexpression;
    };
    return IterableExtensions.join(IterableExtensions.<String>toSet(IterableExtensions.<State, String>flatMap(this.states, _function)), ",\n");
  }

  public String xmlStates() {
    final Function1<State, List<String>> _function = (State it) -> {
      List<String> _xifexpression = null;
      boolean _isEmpty = it.nestedStates.isEmpty();
      if (_isEmpty) {
        String _xmlFormat = this.xmlFormat(it);
        _xifexpression = Collections.<String>unmodifiableList(CollectionLiterals.<String>newArrayList(_xmlFormat));
      } else {
        StringConcatenation _builder = new StringConcatenation();
        _builder.append("<location id=\"gen_pre_");
        _builder.append(it.name);
        _builder.append("\" x=\"");
        _builder.append(it.x);
        _builder.append("\" y=\"");
        _builder.append(it.x);
        _builder.append("\">");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        _builder.append("<name x=\"");
        _builder.append((it.x - this.spacing), "\t");
        _builder.append("\" y=\"");
        _builder.append((it.y + this.spacing), "\t");
        _builder.append("\">gen_pre_");
        _builder.append(it.name, "\t");
        _builder.append("</name>");
        _builder.newLineIfNotEmpty();
        _builder.append("</location>");
        _builder.newLine();
        _xifexpression = Collections.<String>unmodifiableList(CollectionLiterals.<String>newArrayList(_builder.toString(), it.name));
      }
      return _xifexpression;
    };
    return IterableExtensions.join(IterableExtensions.<String>toSet(IterableExtensions.<State, String>flatMap(this.states, _function)));
  }

  public String xmlFormat(final State state) {
    String _xblockexpression = null;
    {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("<location id=\"");
      _builder.append(state.name);
      _builder.append("\" x=\"");
      _builder.append(state.x);
      _builder.append("\" y=\"");
      _builder.append(state.y);
      _builder.append("\">");
      _builder.newLineIfNotEmpty();
      _builder.append("\t");
      _builder.append("<name x=\"");
      _builder.append((state.x - this.spacing), "\t");
      _builder.append("\" y=\"");
      _builder.append((state.y + this.spacing), "\t");
      _builder.append("\">");
      _builder.append(state.name, "\t");
      _builder.append("</name>");
      _builder.newLineIfNotEmpty();
      _builder.append("\t");
      String _labels = this.labels(state);
      _builder.append(_labels, "\t");
      _builder.newLineIfNotEmpty();
      _builder.append("</location>");
      _builder.newLine();
      String location = _builder.toString();
      _xblockexpression = location;
    }
    return _xblockexpression;
  }

  public String labels(final State state) {
    final Function1<Transition, Boolean> _function = (Transition it) -> {
      return Boolean.valueOf((it.timeout > 0));
    };
    final Function1<Transition, String> _function_1 = (Transition it) -> {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("<label kind=\"invariant\" x=\"");
      _builder.append(((it.x - this.spacing) - this.increment));
      _builder.append("\" y=\"");
      _builder.append((it.y + (this.spacing * 2)));
      _builder.append("\">gen_clock &lt;= ");
      _builder.append(it.timeout);
      _builder.append("</label>");
      _builder.newLineIfNotEmpty();
      return _builder.toString();
    };
    return IterableExtensions.join(ListExtensions.<Transition, String>map(IterableExtensions.<Transition>toList(IterableExtensions.<Transition>filter(state.transitions, _function)), _function_1));
  }

  public String format(final State state) {
    String _xblockexpression = null;
    {
      boolean _isEmpty = state.transitions.isEmpty();
      if (_isEmpty) {
        return state.name;
      }
      final Function1<Transition, Boolean> _function = (Transition it) -> {
        return Boolean.valueOf((it.timeout > 0));
      };
      final Transition timeoutTransition = IterableExtensions.<Transition>findFirst(state.transitions, _function);
      if ((timeoutTransition != null)) {
        StringConcatenation _builder = new StringConcatenation();
        _builder.append(state.name);
        _builder.append(" {");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        _builder.append("gen_clock <= ");
        _builder.append(timeoutTransition.timeout, "\t");
        _builder.newLineIfNotEmpty();
        _builder.append("}");
        return _builder.toString();
      }
      _xblockexpression = state.name;
    }
    return _xblockexpression;
  }

  public Iterable<String> committedLocations() {
    Set<String> _nestedStateNames = this.nestedStateNames();
    Set<String> _signalTargets = this.signalTargets();
    return Iterables.<String>concat(_nestedStateNames, _signalTargets);
  }

  public Set<String> nestedStateNames() {
    final Function1<State, Boolean> _function = (State it) -> {
      boolean _isEmpty = it.nestedStates.isEmpty();
      return Boolean.valueOf((!_isEmpty));
    };
    final Function1<State, String> _function_1 = (State it) -> {
      return it.name;
    };
    return IterableExtensions.<String>toSet(IterableExtensions.<State, String>map(IterableExtensions.<State>filter(this.states, _function), _function_1));
  }

  public Set<String> signalTargets() {
    final Function1<State, List<Transition>> _function = (State it) -> {
      return it.transitions;
    };
    final Function1<Transition, Boolean> _function_1 = (Transition it) -> {
      return Boolean.valueOf(((it.signal != null) && it.target.isNested));
    };
    final Function1<Transition, String> _function_2 = (Transition it) -> {
      return it.target.name;
    };
    return IterableExtensions.<String>toSet(IterableExtensions.<Transition, String>map(IterableExtensions.<Transition>filter(IterableExtensions.<State, Transition>flatMap(this.states, _function), _function_1), _function_2));
  }

  public Set<String> signalTransitions() {
    final Function1<State, List<Transition>> _function = (State it) -> {
      return it.transitions;
    };
    final Function1<Transition, Boolean> _function_1 = (Transition it) -> {
      return Boolean.valueOf(((it.signal != null) && it.target.isNested));
    };
    final Function1<Transition, String> _function_2 = (Transition it) -> {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append(it.target.name);
      _builder.append(" -> gen_init {");
      _builder.newLineIfNotEmpty();
      _builder.append("}");
      return _builder.toString();
    };
    return IterableExtensions.<String>toSet(IterableExtensions.<Transition, String>map(IterableExtensions.<Transition>filter(IterableExtensions.<State, Transition>flatMap(this.states, _function), _function_1), _function_2));
  }

  public String transitions() {
    Iterable<String> _actualTransitions = this.actualTransitions();
    Iterable<String> _nestedStateTransitions = this.nestedStateTransitions();
    Iterable<String> _plus = Iterables.<String>concat(_actualTransitions, _nestedStateTransitions);
    Set<String> _signalTransitions = this.signalTransitions();
    return IterableExtensions.join(Iterables.<String>concat(_plus, _signalTransitions), ",\n");
  }

  public Iterable<String> actualTransitions() {
    final Function1<State, List<String>> _function = (State state) -> {
      final Function1<Transition, String> _function_1 = (Transition transition) -> {
        StringConcatenation _builder = new StringConcatenation();
        _builder.append(state.name);
        _builder.append(" -> ");
        CharSequence _targetName = transition.targetName(state.isNested);
        _builder.append(_targetName);
        _builder.append(" {");
        _builder.newLineIfNotEmpty();
        {
          if ((transition.guard != null)) {
            _builder.append("\t");
            _builder.append("guard ");
            Object _guardValue = transition.guardValue();
            _builder.append(_guardValue, "\t");
            _builder.append(";");
            _builder.newLineIfNotEmpty();
          }
        }
        {
          if ((transition.timeout > 0)) {
            _builder.append("\t");
            _builder.append("guard gen_clock >= ");
            _builder.append(transition.timeout, "\t");
            _builder.append(";");
            _builder.newLineIfNotEmpty();
          }
        }
        {
          if ((transition.signal != null)) {
            _builder.append("\t");
            _builder.append("sync ");
            _builder.append(transition.signal, "\t");
            _builder.append("!;");
            _builder.newLineIfNotEmpty();
          }
        }
        {
          if ((transition.when != null)) {
            _builder.append("\t");
            _builder.append("sync ");
            _builder.append(transition.when, "\t");
            _builder.append("?;");
            _builder.newLineIfNotEmpty();
          }
        }
        {
          boolean _isEmpty = transition.assignments().isEmpty();
          boolean _not = (!_isEmpty);
          if (_not) {
            _builder.append("\t");
            _builder.append("assign ");
            String _join = IterableExtensions.join(transition.assignments(), ", ");
            _builder.append(_join, "\t");
            _builder.append(";");
            _builder.newLineIfNotEmpty();
          }
        }
        _builder.append("}");
        return _builder.toString();
      };
      return ListExtensions.<Transition, String>map(state.transitions, _function_1);
    };
    return IterableExtensions.<State, String>flatMap(this.states, _function);
  }

  public String xmlTransitions() {
    final Function1<State, List<String>> _function = (State state) -> {
      final Function1<Transition, String> _function_1 = (Transition transition) -> {
        StringConcatenation _builder = new StringConcatenation();
        _builder.append("<transition>");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("<source ref=\"");
        _builder.append(state.name, "\t");
        _builder.append("\"/>");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        _builder.append("<target ref=\"");
        CharSequence _targetName = transition.targetName(state.isNested);
        _builder.append(_targetName, "\t");
        _builder.append("\"/>");
        _builder.newLineIfNotEmpty();
        {
          boolean _hasGuard = transition.hasGuard();
          if (_hasGuard) {
            _builder.append("\t");
            _builder.append("<label kind=\"guard\" x=\"");
            _builder.append(transition.x, "\t");
            _builder.append("\" y=\"");
            _builder.append(transition.y, "\t");
            _builder.append("\">");
            String _xmlGuard = transition.xmlGuard();
            _builder.append(_xmlGuard, "\t");
            _builder.append("</label>");
            _builder.newLineIfNotEmpty();
          }
        }
        {
          boolean _hasTimeout = transition.hasTimeout();
          if (_hasTimeout) {
            _builder.append("\t");
            _builder.append("<label kind=\"guard\" x=\"");
            _builder.append(transition.x, "\t");
            _builder.append("\" y=\"");
            _builder.append(transition.y, "\t");
            _builder.append("\">gen_clock &gt;= ");
            _builder.append(transition.timeout, "\t");
            _builder.append("</label>");
            _builder.newLineIfNotEmpty();
          }
        }
        {
          boolean _hasSignal = transition.hasSignal();
          if (_hasSignal) {
            _builder.append("\t");
            _builder.append("<label kind=\"synchronisation\" x=\"");
            _builder.append(transition.x, "\t");
            _builder.append("\" y=\"");
            _builder.append(transition.y, "\t");
            _builder.append("\">");
            _builder.append(transition.signal, "\t");
            _builder.append("!</label>");
            _builder.newLineIfNotEmpty();
          }
        }
        {
          boolean _hasWhen = transition.hasWhen();
          if (_hasWhen) {
            _builder.append("\t");
            _builder.append("<label kind=\"synchronisation\" x=\"");
            _builder.append(transition.x, "\t");
            _builder.append("\" y=\"");
            _builder.append(transition.y, "\t");
            _builder.append("\">");
            _builder.append(transition.when, "\t");
            _builder.append("?</label>");
            _builder.newLineIfNotEmpty();
          }
        }
        {
          boolean _hasAssignment = transition.hasAssignment();
          if (_hasAssignment) {
            _builder.append("\t");
            _builder.append("<label kind=\"assignment\" x=\"");
            _builder.append(transition.x, "\t");
            _builder.append("\" y=\"");
            _builder.append(transition.y, "\t");
            _builder.append("\">");
            String _join = IterableExtensions.join(transition.assignments(), ", ");
            _builder.append(_join, "\t");
            _builder.append("</label>");
            _builder.newLineIfNotEmpty();
          }
        }
        _builder.append("</transition>");
        _builder.newLine();
        return _builder.toString();
      };
      return ListExtensions.<Transition, String>map(state.transitions, _function_1);
    };
    return IterableExtensions.join(IterableExtensions.<State, String>flatMap(this.states, _function));
  }

  public Iterable<String> nestedStateTransitions() {
    final Function1<String, String> _function = (String it) -> {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("gen_pre_");
      _builder.append(it);
      _builder.append(" -> ");
      _builder.append(it);
      _builder.append(" {");
      _builder.newLineIfNotEmpty();
      _builder.append("\t");
      _builder.append("sync gen_");
      _builder.append(it, "\t");
      _builder.append("_inner_start!;");
      _builder.newLineIfNotEmpty();
      _builder.append("}");
      return _builder.toString();
    };
    return IterableExtensions.<String, String>map(this.nestedStateNames(), _function);
  }

  @Override
  public String toString() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("process ");
    _builder.append(this.name);
    _builder.append(" {");
    _builder.newLineIfNotEmpty();
    {
      boolean _isEmpty = this.states.isEmpty();
      boolean _not = (!_isEmpty);
      if (_not) {
        _builder.append("\t");
        _builder.append("state");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t");
        String _stateNames = this.stateNames();
        _builder.append(_stateNames, "\t\t");
        _builder.append(";");
        _builder.newLineIfNotEmpty();
        {
          boolean _isEmpty_1 = IterableExtensions.isEmpty(this.committedLocations());
          boolean _not_1 = (!_isEmpty_1);
          if (_not_1) {
            _builder.append("\t");
            _builder.append("commit ");
            final Function1<String, String> _function = (String it) -> {
              StringConcatenation _builder_1 = new StringConcatenation();
              _builder_1.append("gen_pre_");
              _builder_1.append(it);
              return _builder_1.toString();
            };
            Iterable<String> _map = IterableExtensions.<String, String>map(this.nestedStateNames(), _function);
            Set<String> _signalTargets = this.signalTargets();
            String _join = IterableExtensions.join(Iterables.<String>concat(_map, _signalTargets), ", ");
            _builder.append(_join, "\t");
            _builder.append(";");
            _builder.newLineIfNotEmpty();
          }
        }
        _builder.append("\t");
        _builder.append("init ");
        _builder.append(this.initialState.name, "\t");
        _builder.append(";");
        _builder.newLineIfNotEmpty();
      }
    }
    {
      if ((IterableExtensions.<State>exists(this.states, ((Function1<State, Boolean>) (State it) -> {
        boolean _isEmpty_2 = it.transitions.isEmpty();
        return Boolean.valueOf((!_isEmpty_2));
      })) || (!this.nestedStateNames().isEmpty()))) {
        _builder.append("\t");
        _builder.append("trans");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t");
        String _transitions = this.transitions();
        _builder.append(_transitions, "\t\t");
        _builder.append(";");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.append("}");
    _builder.newLine();
    return _builder.toString();
  }

  public CharSequence toXml() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("<template>");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("<name>");
    _builder.append(this.name, "\t");
    _builder.append("</name>");
    _builder.newLineIfNotEmpty();
    {
      int _length = this.xmlStates().length();
      boolean _greaterThan = (_length > 0);
      if (_greaterThan) {
        _builder.append("\t");
        String _xmlStates = this.xmlStates();
        _builder.append(_xmlStates, "\t");
        _builder.newLineIfNotEmpty();
      }
    }
    {
      if ((this.initialState != null)) {
        _builder.append("\t");
        _builder.append("<init ref=\"");
        _builder.append(this.initialState.name, "\t");
        _builder.append("\"/>");
        _builder.newLineIfNotEmpty();
      }
    }
    {
      if ((IterableExtensions.<State>exists(this.states, ((Function1<State, Boolean>) (State it) -> {
        boolean _isEmpty = it.transitions.isEmpty();
        return Boolean.valueOf((!_isEmpty));
      })) || (!this.nestedStateNames().isEmpty()))) {
        _builder.append("\t");
        String _xmlTransitions = this.xmlTransitions();
        _builder.append(_xmlTransitions, "\t");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.append("</template>");
    _builder.newLine();
    return _builder;
  }
}
