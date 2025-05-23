package danieljnm.sm2ta.Dto;

@SuppressWarnings("all")
public class TransitionDto {
  public String stateName;

  public String event;

  public String target;

  public String clientBehaviour;

  public String reactor;

  public boolean isReactorTransition() {
    return this.event.startsWith("EvAll");
  }

  public boolean requiresSuccess() {
    return this.event.startsWith("EvCbSuccess");
  }

  public boolean isTimer() {
    return this.event.startsWith("EvTimer");
  }

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
    int _indexOf = this.event.indexOf("<");
    int _plus = (_indexOf + 1);
    this.clientBehaviour = this.event.substring(_plus, this.event.indexOf(","));
    String _xifexpression = null;
    boolean _startsWith = this.event.startsWith("EvAll");
    if (_startsWith) {
      int _indexOf_1 = this.event.indexOf(",");
      int _plus_1 = (_indexOf_1 + 2);
      _xifexpression = this.event.substring(_plus_1, this.event.indexOf(">"));
    } else {
      _xifexpression = "";
    }
    this.reactor = _xifexpression;
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
}
