package danieljnm.sm2ta

import java.util.List

class State {
	List<Transition> transitions
	
	new() {
		transitions = newArrayList
	}
	
	def getTransitions() {
		transitions
	}
}