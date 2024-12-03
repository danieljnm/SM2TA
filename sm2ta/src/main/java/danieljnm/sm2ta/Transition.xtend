package danieljnm.sm2ta

class Transition {
	TransitionType type
	State target
	
	new(TransitionType type, State target) {
		this.type = type
		this.target = target
	}
}

enum TransitionType {
	Direct
}