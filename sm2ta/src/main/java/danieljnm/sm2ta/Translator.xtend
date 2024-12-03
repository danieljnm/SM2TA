package danieljnm.sm2ta

class Translator {
	static StateMachine stateMachine = new StateMachine()
	
	def static void main(String[] args) {
		stateMachine = new StateMachine()
	}
	
	def static getStateMachine() {
		stateMachine
	}
}