package Uppaal;

import java.util.List;
import org.eclipse.xtend2.lib.StringConcatenation;

@SuppressWarnings("all")
public class Template {
  public String name;

  public List<Location> locations;

  public String initial;

  public List<Transition> transitions;

  public Template(final String name) {
    this.name = name;
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
        _builder.append("\"");
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
