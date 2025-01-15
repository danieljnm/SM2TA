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
	def void emptyMachine() {
		stateMachine.name("Test")
		assertEquals(true, stateMachine.states.values.length == 0)
		assertEquals("Test", stateMachine.name)
	}
	
	@Test
	def void oneState() {
		stateMachine.state("Idle")
		assertEquals(1, stateMachine.states.values.length)
		assertEquals("Idle", stateMachine.states.values.get(0).name)
	}
	
	@Test
	def void initialState() {
		stateMachine.state("Idle").initial
		var initialState = stateMachine.initialState
		assertNotNull(initialState)
		assertEquals("Idle", initialState.name)
	}
	
	@Test
	def void transition() {
		stateMachine
			.state("Idle").initial
				.transition("Planning")
			.state("Planning")
		var transitions = stateMachine.initialState.transitions
		assertEquals(1, transitions.length)
		var transition = transitions.get(0)
		assertEquals("Planning", transition.target.name)
	}
	
	@Test
	def void transitionHasGuard() {
		val guard = "x > 1"
		stateMachine
			.state("Idle").initial
				.transition("Planning").guard(guard)
		var transition = stateMachine.initialState.transitions.get(0)
		assertNotNull(transition)
		assertEquals(guard, transition.guard)
	}
	
	@Test def void transitionHasAction() {
		val action = "x = 0"
		stateMachine
			.state("Idle").initial
				.transition("Planning").action(action)
		var transition = stateMachine.initialState.transitions.get(0)
		assertNotNull(transition)
		assertEquals(action, transition.action)
	}
	
	@Test def void transitionHasTimeout() {
		val timeout = 5
		stateMachine
			.state("Idle").initial
				.transition("Planning").timeout(timeout)
		var transition = stateMachine.initialState.transitions.get(0)
		assertNotNull(transition)
		assertEquals(timeout, transition.timeout)
	}
	
	@Test
	def void nestedMachineStates() {
		stateMachine
			.state("Idle").initial
				.nesting[
					nestedState("Testing").initial
						.transition("Evaluating").guard("x > 1").action("x = 0")
					nestedState("Evaluating")
				]
				.transition("Done")
		
		var nestedStates = stateMachine.initialState.nestedStates
		assertEquals(2, nestedStates.length)
		
		var initialState = nestedStates.findFirst[it.isInitial]
		assertNotNull(initialState)		
		assertEquals("Testing", initialState.name)
		assertFalse(initialState.transitions.empty)
	}
	
	@Test
	def void nestedMachineTransitions() {
		stateMachine
			.state("Idle").initial
				.nesting[
					nestedState("Testing").initial
						.transition("Evaluating").guard("x > 1").action("x = 0")
					nestedState("Evaluating")
						.transition("Done")
				]
				.transition("Planning")
			.state("Planning")
				.transition("Done")
		
		var nestedStates = stateMachine.initialState.nestedStates
		var initialState = nestedStates.findFirst[it.isInitial]
		var transition = initialState.transitions.get(0)
		assertEquals("Evaluating", transition.target.name)
		assertEquals("x > 1", transition.guard)
		assertEquals("x = 0", transition.action)
	}
	
	@Test
	def void variables() {
		stateMachine.name("test")
			.variables[
				variable("hasControl").type("bool").value("false")
			]
			.state("Idle").initial
				.transition("Planning").when("Ready").action("hasControl = true")
				.transition("Idle").when("LostControl").action("hasControl = false")
			.state("Planning")
		
		assertEquals(1, stateMachine.variables.length)
		var variable = stateMachine.variables.get(0)
		assertEquals("hasControl", variable.name)
		assertEquals("bool", variable.type)
		assertEquals("false", variable.value)
		var transitions = stateMachine.initialState.transitions
		assertEquals("hasControl = true", transitions.get(0).action)
		assertEquals("hasControl = false", transitions.get(1).action)
	}
}