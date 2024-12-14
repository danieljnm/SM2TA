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
			process «name» {
				«IF !states.empty»
				state
						«states.values.map[name].join(',\n')»;
				init «initialState.name»;
				«IF hasTransitions»
				trans
						«states.values.map[state | state.transitions.map[
						'''
						«state.name» -> «target.name» {
						};
						'''].join('\n')].join('')»
				«ENDIF»
				«ENDIF»
			}
			system «name»;
		'''
	}
}