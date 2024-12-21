package danieljnm.sm2ta.StateMachine

import java.util.HashMap

class StateMachine {
	public int index
	public String name
	public HashMap<String, State> states = newHashMap
	
	def name(String name) {
		this.name = name
		this
	}
	
	def State getInitialState() {
		states.values.findFirst[it.isInitial]
	}
	
	def State state(String name) {
		states.computeIfAbsent(name) [new State(this, name)]
	}
	
	def boolean hasTransitions() {
		if (states.empty) {
			return false
		}
		
		states.values.findFirst[!it.transitions.empty] !== null
	}
	
	def String toUppaal() {
		'''
		«IF !channels.empty || !nestings.empty»
		chan «(channels + nestings).join(', ')»;
		«ENDIF»
		«IF !clocks.empty»
		clock «clocks.join(', ')»;
		«ENDIF»
		«FOR process : processes»
		«process»
		«ENDFOR»
		«FOR channel : uppaalChannels»
		«channel.channelToUppaal»
		«ENDFOR»
		system «(processes.map[name] + uppaalChannels.map['''gen_sync_«it»''']).join(', ')»;
		'''
	}
	
	def String channelToUppaal(String channel) {
		'''
		process gen_sync_«channel» {
			state
				initSync;
			init initSync;
			trans
				initSync -> initSync {
					sync «channel»!;
				};
		}
		'''
	}
	
	
	def processes() {
		val processes = newArrayList
		val process = new Uppaal.Process(name)
		
		val nestings = newArrayList
		
		states.values.sortBy[index].forEach[state |
			if (!state.isNested) {
				process.addState(state)
			}
			
			if (!state.nestedStates.empty) {
				nestings.add(state)
			}
		]
		
		processes.add(process)
		
		nestings.forEach[nesting |
			val nestedProcess = new Uppaal.Process('''«nesting.name»_inner''')
			var initial = new State(nesting, "gen_init").initial.transition("event", nesting.nestedStates.get(0).name).when('''gen_«nesting.name»_inner_start''')
			nestedProcess.addState(initial)
			nesting.nestedStates.forEach[nestedProcess.addState(it)]
			processes.add(nestedProcess)
		]
		
		processes
	}
	
	def uppaalChannels() {
		val set = signals.toSet
		whens.filter[!set.contains(it)]
	}
	
	def channels() {
		(signals + whens).toSet
	}
	
	def signals() {
		transitions.filter[signal !== null].map[signal]
	}
	
	def whens() {
		transitions.filter[when !== null].map[when]
	}
	
	def transitions() {
		states.values.flatMap[nestedStates].flatMap[transitions]
		+
		states.values.flatMap[transitions]
	}
	
	def nestings() {
		states.values.filter[!nestedStates.empty]
		.map['''gen_«name»_inner_start''']
		.toSet
	}
	
	def clocks() {
		states.values.flatMap[transitions]
		.filter[timeout > 0]
		.map['''«target.name»_gen_clock''']
	}
}