package danieljnm.sm2ta.Model;

import org.eclipse.xtend2.lib.StringConcatenation;

@SuppressWarnings("all")
public class Function {
  public String clientName;

  public String function;

  public String expression;

  public String defaultValue;

  public CharSequence convertedExpression(final boolean requiresSuccess) {
    CharSequence _xifexpression = null;
    boolean _contains = this.expression.contains(".empty()");
    if (_contains) {
      StringConcatenation _builder = new StringConcatenation();
      {
        if ((!requiresSuccess)) {
          _builder.append("!(");
        }
      }
      String _replace = this.expression.replace(".empty()", " == 0");
      _builder.append(_replace);
      {
        if ((!requiresSuccess)) {
          _builder.append(")");
        }
      }
      _xifexpression = _builder;
    } else {
      StringConcatenation _builder_1 = new StringConcatenation();
      {
        if ((!requiresSuccess)) {
          _builder_1.append("!(");
        }
      }
      _builder_1.append(this.expression);
      {
        if ((!requiresSuccess)) {
          _builder_1.append(")");
        }
      }
      _xifexpression = _builder_1;
    }
    return _xifexpression;
  }

  public String assignment() {
    return this.expression.replace("=", ":=");
  }
}
