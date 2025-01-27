package Uppaal

import java.util.List

class Template {
	public String name
	public List<Location> locations
	public String initial
	public List<Transition> transitions
	
	new(String name) {
		this.name = name
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
			<init ref="«initial»"
			«ENDIF»
			«FOR transition : transitions»
			«transition»
			«ENDFOR»
		</template>
		'''
	}
}