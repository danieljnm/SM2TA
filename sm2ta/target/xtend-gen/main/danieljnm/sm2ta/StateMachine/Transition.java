package danieljnm.sm2ta.StateMachine;

import java.util.ArrayList;
import java.util.Collections;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.Functions.Function2;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ListExtensions;
import org.eclipse.xtext.xbase.lib.StringExtensions;

@SuppressWarnings("all")
public class Transition {
  public State target;

  public String guard;

  public String action;

  public int timeout;

  public String when;

  public String signal;

  public int x;

  public int y;

  private int spacing = 15;

  public Transition(final State target) {
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

  public boolean hasGuard() {
    boolean _xblockexpression = false;
    {
      if (((this.guard != null) && (!StringExtensions.isNullOrEmpty(this.guard)))) {
        int _y = this.y;
        this.y = (_y + this.spacing);
      }
      _xblockexpression = ((this.guard != null) && (!StringExtensions.isNullOrEmpty(this.guard)));
    }
    return _xblockexpression;
  }

  public boolean hasTimeout() {
    boolean _xblockexpression = false;
    {
      if ((this.timeout > 0)) {
        int _y = this.y;
        this.y = (_y + this.spacing);
      }
      _xblockexpression = (this.timeout > 0);
    }
    return _xblockexpression;
  }

  public boolean hasSignal() {
    boolean _xblockexpression = false;
    {
      if ((this.signal != null)) {
        int _y = this.y;
        this.y = (_y + this.spacing);
      }
      _xblockexpression = (this.signal != null);
    }
    return _xblockexpression;
  }

  public boolean hasWhen() {
    boolean _xblockexpression = false;
    {
      if ((this.when != null)) {
        int _y = this.y;
        this.y = (_y + this.spacing);
      }
      _xblockexpression = (this.when != null);
    }
    return _xblockexpression;
  }

  public boolean hasAssignment() {
    boolean _xblockexpression = false;
    {
      boolean _isEmpty = this.assignments().isEmpty();
      boolean _not = (!_isEmpty);
      if (_not) {
        int _y = this.y;
        this.y = (_y + this.spacing);
      }
      boolean _isEmpty_1 = this.assignments().isEmpty();
      _xblockexpression = (!_isEmpty_1);
    }
    return _xblockexpression;
  }

  public ArrayList<String> assignments() {
    ArrayList<String> _xblockexpression = null;
    {
      ArrayList<String> assigns = CollectionLiterals.<String>newArrayList();
      final Function1<Transition, Boolean> _function = (Transition it) -> {
        return Boolean.valueOf((it.timeout > 0));
      };
      boolean _exists = IterableExtensions.<Transition>exists(this.target.transitions, _function);
      if (_exists) {
        assigns.add("gen_clock := 0");
      }
      if (((this.action != null) && (!StringExtensions.isNullOrEmpty(this.action)))) {
        assigns.add(this.action);
      }
      _xblockexpression = assigns;
    }
    return _xblockexpression;
  }

  public Integer properties() {
    final Function1<Boolean, Integer> _function = (Boolean it) -> {
      int _xifexpression = (int) 0;
      if ((it).booleanValue()) {
        _xifexpression = 1;
      } else {
        _xifexpression = 0;
      }
      return Integer.valueOf(_xifexpression);
    };
    final Function2<Integer, Integer, Integer> _function_1 = (Integer value, Integer next) -> {
      return Integer.valueOf(((value).intValue() + (next).intValue()));
    };
    return IterableExtensions.<Integer>reduce(ListExtensions.<Boolean, Integer>map(Collections.<Boolean>unmodifiableList(CollectionLiterals.<Boolean>newArrayList(Boolean.valueOf(((this.guard != null) && (!StringExtensions.isNullOrEmpty(this.guard)))), Boolean.valueOf((this.timeout > 0)), Boolean.valueOf((this.signal != null)), Boolean.valueOf((this.when != null)))), _function), _function_1);
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

  public String xmlGuard() {
    return this.guardValue().toString().replace("<", "&lt;").replace(">", "&gt;");
  }

  @Override
  public String toString() {
    StringConcatenation _builder = new StringConcatenation();
    {
      if ((this.target != null)) {
        _builder.append("-> ");
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
