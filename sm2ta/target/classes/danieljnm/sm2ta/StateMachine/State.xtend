package danieljnm.sm2ta.StateMachine

import java.util.List

class State {
	StateMachine stateMachine
	String name
	List<Transition> transitions = newArrayList
	List<State> nestedStates = newArrayList
	boolean isInitial
	boolean isNested
	
	new(String name) {
		this.name = name
		this.isNested = true
	}
	
	new(StateMachine stateMachine, String name) {
		this.stateMachine = stateMachine
		this.name = name
	}
	
	def State state(String name) {
		stateMachine.state(name)
	}
	
	def State nestedState(String name) {
		val nestedState = new State(name)
		nestedStates.add(nestedState)
		nestedState
	}
	
	
	def State nesting((State) => void configure) {
		configure.apply(this)
		this
	}
	
	def getName() {
		name
	}
	
	def setName(String name) {
		this.name = name
	}
	
	def getNestedStates() {
		nestedStates
	}
	
	def getTransitions() {
		transitions
	}
	
	def transition(String event) {
		transitions.add(new Transition(event, stateMachine.initialState))
		this
	}
	
	def transition(String event, String target) {
		if (isNested) {
			return this
		}
		
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
	
	def getIsNested() {
		isNested
	}
	
	def setIsNested(boolean isNested) {
		this.isNested = isNested
	}
	
	override toString() {
		'''
		State: «name»
		«IF transitions.length > 0»
		Transitions: «transitions.join()»
		«ENDIF»
		'''
	}
}