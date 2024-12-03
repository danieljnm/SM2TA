package danieljnm.sm2ta

import static org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class TranslatorTest {
	@Test
	def void emptyMachineTest () {
		assertEquals(true, Translator.stateMachine.states.length == 0)
	}
}