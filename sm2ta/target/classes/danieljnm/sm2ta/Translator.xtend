package danieljnm.sm2ta

import com.google.gson.Gson
import danieljnm.sm2ta.Model.StateDefinition
import danieljnm.sm2ta.Model.Variable
import danieljnm.sm2ta.Model.Transition
import danieljnm.sm2ta.Model.ClientBehaviour
import danieljnm.sm2ta.Model.Function
import danieljnm.sm2ta.Model.StateReactor
import danieljnm.sm2ta.StateMachine.StateMachine
import java.nio.file.Files
import java.nio.file.Paths
import java.util.List
import danieljnm.sm2ta.Model.Condition

class Translator {
	static StateMachine stateMachine = new StateMachine()
	
	def static void main(String[] args) {
		//stateMachine = regular
		translate()
		//reset()
		//withBasicNesting()
		println(stateMachine.toXml)
	}
	
	def static void translate() {
		val states = states
		val initial = states.findFirst[initial && !nestedInitial]
		stateMachine.name(initial.namespace)
			.state(initial.stateName).initial
		variables.setVariables
		states.setStates(initial.namespace)
		//transitions.setTransitions
		//val reactors = reactors
		//val behaviours = behaviours.groupBy[name].mapValues[toList]
	}
	
	def static getStates() {
		val states = new Gson().fromJson(getJson("states"), typeof(StateDefinition[]))
		states.forEach[it.convertName]
		states
	}
	
	def static setStates(List<StateDefinition> states, String namespace) {
		val transitions = transitions.groupBy[stateName]
		val reactors = reactors
		val behaviours = behaviours.groupBy[name].mapValues[toList]
		val functions = functions
		val topLevelStates = states.filter[it.namespace == namespace]
    	topLevelStates.forEach[state |
	        stateMachine.state(state.stateName)
	        val stateTransitions = transitions.getOrDefault(state.stateName, newArrayList)
        	stateTransitions.forEach[transition |
        		/*val test = state.actions.split(",")
        				.map[action | functions.findFirst(function | function.function == action)?.assignment]
        		val actions = state.actions.split(",")
        			.map[action | functions.findFirst(function | function.function == action)?.assignment]
        			.filter[it !== null]*/
        			if (!state.actions.isNullOrEmpty) {
        				state.actions.split(",")
        					.map[action | functions.findFirst(function | function.function == action)?.assignment]
        					.filter[!it.isNullOrEmpty]
        					.forEach[println(it)]
        			}
        			val actions = newArrayList
        		if (transition.event.startsWith("EvAll")) {
        			val conditions = reactors.filter[name == transition.reactor].map[new Condition(it)]
        			val guards = conditions.flatMap[condition | behaviours.getOrDefault(condition.clientBehaviour, newArrayList)
        				.filter[condition.requiresSuccess == (event == "postSuccessEvent")]
        				.map[behaviour | functions.findFirst[function | function.function == behaviour.methodName]?.convertedExpression(condition.requiresSuccess)]
        			]
        			stateMachine.state(state.stateName)
        				.transition(transition.target).guard(guards.join(' &amp;&amp; ')).action(actions.join(', '))
        			return
        		}
        		val conditions = behaviours.getOrDefault(transition.clientBehaviour, newArrayList)
        				.filter[transition.event.startsWith("EvCbSuccess") == (event == "postSuccessEvent")]
        				.map[behaviour | functions.findFirst[function == behaviour.methodName]?.convertedExpression(behaviour.inIf)]
        		
        		stateMachine.state(state.stateName)
        			.transition(transition.target).guard(conditions.join(' &amp;&amp; ')).action(actions.join(', '))
        	]

	        val nestedNamespace = state.stateName
	        val childStates = states.filter[it.namespace == nestedNamespace].sortBy[!it.nestedInitial]
	        if (!childStates.empty) {
	        	stateMachine.state(state.stateName)
	            	.nesting[
	                	childStates.forEach[nested |
	                		val nestedTransitions = transitions.getOrDefault(nested.stateName, newArrayList)
	                    	nestedState(nested.stateName)
                    		nestedTransitions.forEach[transition |
                    			val target = states.findFirst[stateName == transition.target]
                    			if (target.namespace == nested.namespace) {
                    				nestedState(nested.stateName).transition(transition.target)
                    				return
                    			}
                    			nestedState(nested.stateName).transition('''«nested.namespace»«transition.message»''').signal(transition.message)
                    			nestedState('''«nested.namespace»«transition.message»''').committed
                    			stateMachine.state(nested.namespace).transition(transition.target).when(transition.message)
                    		]
	            	]
		    	]
		    }
	    ]
	}
	
	def static getVariables() {
		new Gson().fromJson(getJson("variables"), typeof(Variable[]))
	}
	
	def static setVariables(List<Variable> variables) {
		variables.forEach[variableDefinition |
			stateMachine.variables[
				variable(variableDefinition.variable).type(variableDefinition.convertedType).value(variableDefinition.initializedValue)
			]
		]

	}
	
	def static getReactors() {
		new Gson().fromJson(getJson("state_reactors"), typeof(StateReactor[]))
	}
	
	def static getBehaviours() {
		new Gson().fromJson(getJson("client_behaviours"), typeof(ClientBehaviour[]))
	}
	
	def static getFunctions() {
		new Gson().fromJson(getJson("functions"), typeof(Function[]))
	}
	
	def static getTransitions() {
		var transitions = new Gson().fromJson(getJson("transitions"), typeof(Transition[]))
		transitions.forEach[convert]
		transitions
	}
	
	def static setTransitions(List<Transition> transitions) {
		val reactorMap = reactors.groupBy[stateName].mapValues[it.groupBy[name].mapValues[toList]]
		val behaviourMap = behaviours.groupBy[name].mapValues[toList]
		val transitionMap = transitions.groupBy[stateName]
		
		stateMachine => [
			transitions.forEach[it |
				stateMachine.state(it.stateName)
					.transition(it.target)
			]
		]
	}
	
	def static getJson(String file) {
		val bytes = Files.readAllBytes(Paths.get('''src/main/java/Data/«file».json'''))
		new String(bytes)
	}
	
	def static regular() {
		stateMachine.name("FOD")
			.variables[
				variable("error").type("bool").value("false")
				variable("hasControl").type("bool").value("false")
				variable("missionIndex").type("int").value("-1")
				variable("waypoints").type("int").value("10")
			]
			.state("Idle").initial
				.transition("PositionAcquisition").action("error := false, hasControl := true")
			.state("PositionAcquisition")
				.transition("GlobalPlanning").when("Ready")
				.transition("Idle").when("LostControl").action("hasControl := false")
				.transition("Idle").when("Abort").action("error := true, hasControl := false")
				.transition("Idle").when("FailedEstimation").action("hasControl := false")
			.state("GlobalPlanning")
				.transition("NextPosition").when("Success").action("missionIndex := 0")
				.transition("Idle").when("LostControl").action("hasControl := false")
				.transition("Idle").when("Abort").action("error := true, hasControl := false")
			.state("NextPosition")
				.transition("CaptureState").when("ContinueLoop").guard("missionIndex < waypoints")
				.transition("MissionCompleted").when("Success").guard("missionIndex >= waypoints")
				.transition("Idle").when("LostControl").action("hasControl := false")
			.state("CaptureState")
				.transition("ValidateState").when("Success")
				.transition("Idle").when("LostControl").action("hasControl := false")
			.state("ValidateState")
				.transition("NextPosition").when("Success").action("missionIndex++")
				.transition("Idle").when("LostControl").action("hasControl := false")
			.state("MissionCompleted")
				.transition("Idle").when("Success")
				.transition("Idle").when("Abort").action("error := true, hasControl := false")
				.transition("Idle").when("LostControl").action("hasControl := false")
		stateMachine
	}
	
	def static withNesting() {
		stateMachine.name("FOD")
			.variables[
				variable("bool hasControl = false")
			]
			.state("Idle").initial
				.transition("PositionAcquisition").when("Ready").action("hasControl := true")
			.state("PositionAcquisition")
				.transition("GlobalPlanning")
				.transition("Idle").when("LostControl")
				.transition("Idle").when("Abort")
				.transition("Idle").when("FailedEstimation")
			.state("GlobalPlanning")
				.nesting[
					nestedState("NextPosition")
						.transition("CaptureState")
						.transition("MissionCompleted")
					nestedState("CaptureState")
						.transition("ValidateState")
						.transition("LostControl").signal("NestedLostControl")
					nestedState("ValidateState")
						.transition("NextPosition")
						.transition("LostControl").signal("NestedLostControl")
					nestedState("MissionCompleted")
						.transition("LostControl").signal("NestedLostControl")
						.transition("Abort").signal("NestedAbort")
						.transition("Success").signal("NestedSuccess")
					nestedState("Abort")
					nestedState("LostControl")
					nestedState("Success")
				]
				.transition("Idle").when("NestedLostControl")
				.transition("Idle").when("NestedAbort")
				.transition("Idle").when("NestedSuccess")
		stateMachine
	}
	
	def static withBasicNesting() {
		stateMachine.name("FOD")
			.variables[
				variable("bool hasControl = false")
			]
			.state("Idle").initial
				.transition("PositionAcquisition").when("Ready").action("hasControl := true")
			.state("PositionAcquisition")
				.transition("GlobalPlanning")
				.transition("Idle").when("LostControl")
			.state("GlobalPlanning")
				.nesting[
					nestedState("NextPosition")
						.transition("MissionCompleted")
					nestedState("MissionCompleted")
						.transition("LostControl").signal("NestedLostControl")
						.transition("Abort").signal("NestedAbort")
						.transition("Success").signal("NestedSuccess")
					nestedState("Abort")
					nestedState("LostControl")
					nestedState("Success")
				]
				.transition("Idle").when("NestedLostControl")
				.transition("Idle").when("NestedAbort")
				.transition("Idle").when("NestedSuccess")
		stateMachine
	}
	
	def static getStateMachine() {
		stateMachine
	}
	
	def static reset() {
		stateMachine = new StateMachine()
	}

	def static printMachine() {
		stateMachine.states.values.forEach[println(it)]
	}
}