package danieljnm.sm2ta.Dto;

import com.google.common.base.Objects;

@SuppressWarnings("all")
public class VariableDto {
  public String clientName;

  public String variable;

  public String type;

  public String value;

  public String convertedType() {
    String _lowerCase = this.type.toLowerCase();
    boolean _matched = false;
    if (Objects.equal(_lowerCase, "float")) {
      _matched=true;
      return "double";
    }
    if (!_matched) {
      if (Objects.equal(_lowerCase, "boolean")) {
        _matched=true;
        return "bool";
      }
    }
    if (!_matched) {
      boolean _startsWith = this.type.startsWith("queue");
      if (_startsWith) {
        _matched=true;
        return "int";
      }
    }
    return this.type.toLowerCase();
  }

  public String initializedValue() {
    String _xifexpression = null;
    if ((java.util.Objects.equals(this.value, "") && (this.type.startsWith("queue") || this.type.startsWith("list")))) {
      _xifexpression = "10";
    } else {
      _xifexpression = this.value;
    }
    return _xifexpression;
  }
}
