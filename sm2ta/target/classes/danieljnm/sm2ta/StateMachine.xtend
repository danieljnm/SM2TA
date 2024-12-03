package danieljnm.sm2ta

import java.util.List

class StateMachine {
	List<State> states
	
	new() {
		states = newArrayList
	}
	
	def getStates() {
		states
	}
	
	def addState(String name) {
		states.add(new State(name))
	}
	
}