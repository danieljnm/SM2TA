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
		
		initialState.print(0)
	}
	
	def void print(State state, int depth) {
		if (visited.containsKey(state.name)) {
			return
		}
		visited.put(state.name, state)
		
		println(" ".repeat(depth * 2) + state.toString(depth))
		
		if (!state.nestedStates.empty) {
			println(" ".repeat(depth * 2) + "Nested states:")
			state.nestedStates.forEach[it |
				it.print(depth + 1)
			]
		}
		
		state.transitions.forEach[it |
			it.target.print(depth)
		]
	}
}