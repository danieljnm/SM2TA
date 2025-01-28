package Uppaal;

import org.eclipse.xtend2.lib.StringConcatenation;

@SuppressWarnings("all")
public class Name {
  public String name;

  public int x;

  public int y;

  public Name(final String name) {
    this.name = name;
  }

  public Name(final String name, final int x, final int y) {
    this.name = name;
    this.x = x;
    this.y = y;
  }

  @Override
  public String toString() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("<name x=\"");
    _builder.append(this.x);
    _builder.append("\" y=\"");
    _builder.append(this.y);
    _builder.append("\">");
    _builder.append(this.name);
    _builder.append("</name>");
    _builder.newLineIfNotEmpty();
    return _builder.toString();
  }
}
