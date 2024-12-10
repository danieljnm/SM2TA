package danieljnm.sm2ta

import danieljnm.sm2ta.StateMachine.State
import danieljnm.sm2ta.StateMachine.StateMachine
import java.util.HashMap

class Printer {
	HashMap<String, State> visited = newHashMap
	
	def print(StateMachine stateMachine) {
		var initialState = stateMachine.initialState
		if (initialState === null) {
			stateMachine.states.values.forEach[println(it)]
			return
		}
		
		println(initialState)
		visited.put(initialState.name, initialState)
		initialState.print(0)
	}
	
	def void print(State state, int depth) {
		state.transitions.forEach[it |
			if (!visited.containsKey(it.target.name)) {
				visited.put(it.target.name, it.target)
				println(it.target)
				it.target.print(depth)
			}
		]
	}
}