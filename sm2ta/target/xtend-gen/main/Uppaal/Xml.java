package Uppaal;

import com.google.common.collect.Iterables;
import danieljnm.sm2ta.StateMachine.State;
import danieljnm.sm2ta.StateMachine.StateMachine;
import danieljnm.sm2ta.StateMachine.Transition;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure2;

@SuppressWarnings("all")
public class Xml {
  public Declaration declaration;

  public List<Template> templates = CollectionLiterals.<Template>newArrayList();

  public Uppaal.System system;

  private GridLayout layout = new GridLayout();

  private int index = 0;

  public Xml(final StateMachine stateMachine) {
    this.index = 0;
    Declaration _declaration = new Declaration(stateMachine);
    this.declaration = _declaration;
    this.templates = this.setTemplates(stateMachine);
    Uppaal.System _system = new Uppaal.System(this.templates);
    this.system = _system;
  }

  public ArrayList<Template> setTemplates(final StateMachine stateMachine) {
    ArrayList<Template> _xblockexpression = null;
    {
      final ArrayList<Template> templates = CollectionLiterals.<Template>newArrayList();
      final Template template = new Template(stateMachine.name);
      final ArrayList<State> nestings = CollectionLiterals.<State>newArrayList();
      final Function1<State, Integer> _function = (State it) -> {
        return Integer.valueOf(it.index);
      };
      final Procedure2<State, Integer> _function_1 = (State state, Integer index) -> {
        if ((!state.isNested)) {
          template.location(state);
          template.transitions(state);
        }
        boolean _isEmpty = state.nestedStates.isEmpty();
        boolean _not = (!_isEmpty);
        if (_not) {
          nestings.add(state);
        }
      };
      IterableExtensions.<State>forEach(IterableExtensions.<State, Integer>sortBy(stateMachine.states.values(), _function), _function_1);
      this.layout.applyLayout(template);
      templates.add(template);
      final Consumer<State> _function_2 = (State nesting) -> {
        Template nestingTemplate = this.toTemplate(nesting);
        this.layout.applyLayout(nestingTemplate);
        templates.add(nestingTemplate);
      };
      nestings.forEach(_function_2);
      final HashMap<String, Synchronisation> synchronisations = CollectionLiterals.<String, Synchronisation>newHashMap();
      final Consumer<String> _function_3 = (String it) -> {
        int _plusPlus = this.index++;
        Synchronisation _synchronisation = new Synchronisation(it, "when", _plusPlus);
        synchronisations.put(it, _synchronisation);
      };
      stateMachine.whens().forEach(_function_3);
      final Consumer<String> _function_4 = (String it) -> {
        int _plusPlus = this.index++;
        Synchronisation _synchronisation = new Synchronisation(it, "signal", _plusPlus);
        synchronisations.put(it, _synchronisation);
      };
      stateMachine.signals().forEach(_function_4);
      final Function1<Synchronisation, Integer> _function_5 = (Synchronisation it) -> {
        return Integer.valueOf(it.index);
      };
      final Consumer<Synchronisation> _function_6 = (Synchronisation it) -> {
        final Template synchronisationTemplate = this.toTemplate(it);
        this.layout.applyLayout(synchronisationTemplate);
        templates.add(synchronisationTemplate);
      };
      IterableExtensions.<Synchronisation, Integer>sortBy(synchronisations.values(), _function_5).forEach(_function_6);
      _xblockexpression = templates;
    }
    return _xblockexpression;
  }

  public Template toTemplate(final State nesting) {
    Template _xblockexpression = null;
    {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append(nesting.name);
      _builder.append("_inner");
      final Template template = new Template(_builder.toString());
      State _transition = new State(nesting, "gen_init").initial().transition(nesting.nestedStates.get(0).name);
      StringConcatenation _builder_1 = new StringConcatenation();
      _builder_1.append("gen_");
      _builder_1.append(nesting.name);
      _builder_1.append("_inner_start");
      State initial = _transition.when(_builder_1.toString());
      template.location(initial);
      template.transitions(initial);
      final Procedure2<State, Integer> _function = (State it, Integer index) -> {
        template.location(it);
        template.transitions(it);
      };
      IterableExtensions.<State>forEach(nesting.nestedStates, _function);
      _xblockexpression = template;
    }
    return _xblockexpression;
  }

  public Set<String> whens(final StateMachine stateMachine) {
    final Function1<Transition, Boolean> _function = (Transition it) -> {
      return Boolean.valueOf((it.when != null));
    };
    final Function1<Transition, String> _function_1 = (Transition it) -> {
      return it.when;
    };
    return IterableExtensions.<String>toSet(IterableExtensions.<Transition, String>map(IterableExtensions.<Transition>filter(stateMachine.transitions(), _function), _function_1));
  }

  public Iterable<Transition> transitions(final StateMachine stateMachine) {
    final Function1<State, List<State>> _function = (State it) -> {
      return it.nestedStates;
    };
    final Function1<State, List<Transition>> _function_1 = (State it) -> {
      return it.transitions;
    };
    Iterable<Transition> _flatMap = IterableExtensions.<State, Transition>flatMap(IterableExtensions.<State, State>flatMap(stateMachine.states.values(), _function), _function_1);
    final Function1<State, List<Transition>> _function_2 = (State it) -> {
      return it.transitions;
    };
    Iterable<Transition> _flatMap_1 = IterableExtensions.<State, Transition>flatMap(stateMachine.states.values(), _function_2);
    return Iterables.<Transition>concat(_flatMap, _flatMap_1);
  }

  public Set<String> signals(final StateMachine stateMachine) {
    final Function1<State, List<Transition>> _function = (State it) -> {
      return it.transitions;
    };
    final Function1<Transition, Boolean> _function_1 = (Transition it) -> {
      return Boolean.valueOf((it.signal != null));
    };
    final Function1<Transition, String> _function_2 = (Transition it) -> {
      return it.signal;
    };
    return IterableExtensions.<String>toSet(IterableExtensions.<Transition, String>map(IterableExtensions.<Transition>filter(IterableExtensions.<State, Transition>flatMap(stateMachine.states.values(), _function), _function_1), _function_2));
  }

  public Template toTemplate(final Synchronisation synchronisation) {
    Template _xblockexpression = null;
    {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("gen_sync_");
      _builder.append(synchronisation.name);
      final Template template = new Template(_builder.toString());
      State initial = new State(((State) null), "initSync");
      final String _switchValue = synchronisation.type;
      if (_switchValue != null) {
        switch (_switchValue) {
          case "when":
            State _transition = initial.transition(initial);
            StringConcatenation _builder_1 = new StringConcatenation();
            _builder_1.append(synchronisation.name);
            _transition.signal(_builder_1.toString());
            break;
          case "signal":
            State _transition_1 = initial.transition(initial);
            StringConcatenation _builder_2 = new StringConcatenation();
            _builder_2.append(synchronisation.name);
            _transition_1.when(_builder_2.toString());
            break;
        }
      }
      template.location(initial);
      template.transitions(initial);
      _xblockexpression = template;
    }
    return _xblockexpression;
  }

  @Override
  public String toString() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
    _builder.newLine();
    _builder.append("<nta>");
    _builder.newLine();
    _builder.append("\t");
    _builder.append(this.declaration, "\t");
    _builder.newLineIfNotEmpty();
    {
      for(final Template template : this.templates) {
        _builder.append("\t");
        _builder.append(template, "\t");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.append("\t");
    _builder.append(this.system, "\t");
    _builder.newLineIfNotEmpty();
    _builder.append("</nta>");
    _builder.newLine();
    return _builder.toString();
  }
}
