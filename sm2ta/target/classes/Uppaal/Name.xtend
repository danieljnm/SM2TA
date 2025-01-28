package Uppaal

class Name {
	public String name
	public int x
	public int y
	
	new(String name) {
		this.name = name
	}
	
//	new(String name, int x, int y) {
//		this.name = name
//		this.x = x
//		this.y = y
//	}
	
	override toString() {
		'''
		<name x="«x»" y="«y»">«name»</name>
		'''
	}
}