package danieljnm.sm2ta.StateMachine

import java.util.HashMap
import java.util.List

class StateMachine {
	public int index
	public String name
	public HashMap<String, State> states = newHashMap
	public List<Variable> variables = newArrayList
	
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
	
	def String toXml() {
		new Uppaal.Xml(this).toString
	}
	
	def String toXta() {
		'''
		«IF !variables.empty»
		«variables.map['''«type» «name» = «value»'''].join(';\n')»;
		«ENDIF»
		«IF hasClock»
		clock gen_clock;
		«ENDIF»
		«IF !channels.empty || !nestings.empty»
		chan «(channels + nestings).join(', ')»;
		«ENDIF»
		«FOR process : processes»
		«process»
		«ENDFOR»
		«FOR channel : whenChannels»
		«channel.channelToUppaal("!")»
		«ENDFOR»
		«FOR channel : signalChannels»
		«channel.channelToUppaal("?")»
		«ENDFOR»
		system «(processes.map[name] + uppaalChannels.map['''gen_sync_«it»''']).join(', ')»;
		'''
	}
	
	def String channelToUppaal(String channel, String sign) {
		'''
		process gen_sync_«channel» {
			state
				initSync;
			init initSync;
			trans
				initSync -> initSync {
					sync «channel»«sign»;
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
			processes.add(nesting.toProcess)
		]
		
		processes
	}
	
	def toProcess(State nesting) {
		val nestedProcess = new Uppaal.Process('''«nesting.name»_inner''')
		var initial = new State(nesting, "gen_init").initial.transition(nesting.nestedStates.get(0).name).when('''gen_«nesting.name»_inner_start''')
		nestedProcess.addState(initial)
		nesting.nestedStates.forEach[nestedProcess.addState(it)]
		nestedProcess
	}
	
	def uppaalChannels() {
		(whenChannels + signalChannels).toSet
	}
	
	def whenChannels() {
		states.values.filter[nestedStates.empty]
			.flatMap[transitions]
			.filter[when !== null]
			.map[when]
			.toSet
	}
	
	def signalChannels() {
		states.values.filter[nestedStates.empty]
			.flatMap[transitions]
			.filter[signal !== null]
			.map[signal]
			.toSet
	}
	
	def channels() {
		(whens + signals).toSet
	}
	
	def signals() {
		states.values.flatMap[transitions].filter[signal !== null].map[signal]
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
	
	def hasClock() {
		states.values.flatMap[transitions]
		.exists[timeout > 0]
		||
		states.values.flatMap[nestings].flatMap[transitions]
		.exists[timeout > 0]
	}
	
	def clocks() {
		states.values.flatMap[transitions]
		.filter[timeout > 0]
		.map['''«target.name»_gen_clock''']
	}
	
	def StateMachine variables((StateMachine) => void context) {
		context.apply(this)
		this
	}
	
	def variable(String name) {
		variables.add(new Variable(name))
		this
	}
	
	def type(String type) {
		if (variables.empty) {
			return this
		}
		
		variables.lastOrNull.type = type
		this
	}
	
	def value(String value) {
		if (variables.empty) {
			return this
		}
		
		variables.lastOrNull.value = value
		this
	}
	
}