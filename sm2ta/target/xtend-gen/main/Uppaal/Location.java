package Uppaal;

import org.eclipse.xtend2.lib.StringConcatenation;

@SuppressWarnings("all")
public class Location {
  public String id;

  public int x;

  public int y;

  public Boolean committed;

  public Name name;

  public Label label;

  @Override
  public String toString() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("<location id=\"");
    _builder.append(this.id);
    _builder.append("\" x=\"");
    _builder.append(this.x);
    _builder.append("\" y=\"");
    _builder.append(this.y);
    _builder.append("\"");
    {
      if ((this.committed).booleanValue()) {
        _builder.append(" committed=\"true\"");
      }
    }
    _builder.append(">");
    _builder.newLineIfNotEmpty();
    {
      if ((this.name != null)) {
        _builder.append("\t");
        _builder.append(this.name, "\t");
        _builder.newLineIfNotEmpty();
      }
    }
    {
      if ((this.label != null)) {
        _builder.append("\t");
        _builder.append(this.label, "\t");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.append("</location>");
    _builder.newLine();
    return _builder.toString();
  }
}
