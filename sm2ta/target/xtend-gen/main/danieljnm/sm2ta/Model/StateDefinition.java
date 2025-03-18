package danieljnm.sm2ta.Model;

@SuppressWarnings("all")
public class StateDefinition {
  public String stateName;

  public String namespace;

  public boolean initial;

  public boolean nestedInitial;

  public String actions;

  public void convertName() {
    final int index = this.stateName.indexOf("<");
    if ((index == (-1))) {
      return;
    }
    this.stateName = this.stateName.substring(0, index);
  }
}
