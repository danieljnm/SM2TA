package danieljnm.sm2ta.Model

class Condition {
	public String clientBehaviour
	public boolean requiresSuccess
	
	new(StateReactor reactor) {
		clientBehaviour = reactor.event.substring(reactor.event.indexOf('<') + 1, reactor.event.indexOf(','))
		requiresSuccess = reactor.event.startsWith("EvCbSuccess")
	}
}