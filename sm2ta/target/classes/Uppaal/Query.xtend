package Uppaal

import java.util.List
import danieljnm.sm2ta.StateMachine.StateMachine

class Query {
	public List<String> queries

	new(StateMachine stateMachine) {
		queries = newArrayList
		queries.add("A[] not deadlock")
		stateMachine.errorStates.forEach[queries.add(it)]
		
		stateMachine.states.values.forEach [ state |
		    state.transitions.filter[!guard.nullOrEmpty].forEach [ t |
		        val negatedGuard = "not (" + t.guard + ")"
		        queries.add('''A[] («stateMachine.name».«state.name» and «negatedGuard.format» imply («stateMachine.name».«state.name»))''')
		    ]
		]
	}
	
	def errorStates(StateMachine stateMachine) {
		stateMachine.states.values.filter[it.transitions.empty].map['''A[] not «stateMachine.name».«name»''']
	}
	
	def format(String guard) {
		guard.replace('&&', 'and')
        .replace('>', '&gt;')
        .replace('<', '&lt;')
	}

	override toString() {
		'''
			<queries>
			«FOR query : queries»
				<query>
			    	<formula>«query»</formula>
			    </query>
			«ENDFOR»
			</queries>
		'''
	}
}
