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
	
	def stateNames() {
		states.flatMap[it.nestedStates.empty ? #[it.name] : #['''gen_pre_«it.name»''', it.name]]
		.toSet
		.join(',\n')
	}
	
	def nestedStateNames() {
		states.filter[!nestedStates.empty]
		.map[it.name]
		.toSet
	}
	
	override toString() {
		'''
		process «name» {
			«IF !states.empty»
			state
				«stateNames»;
			«IF !nestedStateNames.empty»
			commit «nestedStateNames.map['''gen_pre_«it»'''].join(', ')»;
			«ENDIF»
			init «initialState.name»;
			«ENDIF»
			«IF states.exists[!transitions.empty] || !nestedStateNames.empty»
			trans
				«states.flatMap[transitions.map[transition | 
				'''
				«name» -> «transition.target.name» {
					«IF transition.when !== null»
						sync «transition.when»?;
					«ENDIF»
				};
				'''
				]].join('\n')»
				«FOR nestedState : nestedStateNames»
				gen_pre_«nestedState» -> «nestedState» {
					sync gen_«nestedState»_inner_start!;
				};
				«ENDFOR»
			«ENDIF»
		}
		'''
	}
}