package danieljnm.sm2ta.StateMachine

class Transition {
	String event
	State target
	String guard
	
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
	
	override toString() {
		'''
		«IF target !== null»
		«event» -> «target.name»
		«ENDIF»
		«IF guard !== null»
		Guard: «guard»
		«ENDIF»
		'''
	}
}

enum TransitionType {
	Direct
}