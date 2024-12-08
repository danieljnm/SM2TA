package danieljnm.sm2ta

import java.util.List

class State {
	String name
	List<Transition> transitions = newArrayList
	List<State> nestedStates = newArrayList
	
	new(String name) {
		this.name = name
	}
	
	def getName() {
		name
	}
	
	def setName(String name) {
		this.name = name
	}
	
	def getTransitions() {
		transitions
	}
	
	def addTransition(String event, TransitionType type, State target) {
		transitions.add(new Transition(event, type, target))
		this
	}
	
	def removeTransition(Transition transition) {
		transitions.remove(transition)
		this
	}
	
	override toString() {
		'''
		State: «name»
		«IF transitions.length > 0»
		Transitions: «transitions.join(", ")»
		«ENDIF»
		«IF nestedStates.length > 0»
		Nedsted states: «nestedStates.join(", ")»
		«ENDIF»
		'''
	}
}