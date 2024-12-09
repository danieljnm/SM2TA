package danieljnm.sm2ta

import java.util.List

class State {
	StateMachine stateMachine
	String name
	List<Transition> transitions = newArrayList
	List<State> nestedStates = newArrayList
	boolean isInitial
	
	// TODO: Might want to keep an order for iteration purposes.
	// It is always possible to just follow the flow from the Initial state and go from there
	
	new(StateMachine stateMachine, String name) {
		this.stateMachine = stateMachine
		this.name = name
	}
	
	def State state(String name) {
		stateMachine.state(name)
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
	
	def transition(String event) {
		transitions.add(new Transition(event, stateMachine.initialState))
		this
	}
	
	def transition(String event, String target) {
		var targetState = stateMachine.state(target)
		transitions.add(new Transition(event, targetState))
		this
	}
	
	def removeTransition(Transition transition) {
		transitions.remove(transition)
		this
	}
	
	def initial() {
		isInitial = true
		this
	}
	
	def getIsInitial() {
		isInitial
	}
	
	override toString() {
		'''
		State: «name»
		«IF transitions.length > 0»
		Transitions: «transitions.join()»
		«ENDIF»
		«IF nestedStates.length > 0»
		Nedsted states: «nestedStates.join(", ")»
		«ENDIF»
		'''
	}
}