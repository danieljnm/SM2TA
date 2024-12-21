package danieljnm.sm2ta.StateMachine

import java.util.List

class State {
	StateMachine stateMachine
	public int index
	public State parent
	public String name
	public List<Transition> transitions = newArrayList
	public List<State> nestedStates = newArrayList
	public boolean isInitial
	public boolean isNested
	
	new(State parent, String name) {
		this.parent = parent
		this.name = name
		this.isNested = true
	}
	
	new(StateMachine stateMachine, String name) {
		this.stateMachine = stateMachine
		this.name = name
		this.index = stateMachine.index++
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
	
	def State nesting((State) => void context) {
		context.apply(this)
		this
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
	
	def guard(String guard) {
		if (transitions.empty) {
			return this
		}
		
		transitions.lastOrNull.guard = guard
		this
	}
	
	def action(String action) {
		if (transitions.empty) {
			return this
		}
		
		transitions.lastOrNull.action = action
		this
	}
	
	def timeout(int timeout) {
		if (transitions.empty) {
			return this
		}
		
		transitions.lastOrNull.timeout = timeout
		this
	}
	
	def when(String when) {
		if (transitions.empty) {
			return this
		}
		
		transitions.lastOrNull.when = when
		this
	}
	
	def signal(String signal) {
		if (transitions.empty) {
			return this
		}
		
		transitions.lastOrNull.signal = signal
	}
	
	def initial() {
		isInitial = true
		this
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