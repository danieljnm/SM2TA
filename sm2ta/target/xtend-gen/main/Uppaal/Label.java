package Uppaal;

import org.eclipse.xtend2.lib.StringConcatenation;

@SuppressWarnings("all")
public class Label {
  public String kind;

  public int x;

  public int y;

  public String value;

  public Label(final String kind, final int x, final int y, final String value) {
    this.kind = kind;
    this.x = x;
    this.y = y;
    this.value = value;
  }

  @Override
  public String toString() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("<label kind=\"");
    _builder.append(this.kind);
    _builder.append("\" x=\"");
    _builder.append(this.x);
    _builder.append("\" y=\"");
    _builder.append(this.y);
    _builder.append("\">");
    String _format = this.format(this.value);
    _builder.append(_format);
    _builder.append("</label>");
    _builder.newLineIfNotEmpty();
    return _builder.toString();
  }

  public String format(final String value) {
    return value.replace(">", "&gt;").replace("<", "&lt;");
  }
}
