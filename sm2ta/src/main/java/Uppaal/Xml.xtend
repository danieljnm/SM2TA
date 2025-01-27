package Uppaal

import java.util.List
import danieljnm.sm2ta.StateMachine.StateMachine
import danieljnm.sm2ta.StateMachine.State
import java.util.Collection

class Xml {
	public Declaration declaration
	public List<Template> templates = newArrayList
	public List<Transition> transitions = newArrayList
	public List<String> systems = newArrayList
	
	new(StateMachine stateMachine) {
		declaration = new Declaration(stateMachine)
	}
	
	override toString() {
		'''
		<?xml version="1.0" encoding="utf-8"?>
		<nta>
			«declaration»
			«FOR template : templates»
			«template»
			«ENDFOR»
			«IF !systems.empty»
			<system>
				«systems.join(', ')»
			</system>
			«ENDIF»
		</nta>
		'''
	}
}