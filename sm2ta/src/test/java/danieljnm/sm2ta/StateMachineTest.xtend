package danieljnm.sm2ta

import static org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.BeforeEach
import danieljnm.sm2ta.StateMachine.StateMachine

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
	def void basicMachineWithOneTransitionTest() {
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
	def void stateHasGuardTest() {
		val guard = "x > 1"
		stateMachine
			.state("Idle").initial
				.transition("Ready", "Planning").guard(guard)
		var transition = stateMachine.initialState.transitions.findFirst[it.event === "Ready"]
		assertNotNull(transition)
		assertEquals(guard, transition.guard)
	}
	
	@Test def void stateHasActionTest() {
		val action = "x = 0"
		stateMachine
			.state("Idle").initial
				.transition("Ready", "Planning").action(action)
		var transition = stateMachine.initialState.transitions.findFirst[it.event === "Ready"]
		assertNotNull(transition)
		assertEquals(action, transition.action)
	}
	
	@Test
	def void nestedMachineTest() {
		stateMachine
			.state("Idle").initial
				.nesting[
					nestedState("Testing").initial
						.transition("Processed", "Evaluating").guard("x > 1").action("x = 0")
					nestedState("Evaluating")
						.transition("Done")
				]
				.transition("Ready", "Planning")
			.state("Planning")
				.transition("Done").action("x = 0")
		
		var nestedStates = stateMachine.initialState.nestedStates
		assertEquals(2, nestedStates.length)
		
		var initialState = nestedStates.findFirst[it.isInitial]
		assertNotNull(initialState)		
		assertEquals("Testing", initialState.name)
		assertFalse(initialState.transitions.empty)
		
		var transition = initialState.transitions.get(0)
		assertEquals("Evaluating", transition.target.name)
		assertEquals("Processed", transition.event)
		assertEquals("x > 1", transition.guard)
		assertEquals("x = 0", transition.action)
		
		var printer = new Printer()
		printer.print(stateMachine)
	}
}