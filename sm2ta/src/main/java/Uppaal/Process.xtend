package Uppaal

import java.util.List
import danieljnm.sm2ta.StateMachine.State
import danieljnm.sm2ta.StateMachine.Transition

class Process {
	public String name
	public List<State> states = newArrayList
	State initialState
	
	var x = 0
	var y = 0
	val increment = 100
	val spacing = 10
	
	new(String name) {
		this.name = name
	}
	
	def addState(State state) {
		if (state.isInitial) {
			initialState = state
		}
		
		states.add(state)
	}
	
	def getInitialState() {
		if (initialState === null) {
			return states.findFirst[]
		}
		
		initialState
	}
	
	def stateNames() {
		states.flatMap[it.nestedStates.empty ? #[it.format] : #['''gen_pre_«it.name»''', it.name]]
		.toSet
		.join(',\n')
	}
	
	def xmlStates() {
		x = 0
		y = 0
		states.flatMap[it.nestedStates.empty ? #[it.xmlFormat] : 
			#[
			'''
			<location id="gen_pre_«it.name»" x="«x»" y="«x»">
				<name x="«x - spacing»" y="«y + spacing»">gen_pre_«it.name»</name>
			</location>
			''', it.name
		].updateCoordinates[]]
		.toSet
		.join()
	}
	
	def List<String> updateCoordinates(List<String> strings, (Object)=>Object object) {
		x += increment
		strings
	}
	
	def String xmlFormat(State state) {
		var location =
		'''
		<location id="«state.name»" x="«x»" y="«y»">
			<name x="«x - spacing»" y="«y + spacing»">«state.name»</name>
		</location>
		'''
		x += increment
		location
	}
	
	def format(State state) {
		if (state.transitions.empty) {
			return state.name
		}
		
		val timeoutTransition = state.transitions.findFirst[timeout > 0]
		if (timeoutTransition !== null) {
			return '''
			«state.name» {
				gen_clock <= «timeoutTransition.timeout»
			}'''
		}
		
		state.name
	}
	
	def committedLocations() {
		nestedStateNames + signalTargets
	}
	
	def nestedStateNames() {
		states.filter[!nestedStates.empty]
		.map[it.name]
		.toSet
	}
	
	def signalTargets() {
		states.flatMap[transitions].filter[signal !== null && target.isNested]
		.map[target.name]
		.toSet
	}
	
	def signalTransitions() {
		states.flatMap[transitions].filter[signal !== null && target.isNested]
		.map[
		'''
		«target.name» -> gen_init {
		}'''
		]
		.toSet
	}
	
	def transitions() {
		(actualTransitions + nestedStateTransitions + signalTransitions).join(',\n')
	}
	
	def actualTransitions() {
		states.flatMap[state | state.transitions.map[transition | 
			'''
			«state.name» -> «transition.targetName(state.isNested)» {
				«IF transition.guard !== null»
					guard «transition.guardValue»;
				«ENDIF»
				«IF transition.timeout > 0»
					guard gen_clock >= «transition.timeout»;
				«ENDIF»
				«IF transition.signal !== null»
					sync «transition.signal»!;
				«ENDIF»
				«IF transition.when !== null»
					sync «transition.when»?;
				«ENDIF»
				«IF !transition.assignments.empty»
					assign «transition.assignments.join(', ')»;
				«ENDIF»
			}'''
			]]
	}
	
	def xmlTransitions() {
		states.flatMap[state | state.transitions.map[transition | 
			'''
			<source ref="«state.name»"/>
			<target ref="«transition.targetName(state.isNested)»"/>
			«IF transition.guard !== null»
				<label kind="guard">«transition.xmlGuard»</label>
			«ENDIF»
			«IF transition.timeout > 0»
				guard gen_clock &gt;= «transition.timeout»;
			«ENDIF»
			«IF transition.signal !== null»
				sync «transition.signal»!;
			«ENDIF»
			«IF transition.when !== null»
				sync «transition.when»?;
			«ENDIF»
			«IF !transition.assignments.empty»
				assign «transition.assignments.join(', ')»;
			«ENDIF»
			'''
			]]
	}
	
	def assignments(Transition transition) {
		var assigns = newArrayList
		if (transition.target.transitions.exists[timeout > 0])
			assigns.add('gen_clock := 0')
			
		if (transition.action !== null)
			assigns.add(transition.action)
		
		assigns
	}
	
	def nestedStateTransitions() {
		nestedStateNames.map[
			'''
			gen_pre_«it» -> «it» {
				sync gen_«it»_inner_start!;
			}'''
		]
	}
	
	override toString() {
		'''
		process «name» {
			«IF !states.empty»
			state
				«stateNames»;
			«IF !committedLocations.empty»
			commit «(nestedStateNames.map['''gen_pre_«it»''']+signalTargets).join(', ')»;
			«ENDIF»
			init «initialState.name»;
			«ENDIF»
			«IF states.exists[!transitions.empty] || !nestedStateNames.empty»
			trans
				«transitions»;
			«ENDIF»
		}
		'''
	}
	
	def toXml() {
		'''
		<template>
			<name>«name»</name>
			«IF xmlStates.length > 0»
			«xmlStates»
			«ENDIF»
			«IF states.exists[!transitions.empty] || !nestedStateNames.empty»
			«xmlTransitions»
			«ENDIF»
			«IF initialState !== null»
			<init ref="«initialState.name»"/>
			«ENDIF»
		</template>
		'''
	}
	
}