package danieljnm.sm2ta.StateMachine

import java.util.HashMap

class StateMachine {
	HashMap<String, State> states = newHashMap
	
	def getStates() {
		states
	}
	
	def State getInitialState() {
		states.values.findFirst[it.isInitial]
	}
	
	def State state(String name) {
		states.computeIfAbsent(name) [new State(this, name)]
	}
}