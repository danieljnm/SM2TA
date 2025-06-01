import cpp

class Smacc2State extends Class {
  Smacc2State() {
    exists(MemberFunction member |
      member.getDeclaringType() = this and
      member.hasName("staticConfigure")
    )
  }
  
  FunctionCall getStateReactorCall() {
    exists(MemberFunction member |
      member.getDeclaringType() = this and
      member.hasName("staticConfigure") and
      result.getEnclosingFunction() = member and
      result.getTarget().getName().matches("%createStateReactor%")
    )
  }
}

string getStateReactor(FunctionCall function) {
  exists(string argument | 
    argument = function.getATemplateArgument().toString() and
    argument.matches("%EvAll%<%") and
    result = argument.regexpCapture("EvAll[A-Za-z0-9_]+<[^,]+,\\s*([A-Za-z0-9_]+)\\s*>", 1)
  )
}

string getEvent(FunctionCall function) {
  exists(string argument |
    argument = function.getATemplateArgument().toString() and
    argument.matches("%list<%") and
    result = argument.regexpFind("Ev[A-Za-z0-9_]+<[^<>]+>", _, _)
  )
}

from Smacc2State state, FunctionCall function
where 
  function = state.getStateReactorCall()
select getStateReactor(function) as name, state.getName() as stateName, getEvent(function) as event