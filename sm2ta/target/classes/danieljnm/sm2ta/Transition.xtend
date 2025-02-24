package danieljnm.sm2ta

class Transition {
	public String state
	public String event
	public String target
	
	def String message() {    
	    switch (target.toLowerCase) {
	        case target.toLowerCase.contains("success"): return "success"
	        case target.toLowerCase.contains("failure"): return "error"
	        default: return "default"
	    }
	}
}