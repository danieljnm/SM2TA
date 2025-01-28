package Uppaal

import java.util.List

class System {
	public List<String> systems = newArrayList
	
	new(List<Template> templates) {
		systems = templates.map[name]
	}
	
	override toString() {
		'''
		<system>
			«IF !systems.empty»
				«systems.join(', ')»
			«ENDIF»
		</system>
		'''
	}
}