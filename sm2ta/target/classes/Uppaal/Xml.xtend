package Uppaal

import java.util.List
import danieljnm.sm2ta.StateMachine.StateMachine
import danieljnm.sm2ta.StateMachine.State

class Xml {
	public Declaration declaration
	public List<Template> templates = newArrayList
	public System system
	GridLayout layout = new GridLayout
	
	new(StateMachine stateMachine) {
		declaration = new Declaration(stateMachine)
		templates = setTemplates(stateMachine)
		system = new System(templates)
	}
	
	def setTemplates(StateMachine stateMachine) {
		val templates = newArrayList
		val template = new Template(stateMachine.name)
		val nestings = newArrayList
		stateMachine.states.values.sortBy[index].forEach[state, index |
			if (!state.isNested) {
				template.location(state)
			}
			
			if (!state.nestedStates.empty) {
				nestings.add(state)
			}				
		]
		layout.applyLayout(template)
		templates.add(template)
		
		nestings.forEach[nesting |
			var nestingTemplate = nesting.toTemplate
			layout.applyLayout(nestingTemplate)
			templates.add(nestingTemplate)
		]
		
		templates
	}
	
	def toTemplate(State nesting) {
		val template = new Template('''«nesting.name»_inner''')
		var initial = new State(nesting, "gen_init").initial.transition(nesting.nestedStates.get(0).name).when('''gen_«nesting.name»_inner_start''')
		template.location(initial)
		nesting.nestedStates.forEach[it, index |
			template.location(it)
		]
		template
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