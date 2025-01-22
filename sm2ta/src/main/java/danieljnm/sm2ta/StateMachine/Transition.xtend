package danieljnm.sm2ta.StateMachine

class Transition {
	public State target
	public String guard
	public String action
	public int timeout
	public String when
	public String signal
	
	new(State target) {
		this.target = target
	}
	
	def targetName(boolean isParentNested) {
		if (isParentNested || target.nestedStates.empty)
			return target.name
		
		'''gen_pre_«target.name»'''
	}
	
	def guardValue() {
		switch guard.toLowerCase {
			case "true": 1
			case "false": 0
			default: guard
		}
	}
	
	def xmlGuard() {
		guardValue.toString.replace('<', '&lt;').replace('>', '&gt;')
	}
		
	override toString() {
		'''
		«IF target !== null»
		-> «target.name»«IF guard !== null» (Guard: «guard»)«ENDIF»«IF action !== null» (Action: «action»)«ENDIF»«IF timeout > 0» (Timeout: «timeout»)«ENDIF»
		«ENDIF»
		'''
	}
}