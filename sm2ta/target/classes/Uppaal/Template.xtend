package Uppaal

import java.util.List
import danieljnm.sm2ta.StateMachine.State

class Template {
	public String name
	public List<Location> locations = newArrayList
	public String initial
	public List<Transition> transitions = newArrayList
	
	new(String name) {
		this.name = name
	}
	
	def location(State state) {
		if (state.isInitial) {
			initial = state.name
		}
		
		var location = new Location(state)
		locations.add(location)
		location
	}
	
	def transitions(State state) {
		state.transitions.forEach[it |
			val transition = new Transition(state.name, state.name.startsWith("gen_pre_") || it.target.nestedStates.empty ?  it.target.name : '''gen_pre_«it.target.name»''')
			transition.labels(it)
			transitions.add(transition)
		]
		if (state.transitions.empty && state.isCommitted) {
			val transitionToInitial = new Transition(state.name, "gen_init")
			transitionToInitial.labels.add(new Label("synchronisation", "Success!"))
			transitions.add(transitionToInitial)
		}
	}
	
	override toString() {
		'''
		<template>
			«IF name !== null»
			<name>«name»</name>
			«ENDIF»
			«FOR location : locations»
			«location»
			«ENDFOR»
			«IF initial !== null»
			<init ref="«initial»"/>
			«ENDIF»
			«FOR transition : transitions»
			«transition»
			«ENDFOR»
		</template>
		'''
	}
}