package danieljnm.sm2ta

import java.util.List

class State {
	String name
	List<Transition> transitions
	
	new(String name) {
		this.name = name
		transitions = newArrayList
	}
	
	def getTransitions() {
		transitions
	}
	
	def addTransition(TransitionType type, State target) {
		transitions.add(new Transition(type, target))
	}
	
	def removeTransition(Transition transition) {
		transitions.remove(transition)
	}
}