package danieljnm.sm2ta

import danieljnm.sm2ta.StateMachine.StateMachine

class Translator {
	static StateMachine stateMachine = new StateMachine()
	
	def static void main(String[] args) {
	}
	
	def static getStateMachine() {
		stateMachine
	}
	
	def static reset() {
		stateMachine = new StateMachine()
	}

	def static printMachine() {
		stateMachine.states.values.forEach[println(it)]
	}
}