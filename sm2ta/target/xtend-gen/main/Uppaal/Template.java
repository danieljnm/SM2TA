package Uppaal;

import danieljnm.sm2ta.StateMachine.State;
import java.util.List;
import java.util.function.Consumer;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;

@SuppressWarnings("all")
public class Template {
  public String name;

  public List<Location> locations = CollectionLiterals.<Location>newArrayList();

  public String initial;

  public List<Transition> transitions = CollectionLiterals.<Transition>newArrayList();

  public Template(final String name) {
    this.name = name;
  }

  public Location location(final State state) {
    Location _xblockexpression = null;
    {
      if (state.isInitial) {
        this.initial = state.name;
      }
      Location location = new Location(state);
      this.locations.add(location);
      _xblockexpression = location;
    }
    return _xblockexpression;
  }

  public void transitions(final State state) {
    final Consumer<danieljnm.sm2ta.StateMachine.Transition> _function = (danieljnm.sm2ta.StateMachine.Transition it) -> {
      final Transition transition = new Transition(state.name, it.target.name);
      transition.labels(it);
      this.transitions.add(transition);
    };
    state.transitions.forEach(_function);
  }

  @Override
  public String toString() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("<template>");
    _builder.newLine();
    {
      if ((this.name != null)) {
        _builder.append("\t");
        _builder.append("<name>");
        _builder.append(this.name, "\t");
        _builder.append("</name>");
        _builder.newLineIfNotEmpty();
      }
    }
    {
      for(final Location location : this.locations) {
        _builder.append("\t");
        _builder.append(location, "\t");
        _builder.newLineIfNotEmpty();
      }
    }
    {
      if ((this.initial != null)) {
        _builder.append("\t");
        _builder.append("<init ref=\"");
        _builder.append(this.initial, "\t");
        _builder.append("\"/>");
        _builder.newLineIfNotEmpty();
      }
    }
    {
      for(final Transition transition : this.transitions) {
        _builder.append("\t");
        _builder.append(transition, "\t");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.append("</template>");
    _builder.newLine();
    return _builder.toString();
  }
}
