package Uppaal

import java.util.List
import danieljnm.sm2ta.StateMachine.StateMachine
import danieljnm.sm2ta.StateMachine.State

class Xml {
	public Declaration declaration
	public List<Template> templates = newArrayList
	public System system
	GridLayout layout = new GridLayout
	int index = 0
	
	new(StateMachine stateMachine) {
		index = 0
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
				template.transitions(state)
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
		
		val synchronisations = newHashMap
		stateMachine.whens.forEach[synchronisations.put(it, new Synchronisation(it, "when", index++))]
		stateMachine.signals.forEach[synchronisations.put(it, new Synchronisation(it, "signal", index++))]
		synchronisations.values.sortBy[index].forEach[it |
			val synchronisationTemplate = it.toTemplate
			layout.applyLayout(synchronisationTemplate)
			templates.add(synchronisationTemplate)
		]
		templates
	}
	
	def toTemplate(State nesting) {
		val template = new Template('''«nesting.name»_inner''')
		var initial = new State(nesting, "gen_init").initial.transition(nesting.nestedStates.get(0).name).when('''gen_«nesting.name»_inner_start''')
		template.location(initial)
		template.transitions(initial)
		nesting.nestedStates.forEach[it, index |
			template.location(it)
			template.transitions(it)
		]
		template
	}
	
	def whens(StateMachine stateMachine) {
		stateMachine.transitions.filter[when !== null].map[when].toSet
	}
	
	def transitions(StateMachine stateMachine) {
		stateMachine.states.values.flatMap[nestedStates].flatMap[transitions]
		+
		stateMachine.states.values.flatMap[transitions]
	}
	
	def signals(StateMachine stateMachine) {
		stateMachine.states.values.flatMap[transitions].filter[signal !== null].map[signal].toSet
	}

	def toTemplate(Synchronisation synchronisation) {
		val template = new Template('''gen_sync_«synchronisation.name»''')
		var initial = new State(null as State, "initSync")
		switch synchronisation.type {
			case "when": initial.transition(initial).signal('''«synchronisation.name»''')
			case "signal": initial.transition(initial).when('''«synchronisation.name»''')
		}
		template.location(initial)
		template.transitions(initial)
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