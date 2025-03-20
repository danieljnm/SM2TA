package danieljnm.sm2ta.Dto

class FunctionDto {
	public String clientName
	public String function
	public String expression
	public String defaultValue
	public String type
	
	def convertedExpression(boolean requiresSuccess) {
		expression.contains(".empty()") ? 
			'''«IF !requiresSuccess»!(«ENDIF»«expression.replace(".empty()", " == 0")»«IF !requiresSuccess»)«ENDIF»''' 
			: '''«IF !requiresSuccess»!(«ENDIF»«expression»«IF !requiresSuccess»)«ENDIF»'''
	}
	
	def assignment() {
		expression.replace("=", ":=")
	}
}