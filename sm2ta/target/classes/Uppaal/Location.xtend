package Uppaal

class Location {
	public String id
	public int x
	public int y
	public Boolean committed
	public Name name
	public Label label
	
	override toString() {
		'''
		<location id="«id»" x="«x»" y="«y»"«IF committed» committed="true"«ENDIF»>
			«IF name !== null»
			«name»
			«ENDIF»
			«IF label !== null»
			«label»
			«ENDIF»
		</location>
		'''
	}
}