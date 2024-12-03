package danieljnm.sm2ta;

@SuppressWarnings("all")
public class Transition {
  private TransitionType type;

  private State target;

  public Transition(final TransitionType type, final State target) {
    this.type = type;
    this.target = target;
  }
}
