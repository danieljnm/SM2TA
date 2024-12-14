package danieljnm.sm2ta.StateMachine;

import org.eclipse.xtend2.lib.StringConcatenation;

@SuppressWarnings("all")
public class Transition {
  public String event;

  public State target;

  public String guard;

  public String action;

  public String timeout;

  public Transition(final String event, final State target) {
    this.event = event;
    this.target = target;
  }

  @Override
  public String toString() {
    StringConcatenation _builder = new StringConcatenation();
    {
      if ((this.target != null)) {
        _builder.append(this.event);
        _builder.append(" -> ");
        _builder.append(this.target.name);
        {
          if ((this.guard != null)) {
            _builder.append(" (Guard: ");
            _builder.append(this.guard);
            _builder.append(")");
          }
        }
        {
          if ((this.action != null)) {
            _builder.append(" (Action: ");
            _builder.append(this.action);
            _builder.append(")");
          }
        }
        {
          if ((this.timeout != null)) {
            _builder.append(" (Timeout: ");
            _builder.append(this.timeout);
            _builder.append(")");
          }
        }
        _builder.newLineIfNotEmpty();
      }
    }
    return _builder.toString();
  }
}
