package danieljnm.sm2ta;

@SuppressWarnings("all")
public class Transition {
  public String state;

  public String event;

  public String target;

  public String message() {
    String _lowerCase = this.event.toLowerCase();
    boolean _matched = false;
    boolean _contains = this.event.toLowerCase().contains("success");
    if (_contains) {
      _matched=true;
      return "success";
    }
    if (!_matched) {
      boolean _contains_1 = this.event.toLowerCase().contains("failure");
      if (_contains_1) {
        _matched=true;
        return "error";
      }
    }
    return "default";
  }
}
