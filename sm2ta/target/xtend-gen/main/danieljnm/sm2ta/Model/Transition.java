package danieljnm.sm2ta.Model;

@SuppressWarnings("all")
public class Transition {
  public String stateName;

  public String event;

  public String target;

  public String message() {
    String _lowerCase = this.event.toLowerCase();
    boolean _matched = false;
    boolean _contains = this.event.toLowerCase().contains("success");
    if (_contains) {
      _matched=true;
      return "Success";
    }
    if (!_matched) {
      boolean _contains_1 = this.event.toLowerCase().contains("failure");
      if (_contains_1) {
        _matched=true;
        return "Failure";
      }
    }
    return "default";
  }

  public void convert() {
    this.convertStateName();
    this.convertTargetName();
  }

  public void convertStateName() {
    final int index = this.stateName.indexOf("<");
    if ((index == (-1))) {
      return;
    }
    this.stateName = this.stateName.substring(0, index);
  }

  public void convertTargetName() {
    final int index = this.target.indexOf("<");
    if ((index == (-1))) {
      return;
    }
    this.target = this.target.substring(0, index);
  }

  public String getClientBehaviour() {
    int _indexOf = this.event.indexOf("<");
    int _plus = (_indexOf + 1);
    return this.event.substring(_plus, this.event.indexOf(","));
  }

  public String getReactor() {
    String _xifexpression = null;
    boolean _startsWith = this.event.startsWith("EvAll");
    if (_startsWith) {
      int _indexOf = this.event.indexOf(",");
      int _plus = (_indexOf + 2);
      _xifexpression = this.event.substring(_plus, this.event.indexOf(">"));
    } else {
      _xifexpression = "";
    }
    return _xifexpression;
  }
}
