package danieljnm.sm2ta.Dto

class StateDto {
	public String stateName
	public String namespace
	public boolean initial
	public boolean nestedInitial
	public String updates
	public int timer
	public String action
	
	def convert(String value) {
		val index = value.indexOf("<")
		if (index == -1)
		{
			return value
		}
		
		value.substring(0, index)
	}
}