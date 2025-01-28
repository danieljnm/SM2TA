package Uppaal;

import danieljnm.sm2ta.StateMachine.State;
import java.util.List;
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

  public boolean location(final State state) {
    boolean _xblockexpression = false;
    {
      if (state.isInitial) {
        this.initial = state.name;
      }
      Location location = new Location(state);
      _xblockexpression = this.locations.add(location);
    }
    return _xblockexpression;
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
