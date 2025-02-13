package Uppaal;

import java.util.List;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;

@SuppressWarnings("all")
public class System {
  public List<String> systems = CollectionLiterals.<String>newArrayList();

  public System(final List<Template> templates) {
    final Function1<Template, Boolean> _function = (Template it) -> {
      return Boolean.valueOf((!(it.exclude).booleanValue()));
    };
    final Function1<Template, String> _function_1 = (Template it) -> {
      return it.name;
    };
    this.systems = IterableExtensions.<String>toList(IterableExtensions.<Template, String>map(IterableExtensions.<Template>filter(templates, _function), _function_1));
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
        _builder.append("system ");
        String _join = IterableExtensions.join(this.systems, ", ");
        _builder.append(_join, "\t");
        _builder.append(";");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.append("</system>");
    _builder.newLine();
    return _builder.toString();
  }
}
