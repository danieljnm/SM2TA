package danieljnm.sm2ta.StateMachine

import java.util.HashMap

class StateMachine {
	public String name
	public HashMap<String, State> states = newHashMap
	
	def name(String name) {
		this.name = name
		this
	}
	
	def State getInitialState() {
		states.values.findFirst[it.isInitial]
	}
	
	def State state(String name) {
		states.computeIfAbsent(name) [new State(this, name)]
	}
	
	def boolean hasTransitions() {
		if (states.empty) {
			return false
		}
		
		states.values.findFirst[!it.transitions.empty] !== null
	}
	
	def String toUppaal() {
		'''
		«IF !channels.empty»
		chan «channels.join(', ')»;
		«ENDIF»
		«IF !clocks.empty»
		clock «clocks.join(', ')»;
		«ENDIF»
		«FOR process : processes»
		«process»
		«ENDFOR»
		system «processes.map[name].join(', ')»;
		'''
	}
	
	
	def processes() {
		val processes = newArrayList
		val process = new Uppaal.Process(name)
		
		states.values.forEach[state |
			if (!state.isNested) {
				process.addState(state)
			}
			
			/* A state with nested states should be treated
			 * as a separate process
			 * state.nestedStates.forEach[nestedState |
				processes.add(nestedState.toProcess)
			]*/
		]
		
		processes.add(process)
		processes
	}
	
	def channels() {
		states.values.flatMap[transitions]
		.filter[when !== null]
		.map[it.when]
	}
	
	def clocks() {
		states.values.flatMap[transitions]
		.filter[timeout !== null]
		.map['''«target.name»_gen_clock''']
	}
}