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
	def timeoutTransition() {
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
		println(stateMachine.toUppaal)
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
						.transition("event", "innerTwo").when("test")
					nestedState("innerTwo")
				]
		val uppaal = 
		'''
		chan test, gen_two_inner_start;
		process test {
			state
				one,
				gen_pre_two,
				two;
			commit gen_pre_two;
			init one;
			trans
				one -> gen_pre_two {
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
			init gen_init;
			trans
				gen_init -> innerOne {
					sync gen_two_inner_start?;
				},
				innerOne -> innerTwo {
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
		system test, two_inner, gen_sync_test;
		'''
		assertEquals(uppaal, stateMachine.toUppaal)
	}
}