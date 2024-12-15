package Uppaal

import java.util.List
import danieljnm.sm2ta.StateMachine.State
import danieljnm.sm2ta.StateMachine.Transition

class Process {
	public String name
	public List<State> states = newArrayList
	State initialState
	public List<Transition> transitions = newArrayList
	
	new(String name) {
		this.name = name
	}
	
	def addState(State state) {
		if (state.isInitial) {
			initialState = state
		}
		
		states.add(state)
	}
	
	def addTransition(Transition transition) {
		transitions.add(transition)
	}
	
	def getInitialState() {
		if (initialState === null) {
			return states.findFirst[]
		}
		
		initialState
	}
	
	override toString() {
		'''
		process «name» {
			«IF !states.empty»
			state
					«states.map[name].join(',\n')»;
			init «initialState.name»
			«ENDIF»
			«IF !transitions.empty»
			trans
					«transitions.join('\n')»
			«ENDIF»
		}
		'''
	}
}