package danieljnm.sm2ta.StateMachine

class Transition {
	public String event
	public State target
	public String guard
	public String action
	public String timeout
	
	new(String event, State target) {
		this.event = event
		this.target = target
	}
		
	override toString() {
		'''
		«IF target !== null»
		«event» -> «target.name»«IF guard !== null» (Guard: «guard»)«ENDIF»«IF action !== null» (Action: «action»)«ENDIF»«IF timeout !== null» (Timeout: «timeout»)«ENDIF»
		«ENDIF»
		'''
	}
}