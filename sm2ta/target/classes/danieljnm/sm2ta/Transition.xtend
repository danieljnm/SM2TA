package danieljnm.sm2ta

class Transition {
	String event
	TransitionType type
	State target
	
	new(String event, TransitionType type, State target) {
		this.event = event
		this.type = type
		this.target = target
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