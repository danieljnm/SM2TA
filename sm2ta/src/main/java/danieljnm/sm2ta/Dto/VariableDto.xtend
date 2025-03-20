package danieljnm.sm2ta.Dto

class VariableDto {
	public String clientName
	public String variable
	public String type
	public String value
	
	def String convertedType() {
		switch (type.toLowerCase) {
			case "float": return "double"
			case "boolean": return "bool"
			case type.startsWith("queue"): return "int"
			default: return type.toLowerCase
		}
	}
	
	def String initializedValue() {
		value == "" && (type.startsWith("queue") || type.startsWith("list")) ? "10" : value
	}
}