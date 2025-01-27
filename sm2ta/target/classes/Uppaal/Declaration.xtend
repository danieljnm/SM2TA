package Uppaal

import java.util.List
import danieljnm.sm2ta.StateMachine.StateMachine
import java.util.Collection
import danieljnm.sm2ta.StateMachine.State

class Declaration {
	public List<String> variables = newArrayList
	public Boolean hasClock
	public List<String> channels = newArrayList
	
	new(StateMachine stateMachine) {
		variables = stateMachine.variables.map['''«type» «name» = «value»;''']
		hasClock = setClock(stateMachine.states.values)
		channels = setChannels(stateMachine.states.values)
	}
	
	def setClock(Collection<State> states) {
		states.flatMap[transitions]
		.exists[timeout > 0]
		||
		states.flatMap[nestedStates].flatMap[transitions]
		.exists[timeout > 0]
	}
	
	def setChannels(Collection<State> states) {
		(whens(states) + signals(states) + nestings(states))
		.toSet
		.toList
	}
	
	def signals(Collection<State> states) {
		states.flatMap[transitions].filter[signal !== null].map[signal]
	}
	
	def whens(Collection<State> states) {
		(states.flatMap[nestedStates].flatMap[transitions]
		+
		states.flatMap[transitions])
		.filter[when !== null].map[when]
	}
	
	def nestings(Collection<State> states) {
		states.filter[!nestedStates.empty]
		.map['''gen_«name»_inner_start''']
	}
	
	override toString() {
		'''
		<declaration>
			«IF !variables.empty»
			«variables.join('\n')»
			«ENDIF»
			«IF hasClock»
			clock gen_clock;
			«ENDIF»
			«IF !channels.empty»
			chan «channels.join(', ')»;
			«ENDIF»
		</declaration>
		'''
	}
}