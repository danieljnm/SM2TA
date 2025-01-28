package Uppaal;

import java.util.List;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ListExtensions;

@SuppressWarnings("all")
public class System {
  public List<String> systems = CollectionLiterals.<String>newArrayList();

  public System(final List<Template> templates) {
    final Function1<Template, String> _function = (Template it) -> {
      return it.name;
    };
    this.systems = ListExtensions.<Template, String>map(templates, _function);
  }

  @Override
  public String toString() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("<system>");
    _builder.newLine();
    {
      boolean _isEmpty = this.systems.isEmpty();
      boolean _not = (!_isEmpty);
      if (_not) {
        _builder.append("\t");
        String _join = IterableExtensions.join(this.systems, ", ");
        _builder.append(_join, "\t");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.append("</system>");
    _builder.newLine();
    return _builder.toString();
  }
}
