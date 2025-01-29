package Uppaal

class Name {
	public String name
	public int x
	public int y
	
	new(String name) {
		this.name = name
	}
	
	override toString() {
		'''
		<name x="«x»" y="«y»">«name»</name>
		'''
	}
}