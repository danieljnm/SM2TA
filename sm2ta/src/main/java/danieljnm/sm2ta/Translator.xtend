package danieljnm.sm2ta

import danieljnm.sm2ta.StateMachine.StateMachine

class Translator {
	static StateMachine stateMachine = new StateMachine()
	
	def static void main(String[] args) {
		stateMachine.name("FOD")
			.state("Idle").initial
				.transition("PositionAcquisition")
			.state("PositionAcquisition")
				.transition("GlobalPlanning")
				.transition("LostControl")
			.state("GlobalPlanning")
				.transition("NextPosition")
				.transition("LostControl")
			.state("NextPosition")
				.transition("CaptureState")
				.transition("MissionCompleted")
				.transition("LostControl")
			.state("CaptureState")
				.transition("ValidateState")
				.transition("LostControl")
			.state("ValidateState")
				.transition("NextPosition")
				.transition("LostControl")
			.state("MissionCompleted")
				.transition("Success")
		
		var printer = new Printer()
		printer.print(stateMachine)
		println(stateMachine.toUppaal)
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