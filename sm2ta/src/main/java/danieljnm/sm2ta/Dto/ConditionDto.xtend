package danieljnm.sm2ta.Dto

class ConditionDto {
	public String clientBehaviour
	public boolean requiresSuccess
	
	new(StateReactorDto reactor) {
		clientBehaviour = reactor.event.substring(reactor.event.indexOf('<') + 1, reactor.event.indexOf(','))
		requiresSuccess = reactor.event.startsWith("EvCbSuccess")
	}
}