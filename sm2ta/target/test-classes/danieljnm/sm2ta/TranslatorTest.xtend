package danieljnm.sm2ta

import static org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.BeforeEach
import danieljnm.sm2ta.StateMachine.StateMachine

class TranslatorTest {
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
	def guardTransitions() {
		stateMachine.name("test")
			.state("one").initial
				.transition("event", "two").guard("false")
				.transition("event", "two").guard("true")
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
						guard 0;
					},
					one -> two {
						guard 1;
					};
			}
			system test;
			'''
		assertEquals(uppaal, stateMachine.toUppaal)
	}	
	
	@Test
	def timeoutTransition() {
		stateMachine.name("test")
			.state("one").initial
				.transition("event", "two")
			.state("two")
				.transition("event", "three").timeout(5).signal("finish")
			.state("three")
		val uppaal =
			'''
			clock gen_clock;
			chan finish;
			process test {
				state
					one,
					two {
						gen_clock <= 5
					},
					three;
				init one;
				trans
					one -> two {
						assign gen_clock := 0;
					},
					two -> three {
						guard gen_clock >= 5;
						sync finish!;
					};
			}
			process gen_sync_finish {
				state
					initSync;
				init initSync;
				trans
					initSync -> initSync {
						sync finish?;
					};
			}
			system test, gen_sync_finish;
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
		assertEquals(uppaal, stateMachine.toUppaal)
	}
	
	@Test
	def nestedMachineWithTransitions() {
		stateMachine.name("test")
			.state("one").initial
				.transition("event", "two")
			.state("two")
				.nesting[
					nestedState("innerOne")
						.transition("event", "innerTwo").signal("finish")
					nestedState("innerTwo")
				]
				.transition("event", "three").when("finish")
			.state("three")
		val uppaal = 
			'''
			chan finish, gen_two_inner_start;
			process test {
				state
					one,
					gen_pre_two,
					two,
					three;
				commit gen_pre_two;
				init one;
				trans
					one -> gen_pre_two {
					},
					two -> three {
						sync finish?;
					},
					gen_pre_two -> two {
						sync gen_two_inner_start!;
					};
			}
			process two_inner {
				state
					gen_init,
					innerOne,
					innerTwo;
				commit innerTwo;
				init gen_init;
				trans
					gen_init -> innerOne {
						sync gen_two_inner_start?;
					},
					innerOne -> innerTwo {
						sync finish!;
					},
					innerTwo -> gen_init {
					};
			}
			system test, two_inner;
			'''
		assertEquals(uppaal, stateMachine.toUppaal)
	}
	
	@Test
	def nestedMachineWithSignalsAndTimeout() {
		stateMachine.name("test")
			.state("one").initial
				.transition("event", "two").when("ready")
			.state("two")
				.nesting[
					nestedState("innerOne")
						.transition("event", "innerTwo").timeout(5).signal("finish")
					nestedState("innerTwo")
				]
				.transition("event", "one").when("finish")
		val uppaal = 
			'''
			clock gen_clock;
			chan ready, finish, gen_two_inner_start;
			process test {
				state
					one,
					gen_pre_two,
					two;
				commit gen_pre_two;
				init one;
				trans
					one -> gen_pre_two {
						sync ready?;
					},
					two -> one {
						sync finish?;
					},
					gen_pre_two -> two {
						sync gen_two_inner_start!;
					};
			}
			process two_inner {
				state
					gen_init,
					innerOne {
						gen_clock <= 5
					},
					innerTwo;
				commit innerTwo;
				init gen_init;
				trans
					gen_init -> innerOne {
						sync gen_two_inner_start?;
						assign gen_clock := 0;
					},
					innerOne -> innerTwo {
						guard gen_clock >= 5;
						sync finish!;
					},
					innerTwo -> gen_init {
					};
			}
			process gen_sync_ready {
				state
					initSync;
				init initSync;
				trans
					initSync -> initSync {
						sync ready!;
					};
			}
			system test, two_inner, gen_sync_ready;
			'''
		assertEquals(uppaal, stateMachine.toUppaal)
	}
}