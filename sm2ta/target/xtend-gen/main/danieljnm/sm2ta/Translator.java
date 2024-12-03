package danieljnm.sm2ta;

@SuppressWarnings("all")
public class Translator {
  private static StateMachine stateMachine = new StateMachine();

  public static void main(final String[] args) {
    StateMachine _stateMachine = new StateMachine();
    Translator.stateMachine = _stateMachine;
  }

  public static StateMachine getStateMachine() {
    return Translator.stateMachine;
  }
}
