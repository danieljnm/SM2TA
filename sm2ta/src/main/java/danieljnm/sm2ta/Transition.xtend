package danieljnm.sm2ta

class Transition {
	String event
	State target
	
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
	
	override toString() {
		'''
		«IF target !== null»
		«event» -> «target.name»
		«ENDIF»
		'''
	}
}

enum TransitionType {
	Direct
}