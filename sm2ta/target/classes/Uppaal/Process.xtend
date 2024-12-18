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
	
	def transitions() {
		(actualTransitions + nestedStateTransitions).join(',\n')
	}
	
	def actualTransitions() {
		states.flatMap[state | state.transitions.map[transition | 
			'''
			«state.name» -> «transition.targetName(state.isNested)» {
				«IF transition.when !== null»
					sync «transition.when»?;
				«ENDIF»
			}'''
			]]
	}
	
	def nestedStateTransitions() {
		nestedStateNames.map[
			'''
			gen_pre_«it» -> «it» {
				sync gen_«it»_inner_start!;
			}'''
		]
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
				«transitions»;
			«ENDIF»
		}
		'''
	}
}