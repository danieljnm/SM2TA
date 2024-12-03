package danieljnm.sm2ta;

import java.util.List;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;

@SuppressWarnings("all")
public class State {
  private List<Transition> transitions;

  public State() {
    this.transitions = CollectionLiterals.<Transition>newArrayList();
  }

  public List<Transition> getTransitions() {
    return this.transitions;
  }
}
