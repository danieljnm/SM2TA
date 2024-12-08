package danieljnm.sm2ta

import static org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.BeforeEach

class TranslatorTest {
	
	@BeforeEach
	def void reset() {
		Translator.reset
	}
	
	@Test
	def void emptyMachineTest () {
		assertEquals(true, Translator.stateMachine.states.values.length == 0)
	}
	
	@Test
	def void basicMachineOneState() {
		Translator.stateMachine.addState("Idle")
		assertEquals(true, Translator.stateMachine.states.values.length == 1)
		assertEquals(true, Translator.stateMachine.states.get("Idle") !== null)
		
	}
	
	@Test
	def void basicMachineWithOneTransition() {
		Translator.addState("Idle")
		Translator.addTransition("Idle", "Planning", "Ready")
		assertEquals(true, Translator.stateMachine.states.values.length == 2)
		assertEquals(true, Translator.stateMachine.states.get("Idle").transitions.length == 1)
	}
}