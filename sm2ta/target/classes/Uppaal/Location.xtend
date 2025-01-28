package Uppaal

import danieljnm.sm2ta.StateMachine.State

class Location {
	public String id
	public int x
	public int y
	public Boolean committed = false
	public Name name
	public Label label
	
	new(State state) {
		id = state.name
		name = new Name(state.name)
		var transition = state.transitions.findFirst[timeout > 0]
		if (transition !== null) {
			label = new Label("invariant", '''gen_clock <= «transition.timeout»''')
		}
	}
	
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