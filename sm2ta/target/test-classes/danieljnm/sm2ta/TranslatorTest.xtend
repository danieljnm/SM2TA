package danieljnm.sm2ta

import static org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.BeforeEach

class TranslatorTest {
	
	// TA states are similar to regular states in a state machine
	// TA transitions are triggered by events, conditions or guards
	// Clocks are variables that increase uniformly with time, that can be reset on transitions or states.
	// Clocks can be utilized to impose timing constraints on transitions
	// Guards (a condition must be satisfied before a transition can happen
	// Invariants are conditions on clocks that must hold while the system remains in a state
	// If an invariant is violated, the system must transition of out the state (a way to force a non deterministic automata to transition).
	// Urgent transitions are transitions that must occur as soon as their guards as satisfied without a delay
	// Deadlines specify a maximum allowed delay for transitions and actions
	
	@Test
	def void emptyTranslation() {
		
	}
	
	@Test
	def void basicTranslation() {
			
	}
}