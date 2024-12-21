package danieljnm.sm2ta.StateMachine

class Transition {
	public String event
	public State target
	public String guard
	public String action
	public int timeout
	public String when
	public String signal
	
	new(String event, State target) {
		this.event = event
		this.target = target
	}
	
	def targetName(boolean isParentNested) {
		if (isParentNested || target.nestedStates.empty)
			return target.name
		
		'''gen_pre_«target.name»'''
	}
		
	override toString() {
		'''
		«IF target !== null»
		«event» -> «target.name»«IF guard !== null» (Guard: «guard»)«ENDIF»«IF action !== null» (Action: «action»)«ENDIF»«IF timeout > 0» (Timeout: «timeout»)«ENDIF»
		«ENDIF»
		'''
	}
}