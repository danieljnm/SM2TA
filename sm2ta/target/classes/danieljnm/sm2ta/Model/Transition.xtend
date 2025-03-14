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
}