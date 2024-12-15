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
			«generateGloablVariables»
			process «name» {
				«IF !states.empty»
					«generateStates»
					«generateTransitions»
				«ENDIF»
			}
			system «name»;
		'''
	}
	
	def String generateGloablVariables() {
		'''
		«IF states.values.exists[it.transitions.exists[timeout !== null]]»
		clock gen_clock;
		«ENDIF»
		«IF states.values.exists[it.transitions.exists[when !== null]]»
		chan «states.values.flatMap[it.transitions].filter[when !== null].map[when].join(', ')»;
		«ENDIF»
		'''
	}
	
	def String generateStates() {
		'''
		state
				«states.values.map[name].join(',\n')»;
		init «initialState.name»;
		'''
	}
	
	def generateTransitions() {
		'''
		«IF hasTransitions»
		trans
				«states.values.filter[nestedStates.empty].map[state | state.transitions.map[
				'''
				«state.name» -> «target.name» {
					«IF guard !== null»[«guard»]«ENDIF»
					«IF timeout !== null»timeout = «timeout»;«ENDIF»
					«IF action !== null»assign { «action» }«ENDIF»
					«IF when !== null»sync «when»?«ENDIF»
				};
				'''].join('\n')].join('')»
		«ENDIF»
		'''
	}
}