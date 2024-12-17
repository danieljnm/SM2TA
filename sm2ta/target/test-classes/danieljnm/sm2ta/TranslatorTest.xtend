package danieljnm.sm2ta

import static org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.BeforeEach
import danieljnm.sm2ta.StateMachine.StateMachine

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
	
	StateMachine stateMachine
	
	@BeforeEach
	def void reset() {
		stateMachine = new StateMachine()
	}
	
	@Test
	def void emptyMachine() {
		stateMachine.name("test")
		val uppaal = 
		'''
			process test {
			}
			system test;
		'''
		assertEquals(uppaal, stateMachine.toUppaal)
	}
	
	@Test
	def void states() {
		stateMachine.name("test")
			.state("one").initial
			.state("two")
		val uppaal = 
			'''
			process test {
				state
					one,
					two;
				init one;
			}
			system test;
			'''
		assertEquals(uppaal, stateMachine.toUppaal)
	}
	
	@Test
	def void transitions() {
		stateMachine.name("test")
			.state("one").initial
				.transition("event", "two")
			.state("two")
		val uppaal = 
			'''
			process test {
				state
					one,
					two;
				init one;
				trans
					one -> two {
					};
			}
			system test;
			'''
		assertEquals(uppaal, stateMachine.toUppaal)
	}
	
	@Test
	def actionTransition() {
		stateMachine.name("test")
			.state("one").initial
				.transition("event", "two").when("test")
			.state("two")
		val uppaal =
			'''
			chan test;
			process test {
				state
					one,
					two;
				init one;
				trans
					one -> two {
						sync test?;
					};
			}
			process gen_sync_test {
				state
					initSync;
				init initSync;
				trans
					initSync -> initSync {
						sync test!;
					};
			}
			system test, gen_sync_test;
			'''
		assertEquals(uppaal, stateMachine.toUppaal)
	}
	
	@Test
	def nestedMachine() {
		stateMachine.name("test")
			.state("one").initial
			.state("two")
				.nesting[
					nestedState("three")
					nestedState("four")
				]
		val uppaal = 
		'''
		chan gen_two_inner_start;
		process test {
			state
				one,
				gen_pre_two,
				two;
			commit gen_pre_two;
			init one;
			trans
				gen_pre_two -> two {
					sync gen_two_inner_start!;
				};
		}
		process two_inner {
			state
				gen_init,
				three,
				four;
			init gen_init;
			trans
				gen_init -> three {
					sync gen_two_inner_start?;
				};
		}
		system test, two_inner;
		'''
		println(stateMachine.toUppaal)
		assertEquals(uppaal, stateMachine.toUppaal)
	}
}