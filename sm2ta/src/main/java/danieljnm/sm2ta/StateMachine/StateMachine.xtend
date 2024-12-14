package danieljnm.sm2ta.StateMachine

import java.util.HashMap

class StateMachine {
	public String name
	public HashMap<String, State> states = newHashMap
	
	def name(String name) {
		this.name = name
	}
	
	def State getInitialState() {
		states.values.findFirst[it.isInitial]
	}
	
	def State state(String name) {
		states.computeIfAbsent(name) [new State(this, name)]
	}
	
	def String toUppaal() {
		'''
			process «name» {
			}
			system «name»;
		'''
	}
}