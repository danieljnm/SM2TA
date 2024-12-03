package danieljnm.sm2ta

class Transition {
	TransitionType type
	
	new(TransitionType type) {
		this.type = type
	}
}

enum TransitionType {
	Direct
}