package danieljnm.sm2ta.Model

class Variable {
	public String clientName
	public String variable
	public String type
	public String value
	
	def String convertedType() {
		switch (type.toLowerCase) {
			case "float": return "double"
			case "boolean": return "bool"
			default: return type.toLowerCase
		}
	}
}