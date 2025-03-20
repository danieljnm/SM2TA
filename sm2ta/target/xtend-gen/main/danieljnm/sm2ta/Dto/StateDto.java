package danieljnm.sm2ta.Dto;

@SuppressWarnings("all")
public class StateDto {
  public String stateName;

  public String namespace;

  public boolean initial;

  public boolean nestedInitial;

  public String actions;

  public String convert(final String value) {
    String _xblockexpression = null;
    {
      final int index = value.indexOf("<");
      if ((index == (-1))) {
        return value;
      }
      _xblockexpression = value.substring(0, index);
    }
    return _xblockexpression;
  }
}
