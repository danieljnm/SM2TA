package Uppaal;

import java.util.List;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;

@SuppressWarnings("all")
public class Transition {
  public String source;

  public String target;

  public List<Label> labels = CollectionLiterals.<Label>newArrayList();

  @Override
  public String toString() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("<source ref=\"");
    _builder.append(this.source);
    _builder.append("\"/>");
    _builder.newLineIfNotEmpty();
    _builder.append("<target ref=\"");
    _builder.append(this.target);
    _builder.append("/>");
    _builder.newLineIfNotEmpty();
    {
      for(final Label label : this.labels) {
        _builder.append(label);
        _builder.newLineIfNotEmpty();
      }
    }
    return _builder.toString();
  }
}
