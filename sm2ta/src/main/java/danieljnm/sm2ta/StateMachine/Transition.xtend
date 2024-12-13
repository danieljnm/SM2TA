package danieljnm.sm2ta.StateMachine

class Transition {
	String event
	State target
	String guard
	String action
	
	new(String event, State target) {
		this.event = event
		this.target = target
	}
	
	def getEvent() {
		event
	}
	
	def getTarget() {
		target
	}
	
	def setGuard(String guard) {
		this.guard = guard
	}
	
	def getGuard() {
		guard
	}
	
	def setAction(String action) {
		this.action = action
	}
	
	def getAction() {
		action
	}
		
	override toString() {
		'''
		«IF target !== null»
		«event» -> «target.name»«IF guard !== null» (Guard: «guard»)«ENDIF»«IF action !== null» (Action: «action»)«ENDIF»
		«ENDIF»
		'''
	}
}