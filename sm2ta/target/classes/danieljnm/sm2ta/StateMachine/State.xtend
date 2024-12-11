package danieljnm.sm2ta.StateMachine

import java.util.List

class State {
	StateMachine stateMachine
	State parent
	String name
	List<Transition> transitions = newArrayList
	List<State> nestedStates = newArrayList
	boolean isInitial
	boolean isNested
	
	new(State parent, String name) {
		this.parent = parent
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
		val existingState = nestedStates.findFirst[it.name == name]
		if (existingState !== null) {
			return existingState
		}
		
		val nestedState = new State(this, name)
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
		if (isNested) {
			transitions.add(new Transition(event, parent))
			return this
		}
		transitions.add(new Transition(event, stateMachine.initialState))
		this
	}
	
	def transition(String event, String target) {
		if (isNested) {
			var targetState = parent.nestedStates.findFirst[it.name === target]
			if (targetState === null) {
				targetState = new State(parent, target)
				parent.nestedStates.add(targetState)
			}
			transitions.add(new Transition(event, targetState))
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
	
	def toString(int depth) {
		'''
		State: «name»
		«IF transitions.length > 0»
		«" ".repeat(depth * 2)»Transitions: «transitions.join()»
		«ENDIF»
		'''
	}
}