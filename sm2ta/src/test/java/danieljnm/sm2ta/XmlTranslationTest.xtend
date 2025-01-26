package danieljnm.sm2ta

import static org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.BeforeEach
import danieljnm.sm2ta.StateMachine.StateMachine

class XmlTranslationTest {
	StateMachine stateMachine
	
	@BeforeEach
	def void reset() {
		stateMachine = new StateMachine()
	}
	
	@Test
	def void emptyMachine() {
		stateMachine.name("test")
		val xml = 
		'''
		<?xml version="1.0" encoding="utf-8"?>
		<nta>
		<declaration>
		</declaration>
		<template>
			<name>test</name>
		</template>
		<system>
			test
		</system>
		</nta>
		'''
		assertEquals(xml, stateMachine.toXml)
	}
	
	@Test
	def void states() {
		stateMachine.name("test")
			.state("one").initial
			.state("two")
		val xml = 
		'''
		<?xml version="1.0" encoding="utf-8"?>
		<nta>
		<declaration>
		</declaration>
		<template>
			<name>test</name>
			<location id="one" x="0" y="0">
				<name x="-15" y="15">one</name>
			</location>
			<location id="two" x="400" y="0">
				<name x="385" y="15">two</name>
			</location>
			<init ref="one"/>
		</template>
		<system>
			test
		</system>
		</nta>
		'''
		println(stateMachine.toXml)
		assertEquals(xml, stateMachine.toXml)
	}
	
	@Test
	def transitions() {
		stateMachine.name("test")
			.state("one").initial
				.transition("two").guard("false").when("test")
				.transition("two").guard("true").when("test")
			.state("two")
				.transition("three").timeout(5).signal("finish")
		val uppaal =
			'''
			<?xml version="1.0" encoding="utf-8"?>
			<nta>
			<declaration>
			clock gen_clock;
			chan test, finish;
			</declaration>
			<template>
				<name>test</name>
				<location id="one" x="0" y="0">
					<name x="-15" y="15">one</name>
				</location>
				<location id="two" x="400" y="0">
					<name x="385" y="15">two</name>
					<label kind="invariant" x="385" y="30">gen_clock &lt;= 5</label>
				</location>
				<location id="three" x="800" y="0">
					<name x="785" y="15">three</name>
				</location>
				<init ref="one"/>
				<transition>
					<source ref="one"/>
					<target ref="two"/>
					<label kind="guard" x="150" y="15">0</label>
					<label kind="synchronisation" x="150" y="30">test?</label>
					<label kind="assignment" x="150" y="45">gen_clock := 0</label>
				</transition>
				<transition>
					<source ref="one"/>
					<target ref="two"/>
					<label kind="guard" x="150" y="60">1</label>
					<label kind="synchronisation" x="150" y="75">test?</label>
					<label kind="assignment" x="150" y="90">gen_clock := 0</label>
				</transition>
				<transition>
					<source ref="two"/>
					<target ref="three"/>
					<label kind="guard" x="550" y="15">gen_clock &gt;= 5</label>
					<label kind="synchronisation" x="550" y="30">finish!</label>
				</transition>
			</template>
			<template>
				<name>gen_sync_test</name>
				<location id="initSync" x="0" y="0">
					<name x="-30" y="15">initSync</name>
				</location>
				<transition>
					<source ref="initSync"/>
					<target ref="initSync"/>
					<label kind="synchronisation" x="-25" y="-55">test!</label>
				</transition>
			</template>
			<template>
				<name>gen_sync_finish</name>
				<location id="initSync" x="0" y="0">
					<name x="-30" y="15">initSync</name>
				</location>
				<transition>
					<source ref="initSync"/>
					<target ref="initSync"/>
					<label kind="synchronisation" x="-25" y="-55">finish?</label>
				</transition>
			</template>
			<system>
				test, gen_sync_test, gen_sync_finish
			</system>
			</nta>
			'''
		assertEquals(uppaal, stateMachine.toXml)
	}
}