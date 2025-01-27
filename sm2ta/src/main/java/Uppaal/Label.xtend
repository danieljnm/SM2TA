package Uppaal

class Label {
	public String kind
	public int x
	public int y
	public String value
	
	new(String kind, int x, int y, String value) {
		this.kind = kind
		this.x = x
		this.y = y
		this.value = value
	}
	
	override toString() {
		'''
		<label kind="«kind»" x="«x»" y="«y»">«value.format»</label>
		'''
	}
	
	def String format(String value) {
		value.replace('>', '&gt;').replace('<', '&lt;')
	}
}