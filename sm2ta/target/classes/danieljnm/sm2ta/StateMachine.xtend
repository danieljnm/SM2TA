package danieljnm.sm2ta

import java.util.HashMap

class StateMachine {
	HashMap<String, State> states = newHashMap
	
	def getStates() {
		states
	}
	
	def State getInitialState() {
		states.values.findFirst[it.isInitial]
	}
	
	def state(String name) {
		states.computeIfAbsent(name) [new State(this, name)]
	}
}