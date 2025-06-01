package danieljnm.sm2ta

import com.google.gson.Gson
import danieljnm.sm2ta.StateMachine.StateMachine
import java.nio.file.Files
import java.nio.file.Paths
import java.util.List
import danieljnm.sm2ta.Dto.ClientBehaviourDto
import danieljnm.sm2ta.Dto.ConditionDto
import danieljnm.sm2ta.Dto.FunctionDto
import danieljnm.sm2ta.Dto.StateReactorDto
import danieljnm.sm2ta.Dto.TransitionDto
import danieljnm.sm2ta.Dto.VariableDto
import danieljnm.sm2ta.Dto.StateDto

class Translator {
	static StateMachine stateMachine = new StateMachine()
	
	def static void main(String[] args) {
		translate()
		println(stateMachine.toXml)
	}
	
	def static process(StateDto state) {
		stateMachine.state(state.stateName)
	}
	
	def static void translate() {
		val initial = states.findFirst[initial && !nestedInitial]
		stateMachine.name(initial.namespace)
			.state(initial.stateName).initial
		variables.setVariables
		initial.namespace.setStates
	}
	
	def static setStates(String namespace) {
		val transitions = transitions
		val functions = functions
		val topLevelStates = states.filter[it.namespace == namespace]
    	topLevelStates.forEach[state |
	        stateMachine.state(state.stateName)
	        val stateTransitions = transitions.getOrDefault(state.stateName, newArrayList)
        	stateTransitions.forEach[transition |
        		val updates = state.updates.split(",")
				        .map[update | getAssignment(update, functions)]
				        .filterNull
				val client = behaviours.getOrDefault(transition.clientBehaviour, newArrayList)
				val transitionUpdates = newArrayList
				client.forEach[it |
					val assignment = getAssignment(it.methodName, functions)
					if (assignment !== null) {
						transitionUpdates.add(assignment)
					}
				]
				val guards = transition.guards.filterNull.filter[guard | variables.exists[variable | guard.toString.contains(variable.variable)]]
				val timeout = transition.isTimer ? state.timer : 0
				val when = !state.action.nullOrEmpty && transition.event.contains(state.action) ? state.action : null
        		stateMachine.state(state.stateName)
        			.transition(transition.target).guard(guards.join(' &amp;&amp; ')).timeout(timeout).action((updates + transitionUpdates).join(', ')).when(when)
        	]

	        val nestedNamespace = state.stateName
	        val childStates = states.filter[it.namespace == nestedNamespace].sortBy[!it.nestedInitial]
	        if (!childStates.empty) {
	        	stateMachine.state(state.stateName)
	            	.nesting[
	                	childStates.forEach[nested |
	                		val updates = nested.updates.split(",")
						        .map[update | getAssignment(update, functions)]
						        .filterNull
	                		val nestedTransitions = transitions.getOrDefault(nested.stateName, newArrayList)
	                    	nestedState(nested.stateName)
                    		nestedTransitions.forEach[transition |
                    			val target = states.findFirst[stateName == transition.target]
                    			if (target !== null && target.namespace == nested.namespace) {
                    				val guards = transition.guards
                    				val timeout = transition.isTimer ? nested.timer : 0
                    				val when = !nested.action.nullOrEmpty && transition.event.contains(nested.action) ? nested.action : null
                    				nestedState(nested.stateName).transition(transition.target).guard(guards.join(' &amp;&amp; ')).timeout(timeout).action(updates.join(', ')).when(when)
                    				return
                    			}
                    			val when = !nested.action.nullOrEmpty && transition.event.contains(nested.action) ? nested.action : null
                    			nestedState(nested.stateName).transition('''«nested.namespace»«transition.message»''').when(when)
                    			nestedState('''«nested.namespace»«transition.message»''').committed
                    			stateMachine.state(nested.namespace).transition(transition.target).when(transition.message).action(updates.join(', '))
                    		]
	            	]
		    	]
		    }
	    ]
	}
	
	def static getGuards(TransitionDto transition) {
		transition.isReactorTransition ?
			transition.conditions.map		
			: transition.map
	}
	
	def static getConditions(TransitionDto transition) {
		reactors.filter[name == transition.reactor].map[new ConditionDto(it)]
	}
	
	def static map(Iterable<ConditionDto> conditions) {
		conditions.flatMap[condition | 
			behaviours.getOrDefault(condition.clientBehaviour, newArrayList).map(condition.requiresSuccess)
		]
	}
	
	def static map(TransitionDto transition) {
		transition.clientBehaviours.map(transition.requiresSuccess)
	}
	
	def static getClientBehaviours(TransitionDto transition) {
		behaviours.getOrDefault(transition.clientBehaviour, newArrayList)
	}
	
	def static map(Iterable<ClientBehaviourDto> clientBehaviours, boolean requiresSuccess) {
		clientBehaviours.filter[requiresSuccess == (event == "postSuccessEvent")]
        		.map[behaviour | functions.findFirst[function == behaviour.methodName]?.convertedExpression(behaviour.inIf)]
	}
	
	def static getStates() {
		val states = new Gson().fromJson(getJson("states"), typeof(StateDto[]))
		states.forEach[stateName = convert(stateName)]
		states
	}
		
	def static getAssignment(String action, List<FunctionDto> functions) {
	    var FunctionDto current = functions.findFirst[f | f.function == action && f.type != "return"]
	    var Object defaultVal = null
	    
	    while (current !== null && current.type != "assignment") {
	        if (defaultVal === null) {
	            defaultVal = current.defaultValue
	        }
	        
	        val currentExpr = current.expression
	        current = functions.findFirst[f | f.function == currentExpr]
	    }
	    
	    val value = defaultVal ?: current?.defaultValue
	    current === null || !((value instanceof Integer) && variables.exists[variable | value.toString.contains(variable.variable)]) ? null : '''«current.expression» := «defaultVal ?: current.defaultValue»'''
	}
	
	def static getVariables() {
		new Gson().fromJson(getJson("variables"), typeof(VariableDto[])).groupBy[variable].values.map[head].toList
	}
	
	def static setVariables(List<VariableDto> variables) {
		variables.forEach[variableDefinition |
			stateMachine.variables[
				variable(variableDefinition.variable).type(variableDefinition.convertedType).value(variableDefinition.initializedValue)
			]
		]

	}
	
	def static getReactors() {
		new Gson().fromJson(getJson("state_reactors"), typeof(StateReactorDto[]))
	}
	
	def static getBehaviours() {
		new Gson().fromJson(getJson("client_behaviours"), typeof(ClientBehaviourDto[]))
			.groupBy[name].mapValues[toList]
	}
	
	def static getFunctions() {
		new Gson().fromJson(getJson("functions"), typeof(FunctionDto[]))
	}
	
	def static getTransitions() {
		val transitions = new Gson().fromJson(getJson("transitions"), typeof(TransitionDto[]))
		transitions.forEach[convert]
		transitions.groupBy[stateName]
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