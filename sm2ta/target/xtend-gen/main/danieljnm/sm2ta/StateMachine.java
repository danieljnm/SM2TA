package danieljnm.sm2ta;

import java.util.List;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;

@SuppressWarnings("all")
public class StateMachine {
  private List<State> states;

  public StateMachine() {
    this.states = CollectionLiterals.<State>newArrayList();
  }

  public List<State> getStates() {
    return this.states;
  }
}
