package Uppaal

import java.util.List

class Transition {
	public String source
	public String target
	public List<Label> labels = newArrayList
	
	override toString() {
		'''
		<source ref="«source»"/>
		<target ref="«target»/>
		«FOR label : labels»
		«label»
		«ENDFOR»
		'''
	}
}