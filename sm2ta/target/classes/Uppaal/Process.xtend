package Uppaal

import java.util.List
import danieljnm.sm2ta.StateMachine.State

class Process {
	public String name
	public List<State> states = newArrayList
	State initialState
	int spacing = 15
	int increment = 150
	int currentY = 0
	
	new(String name) {
		this.name = name
	}
	
	def addState(State state) {
		currentY = 0
		if (state.isInitial) {
			initialState = state
		}
		
		states.add(state)
		state.transitions.forEach[transition, index |
			transition.x = state.x + increment
			if (index == 0) {
				transition.y = state.y
				currentY += spacing * transition.properties
        		return
			}
			transition.y = currentY + spacing
       		currentY = transition.y + spacing * transition.properties
		]
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
		states.flatMap[it.nestedStates.empty ? #[it.xmlFormat] : 
			#[
			'''
			<location id="gen_pre_«it.name»" x="«x»" y="«y»" committed="true">
				<name x="«x - spacing»" y="«y + spacing»">gen_pre_«it.name»</name>
			</location>
			''',
			'''
			<location id="«it.name»" x="«x + 400»" y="«y»">
				<name x="«x - spacing + 400»" y="«y + spacing»">«it.name»</name>
			</location>
			'''
		]]
		.toSet
		.join()
	}
	
	def String xmlFormat(State state) {
		var location =
		'''
		<location id="«state.name»" x="«state.x»" y="«state.y»">
			<name x="«state.x - spacing»" y="«state.y + spacing»">«state.name»</name>
			«state.labels»
		</location>
		'''
		location
	}
	
	def labels(State state) {
		state.transitions.filter[timeout > 0].toList.map[
			'''
			<label kind="invariant" x="«x - spacing - increment»" y="«y + spacing * 2»">gen_clock &lt;= «timeout»</label>
			'''
		]
		.join()
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
	
	def transitions() {
		(actualTransitions + nestedStateTransitions(false) + signalTransitions(false)).join(',\n')
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
	
	def nestedStateTransitions(boolean xml) {
		nestedStateNames.map[
			if (!xml) {
				return '''
				gen_pre_«it» -> «it» {
					sync gen_«it»_inner_start!;
				}'''
			}
			'''
			<transition>
				<source ref="gen_pre_«it»"/>
				<target ref="«it»"/>
				<label kind="synchronisation">gen_«it»_inner_start!</label>
			</transition>
			'''
		]
	}
	
	def signalTransitions(boolean xml) {
		states.flatMap[transitions].filter[signal !== null && target.isNested]
		.map[
		if (!xml) {
			return '''
			«target.name» -> gen_init {
			}'''
		}
		'''
		<transition>
			<source ref="«target.name»"/>
			<target ref="gen_init"/>
		</transition>
		'''
		]
		.toSet
	}
	
	def xmlTransitions() {
		(states.flatMap[state | state.transitions.map[transition | 
			'''
			<transition>
				<source ref="«state.name»"/>
				<target ref="«transition.targetName(state.isNested)»"/>
				«IF transition.hasGuard»
					<label kind="guard" x="«transition.x»" y="«transition.y»">«transition.xmlGuard»</label>
				«ENDIF»
				«IF transition.hasTimeout»
					<label kind="guard" x="«transition.x»" y="«transition.y»">gen_clock &gt;= «transition.timeout»</label>
				«ENDIF»
				«IF transition.hasSignal»
					<label kind="synchronisation" x="«transition.x»" y="«transition.y»">«transition.signal»!</label>
				«ENDIF»
				«IF transition.hasWhen»
					<label kind="synchronisation" x="«transition.x»" y="«transition.y»">«transition.when»?</label>
				«ENDIF»
				«IF transition.hasAssignment()»
					<label kind="assignment" x="«transition.x»" y="«transition.y»">«transition.assignments.join(', ')»</label>
				«ENDIF»
			</transition>
			'''
			]]+nestedStateTransitions(true)+signalTransitions(true)).join()
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
			«IF initialState !== null»
			<init ref="«initialState.name»"/>
			«ENDIF»
			«IF states.exists[!transitions.empty] || !nestedStateNames.empty»
			«xmlTransitions»
			«ENDIF»
		</template>
		'''
	}
	
}