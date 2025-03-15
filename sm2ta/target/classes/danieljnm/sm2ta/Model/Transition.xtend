package danieljnm.sm2ta.Model

class Transition {
	public String stateName
	public String event
	public String target
	
	def String message() {
		switch (event.toLowerCase) {
			case event.toLowerCase.contains("success"): return "success"
			case event.toLowerCase.contains("failure"): return "error"
			default: return "default"
		}
	}
	
	def void convert() {
		convertStateName()
		convertTargetName()
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