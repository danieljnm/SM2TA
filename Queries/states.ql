import cpp

predicate isInitial(Class c) {
  exists(Class machineBase |
    machineBase.getName().matches("SmaccStateMachineBase<%,%>%") and
    machineBase.getName().regexpCapture(".*SmaccStateMachineBase<[^,]+,\\s*([^>]+)>.*", 1) = c.getName()
  )
}

boolean isInitialBool(Class c) {
  isInitial(c) and result = true
  or
  not isInitial(c) and result = false
}


predicate isNestedInitial(Class c) {
  exists(Class parentBase | 
    parentBase.getName().matches("%SmaccState<%,%,%>%") and 
    parentBase.getName().regexpCapture(".*SmaccState<[^,]+,\\s*[^,]+,\\s*([^,]+),.*", 1) = c.getName()
  )
}

boolean isNestedInitialBool(Class c) {
  isNestedInitial(c) and result = true
  or
  not isNestedInitial(c) and result = false
}

predicate hasActionCall(Class c, string action) {
  exists(MemberFunction f, FunctionCall call |
    f.getDeclaringType() = c and
    call.getEnclosingFunction() = f and
    exists(Expr qualifier | 
      qualifier = call.getQualifier() and
      action = call.getTarget().getName() and
      not action.matches("getLogger") and
      not action.matches("get_name") and
      not action.matches("requires%") and
      not action.matches("~%") and
      not action.matches("configure")
    )
  )
}

string getAllActions(Class c) {
  exists(string action | hasActionCall(c, action)) and
  result = concat(string action | 
    hasActionCall(c, action)
    |
    action, ","
  )
  or
  not exists(string action | hasActionCall(c, action)) and result = ""
}

predicate getTimerArgument(Class c, int timerVal) {
  exists(MemberFunction f, FunctionCall call, Expr arg |
    f.getDeclaringType() = c and
    f.getName() = "staticConfigure" and
    f.isStatic() and
    call.getEnclosingFunction() = f and
    call.getTarget().getName() = "configure_orthogonal" and
    arg = call.getArgument(0) and
    call.getTemplateArgument(0).toString() = "OrTimer" and
    timerVal = arg.toString().toInt()
  )
}

int getTimer(Class c) {
  exists(int t | getTimerArgument(c, t)) and result = any(int t | getTimerArgument(c, t) | t)
  or
  not exists(int t | getTimerArgument(c, t)) and result = 0
}

predicate hasActionServerCall(Class c, string actionCall) {
  exists(MemberFunction f, FunctionCall call |
    f.getDeclaringType() = c and
    (f.getName() = "staticConfigure" or f.getName() = "runtimeConfigure") and
    call.getEnclosingFunction() = f and
    (call.getTarget().getName() = "configure_orthogonal" or call.getTarget().getName() = "configure") and
    call.getTemplateArgument(0).toString() = "OrNavigation" and
    actionCall = call.getTemplateArgument(1).toString()
  )
}

string getActionServerCall(Class c) {
  exists(string action | hasActionServerCall(c, action)) and
    result = any(string action | hasActionServerCall(c, action) | action)
  or
    not exists(string action | hasActionServerCall(c, action)) and result = ""
}


from Class c, Class base
where 
  c.getABaseClass() = base and
  base.getName().matches("SmaccState<%,%>%") and
  not base.getName().matches("%SS%")
select 
  c.getName() as stateName,
  base.getName().regexpCapture(".*SmaccState<[^,]+,\\s*([^,>]+).*", 1) as namespace,
  isInitialBool(c) as initial,
  isNestedInitialBool(c) as nestedInitial,
  getAllActions(c) as updates,
  getTimer(c) as timer,
  getActionServerCall(c) as action