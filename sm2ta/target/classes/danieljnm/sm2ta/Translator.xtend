package danieljnm.sm2ta

import danieljnm.sm2ta.StateMachine.StateMachine

class Translator {
	static StateMachine stateMachine = new StateMachine()
	
	def static void main(String[] args) {
		stateMachine
			.state("Idle").initial
				.transition("Ready", "Position acquisition")
			.state("Position acquisition")
				.transition("Systems ready", "Global planning")
				.transition("Lost control")
			.state("Global planning")
				.transition("Success", "Next position")
				.transition("Lost control")
			.state("Next position")
				.transition("Continue loop", "Capture state")
				.transition("Done", "Mission completed")
				.transition("Lost control")
			.state("Capture state")
				.transition("Success", "Validate state")
				.transition("Lost control")
			.state("Validate state")
				.transition("Success", "Next position")
				.transition("Lost control")
			.state("Mission completed")
				.transition("Success")
		
		var printer = new Printer()
		printer.print(stateMachine)
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