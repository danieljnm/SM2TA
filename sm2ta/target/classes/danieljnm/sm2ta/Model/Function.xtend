package danieljnm.sm2ta.Model

class Function {
	public String clientName
	public String function
	public String expression
	public String defaultValue
	
	def convertedExpression(boolean requiresSuccess) {
		expression.contains(".empty()") ? 
			'''«IF !requiresSuccess»!(«ENDIF»«expression.replace(".empty()", " == 0")»«IF !requiresSuccess»)«ENDIF»''' 
			: '''«IF !requiresSuccess»!(«ENDIF»«expression»«IF !requiresSuccess»)«ENDIF»'''
	}
	
	def assignment() {
		expression.replace("=", ":=")
	}
}