package Uppaal

import java.util.List

class System {
	public List<String> systems = newArrayList
	
	new(List<Template> templates) {
		systems = templates.filter[!exclude].map[name].toList
	}
	
	override toString() {
		'''
		<system>
			«IF !systems.empty»
				system «systems.join(', ')»;
			«ENDIF»
		</system>
		'''
	}
}