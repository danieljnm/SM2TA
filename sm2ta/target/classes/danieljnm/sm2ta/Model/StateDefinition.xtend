package danieljnm.sm2ta.Model

class StateDefinition {
	public String stateName
	public String namespace
	public boolean initial
	public boolean nestedInitial
	
	def void convertName() {
		val index = stateName.indexOf("<")
		if (index == -1)
		{
			return
		}
		
		stateName = stateName.substring(0, index)
	}
}