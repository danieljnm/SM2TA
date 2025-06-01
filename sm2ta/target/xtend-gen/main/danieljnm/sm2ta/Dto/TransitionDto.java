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
    boolean _contains = this.event.contains("<");
    if (_contains) {
      String _xifexpression = null;
      boolean _startsWith = this.event.startsWith("EvLoopEnd");
      if (_startsWith) {
        int _indexOf = this.event.indexOf("<");
        int _plus = (_indexOf + 1);
        _xifexpression = this.event.substring(_plus);
      } else {
        int _indexOf_1 = this.event.indexOf("<");
        int _plus_1 = (_indexOf_1 + 1);
        _xifexpression = this.event.substring(_plus_1, this.event.indexOf(","));
      }
      this.clientBehaviour = _xifexpression;
    }
    String _xifexpression_1 = null;
    boolean _startsWith_1 = this.event.startsWith("EvAll");
    if (_startsWith_1) {
      int _indexOf_2 = this.event.indexOf(",");
      int _plus_2 = (_indexOf_2 + 2);
      _xifexpression_1 = this.event.substring(_plus_2, this.event.indexOf(">"));
    } else {
      _xifexpression_1 = "";
    }
    this.reactor = _xifexpression_1;
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
