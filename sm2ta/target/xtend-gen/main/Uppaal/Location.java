package Uppaal;

import danieljnm.sm2ta.StateMachine.State;
import danieljnm.sm2ta.StateMachine.Transition;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;

@SuppressWarnings("all")
public class Location {
  public String id;

  public int x;

  public int y;

  public Boolean committed = Boolean.valueOf(false);

  public Name name;

  public Label label;

  public Location(final State state) {
    this.id = state.name;
    Name _name = new Name(state.name);
    this.name = _name;
    this.committed = Boolean.valueOf(state.isCommitted);
    final Function1<Transition, Boolean> _function = (Transition it) -> {
      return Boolean.valueOf((it.timeout > 0));
    };
    Transition transition = IterableExtensions.<Transition>findFirst(state.transitions, _function);
    if ((transition != null)) {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("gen_clock <= ");
      _builder.append(transition.timeout);
      Label _label = new Label("invariant", _builder.toString());
      this.label = _label;
    }
  }

  public Location isCommitted() {
    Location _xblockexpression = null;
    {
      this.committed = Boolean.valueOf(true);
      _xblockexpression = this;
    }
    return _xblockexpression;
  }

  @Override
  public String toString() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("<location id=\"");
    _builder.append(this.id);
    _builder.append("\" x=\"");
    _builder.append(this.x);
    _builder.append("\" y=\"");
    _builder.append(this.y);
    _builder.append("\">");
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
    {
      if ((this.committed).booleanValue()) {
        _builder.append("\t");
        _builder.append("<committed/>");
        _builder.newLine();
      }
    }
    _builder.append("</location>");
    _builder.newLine();
    return _builder.toString();
  }
}
