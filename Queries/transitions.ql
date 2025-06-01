import cpp

class Smacc2State extends Class {
  Smacc2State() {
    exists(TypedefType typedef |
      typedef.getDeclaringType() = this and
      typedef.getName() = "reactions"
    ) and not this.getName().matches("%<SS>%")
  }

  TypedefType getReactionsTypedef() {
    result.getDeclaringType() = this and
    result.getName() = "reactions"
  }
}

predicate hasTransition(Smacc2State state, string event, string target) {
  exists(string reactions, string transition |
			reactions = state.getReactionsTypedef().getUnderlyingType().toString() and
      transition = reactions.regexpFind("Transition<[A-Za-z0-9_<>, ]+>", _, _) and
      event = transition.regexpFind("Ev[A-Za-z0-9_]+(<[^<>]+>)?", _, _) and
      (
        target = transition.regexpCapture(".*" + event + ",\\s*([A-Za-z0-9_]+)(<[^>]*>)?\\s*[,>].*", 1) or
        target = transition.regexpCapture(".*" + event + ",\\s*([A-Za-z0-9_]+)\\s*[,>].*", 1)
      )
  )
}

from Smacc2State state, string event, string target
where hasTransition(state, event, target)
select state.getName() as stateName, event, target
