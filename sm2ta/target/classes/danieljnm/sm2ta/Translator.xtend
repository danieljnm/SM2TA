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
		transitions.setTransitions
		val reactors = reactors
		val behaviours = behaviours.groupBy[name].mapValues[toList]
	}
	
	def static getStates() {
		val states = new Gson().fromJson(getJson("states"), typeof(StateDefinition[]))
		states.forEach[it.convertName]
		states
	}
	
	def static setStates(List<StateDefinition> states, String namespace) {
		val topLevelStates = states.filter[it.namespace == namespace]
    	topLevelStates.forEach[state |
	        stateMachine.state(state.stateName)
	        val nestedNamespace = state.stateName
	        val childStates = states.filter[it.namespace == nestedNamespace].sortBy[!it.nestedInitial]
	        if (!childStates.empty) {
	            stateMachine.state(state.stateName)
	            .nesting[
	                childStates.forEach[nested |
	                    nestedState(nested.stateName)
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
				variable(variableDefinition.variable).type(variableDefinition.convertedType).value(variableDefinition.value)
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
		new Gson().fromJson(getJson("transitions"), typeof(Transition[]))
	}
	
	def static setTransitions(List<Transition> transitions) {
		val reactorMap = reactors.groupBy[stateName].mapValues[it.groupBy[name].mapValues[toList]]
		val behaviourMap = behaviours.groupBy[name].mapValues[toList]
		val transitionMap = transitions.groupBy[stateName]
		
		stateMachine => [
			transitions.forEach[it |
				it.convert
				stateMachine.state(it.stateName)
					.transition(it.target)
			]
		]
	}
	
	def static getJson(String file) {
		val bytes = Files.readAllBytes(Paths.get('''src/main/java/Data/«file».json'''))
		new String(bytes)
	}
	
	def static void translateTransitions() {
		// When doing this, get the state reactors too.
		// If a transition is dependent on multiple conditions
		// this behaviour can be received from the state reactor
		// and used as a guard for the transition
		var json = new String(Files.readAllBytes(Paths.get("src/main/java/Data/transitions.json")))
		val transitions = new Gson().fromJson(json, typeof(Transition[]))
		reset()
		stateMachine.name("test")
		transitions.forEach[it |
			stateMachine
				.state(it.stateName)
					.transition(it.target).when(it.message)
		]
		println(stateMachine.toXml)
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