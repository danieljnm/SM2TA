import cpp

predicate isClientType(Class c, string t) {
    (c.getName().matches("Cb%") or c.getName().matches("Cl%")) and t = c.getName()
    or
    not (c.getName().matches("Cb%") or c.getName().matches("Cl%")) and t = "Global"
}

from 
  Class c,
  FunctionCall clientCall,
  FunctionCall eventCall,
  boolean isNegated,
  boolean inIf,
  string clientTypeName
where
  clientCall.getEnclosingFunction().getDeclaringType() = c and
  eventCall.getEnclosingFunction().getDeclaringType() = c and
  not clientCall.getTarget().getName().matches("create_%") and
  not clientCall.getTarget().getName().matches("%Node%") and
  not clientCall.getTarget().getName().matches("~%") and
  not clientCall.getTarget().getName().matches("%Smacc%") and
  not clientCall.getTarget().getDeclaringType().getName().matches("%<%>%") and
  not clientCall.getTarget().getDeclaringType().getName().matches("Client<%") and
  not clientCall.getTarget().getDeclaringType().getName().matches("%Smacc%") and
  not clientCall.getTarget().getDeclaringType().getName().matches("Log%") and
  not clientCall.getTarget().getDeclaringType().getName().toLowerCase().matches("%info") and
  not clientCall.getTarget().getDeclaringType().getName().toLowerCase().matches("thread") and
  not clientCall.getTarget().getDeclaringType().getName() in [
    "MetadataHelper", "Node", "Time", "SignalDetector", "StateReactor",
    "Duration", "PositionHelper", "SensorDataQoS", "CpWaypointNavigatorBase"
  ] and
  eventCall.getTarget().getName().matches("post%Event") and
  not clientCall.getTarget().getName() in [
    "reset", "fwrite", "strlen", "thread", "path", "Logger", "ServicesQoS",
    "rcutils_reset_error", "rcutils_get_error_string", "~Logger",
    "getLogger", "get_name", "postSuccessEvent", "postFailureEvent",
    "getComponent", "rcutils_log", "rcutils_logging_logger_is_enabled_for",
    "rcutils_logging_initialize", "get_logger"
  ] and
  eventCall.getLocation().getStartLine() > clientCall.getLocation().getStartLine() and
  (
    if exists(NotExpr notExpr | clientCall.getParent+() = notExpr)
    then isNegated = true
    else isNegated = false
  ) and
  (
    if exists(IfStmt ifstmt, Stmt s | 
          s = eventCall.getEnclosingStmt() and
          s.getParent*() = ifstmt.getThen())
    then inIf = true
    else inIf = false
  ) and
  c.getName().toString() != "ISmaccState" and
  isClientType(clientCall.getTarget().getDeclaringType(), clientTypeName)
select
  c.getName() as name,
  clientCall.getTarget().getName() as methodName,
  eventCall.getTarget().getName() as event,
  inIf,
  isNegated

