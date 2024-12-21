package Uppaal

import java.util.List
import danieljnm.sm2ta.StateMachine.State

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
	
	def committedLocations() {
		nestedStateNames + signalTargets
	}
	
	def nestedStateNames() {
		states.filter[!nestedStates.empty]
		.map[it.name]
		.toSet
	}
	
	def signalTargets() {
		states.flatMap[transitions].filter[signal !== null]
		.map[target.name]
		.toSet
	}
	
	def signalTransitions() {
		states.flatMap[transitions].filter[signal !== null]
		.map[
		'''
		«target.name» -> gen_init {
		}'''
		]
		.toSet
	}
	
	def transitions() {
		(actualTransitions + nestedStateTransitions + signalTransitions).join(',\n')
	}
	
	def actualTransitions() {
		states.flatMap[state | state.transitions.map[transition | 
			'''
			«state.name» -> «transition.targetName(state.isNested)» {
				«IF transition.guard !== null»
					gen_clock <= «transition.guard»
				«ENDIF»
				«IF transition.when !== null»
					sync «transition.when»?;
				«ENDIF»
				«IF transition.signal !== null»
					sync «transition.signal»!;
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
			«IF !committedLocations.empty»
			commit «(nestedStateNames.map['''gen_pre_«it»''']+signalTargets).join(', ')»;
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