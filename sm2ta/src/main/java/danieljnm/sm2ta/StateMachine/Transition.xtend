package danieljnm.sm2ta.StateMachine

class Transition {
	public State target
	public String guard
	public String action
	public int timeout
	public String when
	public String signal
	public int x
	public int y
	int spacing = 15
	
	new(State target) {
		this.target = target
	}
	
	def targetName(boolean isParentNested) {
		if (isParentNested || target.nestedStates.empty)
			return target.name
		
		'''gen_pre_«target.name»'''
	}
	
	def hasGuard() {
		if (guard !== null) {
			y += spacing
		}
		guard !== null
	}
	
	def hasTimeout() {
		if (timeout > 0) {
			y += spacing
		}
		
		timeout > 0
	}
	
	def hasSignal() {
		if (signal !== null) {
			y += spacing
		}
		
		signal !== null
	}
	
	def hasWhen() {
		if (when !== null) {
			y += spacing	
		}
		
		when !== null
	}
	
	def hasAssignment() {
		if (!assignments.empty) {
			y += spacing
		}
		
		!assignments.empty
	}
	
	def assignments() {
		var assigns = newArrayList
		if (target.transitions.exists[timeout > 0])
			assigns.add('gen_clock := 0')
			
		if (action !== null)
			assigns.add(action)
		
		assigns
	}
	
	def properties() {
    	#[
	        guard !== null,
	        timeout > 0,
	        signal !== null,
	        when !== null
	    ]
	    .map[if (it) 1 else 0]
	    .reduce[value, next | value + next]
	}
	
	def guardValue() {
		switch guard.toLowerCase {
			case "true": 1
			case "false": 0
			default: guard
		}
	}
	
	def xmlGuard() {
		guardValue.toString.replace('<', '&lt;').replace('>', '&gt;')
	}
		
	override toString() {
		'''
		«IF target !== null»
		-> «target.name»«IF guard !== null» (Guard: «guard»)«ENDIF»«IF action !== null» (Action: «action»)«ENDIF»«IF timeout > 0» (Timeout: «timeout»)«ENDIF»
		«ENDIF»
		'''
	}
}