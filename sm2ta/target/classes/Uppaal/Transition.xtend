package Uppaal

import java.util.List

class Transition {
	public String source
	public String target
	public List<Label> labels = newArrayList
	
	new(String source, String target) {
		this.source = source
		this.target = target
	}
	
	def labels(danieljnm.sm2ta.StateMachine.Transition transition) {
		if (transition.hasGuard) {
			labels.add(new Label("guard", transition.guard))
		}
		if (transition.hasTimeout) {
			labels.add(new Label("guard", '''gen_clock >= «transition.timeout»'''))
		}
		if (transition.hasSignal) {
			labels.add(new Label("synchronisation", '''«transition.signal»!'''))
		}
		if (transition.hasWhen) {
			labels.add(new Label("synchronisation", '''«transition.when»?'''))
		}
		if (transition.hasAssignment()) {
			labels.add(new Label("assignment", transition.assignments.join(', ')))
		}
	}
	
	override toString() {
		'''
		<transition>
			<source ref="«source»"/>
			<target ref="«target»"/>
			«FOR label : labels»
			«label»
			«ENDFOR»
		</transition>
		'''
	}
}