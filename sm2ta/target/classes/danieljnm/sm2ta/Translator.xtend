package danieljnm.sm2ta

class Translator {
	static StateMachine stateMachine = new StateMachine()
	
	def static void main(String[] args) {
	}
	
	def static void buildStateMachine() {
		new StateMachine => [
			addState("Idle")
			addTransition("Idle", "Planning", "Ready")
			addTransition("Planning", "Idle", "Lost control")
			addTransition("Planning", "Next position", "Success")
			addTransition("Next position", "Idle", "Lost control")
		]
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
	
	def static addState(String name) {
		stateMachine.addState(name)
	}
	
	def static addTransition(String source, String target, String event) {
		stateMachine.addTransition(source, target, event)
	}
}