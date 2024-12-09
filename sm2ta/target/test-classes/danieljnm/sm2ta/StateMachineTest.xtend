package danieljnm.sm2ta

import static org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.BeforeEach

class StateMachineTest {
	
	StateMachine stateMachine
	
	@BeforeEach
	def void reset() {
		stateMachine = new StateMachine()
	}
	
	@Test
	def void emptyMachineTest() {
		assertEquals(true, stateMachine.states.values.length == 0)
	}
	
	@Test
	def void oneStateTest() {
		stateMachine.state("Idle")
		assertEquals(1, stateMachine.states.values.length)
		assertEquals(true, stateMachine.states.get("Idle") !== null)
		
	}
	
	@Test
	def void initialStateTest() {
		stateMachine.state("Idle").initial
		var initialState = stateMachine.initialState
		assertNotNull(initialState)
		assertEquals("Idle", initialState.name)
	}
	
	@Test
	def void basicMachineWithOneTransition() {
		stateMachine
			.state("Idle").initial
			.transition("Ready", "Planning")
			.state("Planning")
		var transitions = stateMachine.state("Idle").transitions
		assertEquals(1, transitions.length)
		var transition = transitions.get(0)
		assertEquals("Planning", transition.target.name)
		assertEquals("Ready", transition.event)
	}
	
	@Test
	def void simpleMachineTest() {
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
		
		stateMachine.states.values.forEach[println(it)]
	}
}