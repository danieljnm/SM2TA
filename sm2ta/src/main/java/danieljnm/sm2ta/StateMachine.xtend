package danieljnm.sm2ta

import java.util.HashMap

class StateMachine {
	HashMap<String, State> states = newHashMap
	
	def getStates() {
		states
	}
	
	def addState(String name) {
		states.computeIfAbsent(name) [new State(name)]
	}
	
	def addTransition(String source, String target, String event) {
		var targetState = addState(target)
		addState(source).addTransition(event, TransitionType.Direct, targetState)
	}
}