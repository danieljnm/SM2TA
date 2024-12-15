package Uppaal

import java.util.List
import danieljnm.sm2ta.StateMachine.State
import danieljnm.sm2ta.StateMachine.Transition

class Process {
	public String name
	public List<State> states = newArrayList
	State initialState
	
	new(String name) {
		this.name = name
	}
	
	def addState(State state) {
		if (state.isInitial) {
			initialState = state
		}
		
		states.add(state)
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
			init «initialState.name»;
			«ENDIF»
			«IF states.exists[!transitions.empty]»
			trans
					«states.flatMap[transitions.map[transition | 
					'''
					«name» -> «transition.target.name» {
					};
					'''
					]].join('\n')»
			«ENDIF»
		}
		'''
	}
}