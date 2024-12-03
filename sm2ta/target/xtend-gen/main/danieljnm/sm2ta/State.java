package danieljnm.sm2ta;

import java.util.List;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;

@SuppressWarnings("all")
public class State {
  private String name;

  private List<Transition> transitions;

  public State(final String name) {
    this.name = name;
    this.transitions = CollectionLiterals.<Transition>newArrayList();
  }

  public List<Transition> getTransitions() {
    return this.transitions;
  }

  public boolean addTransition(final TransitionType type, final State target) {
    Transition _transition = new Transition(type, target);
    return this.transitions.add(_transition);
  }

  public boolean removeTransition(final Transition transition) {
    return this.transitions.remove(transition);
  }
}
