package danieljnm.sm2ta.Dto;

@SuppressWarnings("all")
public class ConditionDto {
  public String clientBehaviour;

  public boolean requiresSuccess;

  public ConditionDto(final StateReactorDto reactor) {
    int _indexOf = reactor.event.indexOf("<");
    int _plus = (_indexOf + 1);
    this.clientBehaviour = reactor.event.substring(_plus, reactor.event.indexOf(","));
    this.requiresSuccess = reactor.event.startsWith("EvCbSuccess");
  }
}
