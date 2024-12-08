package danieljnm.sm2ta;

import org.eclipse.xtend2.lib.StringConcatenation;

@SuppressWarnings("all")
public class Transition {
  private String event;

  private TransitionType type;

  private State target;

  public Transition(final String event, final TransitionType type, final State target) {
    this.event = event;
    this.type = type;
    this.target = target;
  }

  @Override
  public String toString() {
    StringConcatenation _builder = new StringConcatenation();
    {
      if ((this.target != null)) {
        _builder.append(this.event);
        _builder.append(" -> ");
        String _name = this.target.getName();
        _builder.append(_name);
        _builder.newLineIfNotEmpty();
      }
    }
    return _builder.toString();
  }
}
