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
		var location = new Location(state)
		locations.add(location)
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