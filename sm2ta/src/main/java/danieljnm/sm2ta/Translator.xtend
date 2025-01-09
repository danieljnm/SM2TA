package danieljnm.sm2ta

import danieljnm.sm2ta.StateMachine.StateMachine

class Translator {
	static StateMachine stateMachine = new StateMachine()
	
	def static void main(String[] args) {
		stateMachine = regular
		println(stateMachine.toUppaal)
	}
	
	def static regular() {
		stateMachine.name("FOD")
			.state("Idle").initial
				.transition("PositionAcquisition")
			.state("PositionAcquisition")
				.transition("GlobalPlanning").when("Ready")
				.transition("Idle").when("LostControl")
				.transition("Idle").when("Abort")
				.transition("Idle").when("FailedEstimation")
			.state("GlobalPlanning")
				.transition("NextPosition").when("Success")
				.transition("Idle").when("LostControl")
				.transition("Idle").when("Abort")
			.state("NextPosition")
				.transition("CaptureState").when("ContinueLoop")
				.transition("MissionCompleted").when("Success")
				.transition("Idle").when("LostControl")
			.state("CaptureState")
				.transition("ValidateState").when("Success")
				.transition("Idle").when("LostControl")
			.state("ValidateState")
				.transition("NextPosition").when("Success")
				.transition("Idle").when("LostControl")
			.state("MissionCompleted")
				.transition("Idle").when("Success")
				.transition("Idle").when("Abort")
				.transition("Idle").when("LostControl")
		stateMachine
	}
	
	def static withNesting() {
		stateMachine.name("FOD")
			.state("Idle").initial
				.transition("PositionAcquisition").when("Ready")
			.state("PositionAcquisition")
				.transition("GlobalPlanning")
				.transition("Idle").when("LostControl")
				.transition("Idle").when("Abort")
				.transition("Idle").when("FailedEstimation")
			.state("GlobalPlanning")
				.nesting[
					nestedState("NextPosition")
						.transition("CaptureState")
						.transition("MissionCompleted")
					nestedState("CaptureState")
						.transition("ValidateState")
						.transition("LostControl").signal("NestedLostControl")
					nestedState("ValidateState")
						.transition("NextPosition")
						.transition("LostControl").signal("NestedLostControl")
					nestedState("MissionCompleted")
						.transition("LostControl").signal("NestedLostControl")
						.transition("Abort").signal("NestedAbort")
						.transition("Success").signal("NestedSuccess")
					nestedState("Abort")
					nestedState("LostControl")
					nestedState("Success")
				]
				.transition("Idle").when("NestedLostControl")
				.transition("Idle").when("NestedAbort")
				.transition("Idle").when("NestedSuccess")
		stateMachine
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