package danieljnm.sm2ta.Dto

class TransitionDto {
	public String stateName
	public String event
	public String target
	public String clientBehaviour
	public String reactor
	
	def boolean isReactorTransition() {
		event.startsWith("EvAll")
	}
	
	def boolean requiresSuccess() {
		event.startsWith("EvCbSuccess")
	}
	
	def boolean isTimer() {
		event.startsWith("EvTimer")
	}
	
	def String message() {
		switch (event.toLowerCase) {
			case event.toLowerCase.contains("success"): return "Success"
			case event.toLowerCase.contains("failure"): return "Failure"
			default: return "default"
		}
	}
	
	def void convert() {
		convertStateName()
		convertTargetName()
		clientBehaviour = event.substring(event.indexOf('<') + 1, event.indexOf(','))
		reactor = event.startsWith("EvAll") ? event.substring(event.indexOf(',') + 2, event.indexOf('>')) : ""
	}
	
	def void convertStateName() {
		val index = stateName.indexOf("<")
		if (index == -1)
		{
			return
		}
		
		stateName = stateName.substring(0, index)
	}
	
	def void convertTargetName() {
		val index = target.indexOf("<")
		if (index == -1)
		{
			return
		}
		
		target = target.substring(0, index)
	}
}