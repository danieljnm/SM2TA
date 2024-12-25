package danieljnm.sm2ta.StateMachine;

import org.eclipse.xtend2.lib.StringConcatenation;

@SuppressWarnings("all")
public class Transition {
  public String event;

  public State target;

  public String guard;

  public String action;

  public int timeout;

  public String when;

  public String signal;

  public Transition(final String event, final State target) {
    this.event = event;
    this.target = target;
  }

  public CharSequence targetName(final boolean isParentNested) {
    CharSequence _xblockexpression = null;
    {
      if ((isParentNested || this.target.nestedStates.isEmpty())) {
        return this.target.name;
      }
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("gen_pre_");
      _builder.append(this.target.name);
      _xblockexpression = _builder;
    }
    return _xblockexpression;
  }

  public Object guardValue() {
    Object _switchResult = null;
    String _lowerCase = this.guard.toLowerCase();
    if (_lowerCase != null) {
      switch (_lowerCase) {
        case "true":
          _switchResult = Integer.valueOf(1);
          break;
        case "false":
          _switchResult = Integer.valueOf(0);
          break;
        default:
          _switchResult = this.guard;
          break;
      }
    } else {
      _switchResult = this.guard;
    }
    return _switchResult;
  }

  public Object stringToBool(final String value) {
    return null;
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
          if ((this.timeout > 0)) {
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
