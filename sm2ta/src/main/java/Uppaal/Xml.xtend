package Uppaal

import java.util.List
import danieljnm.sm2ta.StateMachine.StateMachine

class Xml {
	public Declaration declaration
	public List<Template> templates = newArrayList
	public System system
	
	new(StateMachine stateMachine) {
		declaration = new Declaration(stateMachine)
		templates = setTemplates(stateMachine)
		system = new System(templates)
	}
	
	def setTemplates(StateMachine stateMachine) {
		val templates = newArrayList
		val template = new Template(stateMachine.name)
		stateMachine.states.values.sortBy[index].forEach[state, index |
			if (state.isInitial) {
				template.initial = state.name
			}
			if (!state.isNested) {
				template.location(state)
			}
			// nestings should have their own template
			templates.add(template)
		]
		templates
	}
	
	override toString() {
		'''
		<?xml version="1.0" encoding="utf-8"?>
		<nta>
			«declaration»
			«FOR template : templates»
			«template»
			«ENDFOR»
			«system»
		</nta>
		'''
	}
}