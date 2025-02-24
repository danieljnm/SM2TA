package danieljnm.sm2ta

class Transition {
	public String state
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