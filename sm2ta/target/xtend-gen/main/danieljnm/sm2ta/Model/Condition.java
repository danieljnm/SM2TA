package danieljnm.sm2ta.Model;

@SuppressWarnings("all")
public class Condition {
  public String clientBehaviour;

  public boolean requiresSuccess;

  public Condition(final StateReactor reactor) {
    int _indexOf = reactor.event.indexOf("<");
    int _plus = (_indexOf + 1);
    this.clientBehaviour = reactor.event.substring(_plus, reactor.event.indexOf(","));
    this.requiresSuccess = reactor.event.startsWith("EvCbSuccess");
  }
}
