package danieljnm.sm2ta.StateMachine;

import org.eclipse.xtend2.lib.StringConcatenation;

@SuppressWarnings("all")
public class Transition {
  private String event;

  private State target;

  private String guard;

  public Transition(final String event, final State target) {
    this.event = event;
    this.target = target;
  }

  public String getEvent() {
    return this.event;
  }

  public State getTarget() {
    return this.target;
  }

  public String setGuard(final String guard) {
    return this.guard = guard;
  }

  public String getGuard() {
    return this.guard;
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
        _builder.append(" ");
        {
          if ((this.guard != null)) {
            _builder.append("(");
            _builder.append(this.guard);
            _builder.append(")");
          }
        }
        _builder.newLineIfNotEmpty();
      }
    }
    return _builder.toString();
  }
}
