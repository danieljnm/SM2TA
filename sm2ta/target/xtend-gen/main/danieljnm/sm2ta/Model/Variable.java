package danieljnm.sm2ta.Model;

@SuppressWarnings("all")
public class Variable {
  public String clientName;

  public String variable;

  public String type;

  public String value;

  public String convertedType() {
    String _lowerCase = this.type.toLowerCase();
    if (_lowerCase != null) {
      switch (_lowerCase) {
        case "float":
          return "double";
        case "boolean":
          return "bool";
        default:
          return this.type.toLowerCase();
      }
    } else {
      return this.type.toLowerCase();
    }
  }
}
