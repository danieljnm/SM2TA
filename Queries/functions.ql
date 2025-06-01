import cpp

from Class client, Function method, string expression, string defaultValue, string type, Namespace ns
where
  not client.getName().matches("%<%>%") and
  not client.getName().matches("%unsequenced%") and
  not client.getName().matches("%_policy") and
  method.getDeclaringType() = client and
  client.getNamespace() = ns and
  not ns.getQualifiedName().matches("boost%")
  and not ns.getQualifiedName().matches("std%")
  and not ns.getQualifiedName().matches("%cxx%")
  and not ns.getQualifiedName().matches("%smacc2%")
  and not ns.getQualifiedName().matches("%srv%")
  and not ns.getQualifiedName().matches("rclcpp%")
  and not ns.getQualifiedName().matches("%::msg%")
  and not ns.getQualifiedName().matches("rcpputils%")
  and not ns.getQualifiedName().matches("libstatistics%")
  and not ns.getQualifiedName().matches("tf2%")
  and not ns.getQualifiedName().matches("%_ros_%")
  and not ns.getQualifiedName().matches("%nav2%")
  and not ns.getQualifiedName().matches("%date%")
  and not ns.getQualifiedName() in [
      "", "mpl_", "smacc2", "KDL", "rcpputils",
    ]
  and not method.getName().matches("%=")
  and not defaultValue.matches("call to%")
  and
  (
    exists(FunctionCall innerCall, Function calledMethod, BinaryOperation binOp |
      innerCall.getEnclosingFunction() = method and
      calledMethod = innerCall.getTarget() and
      calledMethod.getDeclaringType() = client and
      binOp = innerCall.getAnArgument() and
      expression = calledMethod.getName() and
      defaultValue = binOp.getLeftOperand().toString() + " " + binOp.getOperator().toString() + " " + binOp.getRightOperand().toString() and
      type = "function"
    )
    or
    exists(FunctionCall innerCall, Function calledMethod, Expr arg |
      innerCall.getEnclosingFunction() = method and
      calledMethod = innerCall.getTarget() and
      calledMethod.getDeclaringType() = client and
      arg = innerCall.getAnArgument() and
      not arg instanceof BinaryOperation and
      not exists(Parameter p | 
        p = method.getAParameter() and 
        exists(p.getInitializer()) and 
        arg.toString() = p.getName()
      ) and
      expression = calledMethod.getName() and
      defaultValue = arg.toString() and
      type = "function"
    )
    or
    exists(ReturnStmt ret, BinaryOperation binOp |
      ret.getEnclosingFunction() = method and
      binOp = ret.getExpr() and
      expression = binOp.getLeftOperand().toString() + " " + binOp.getOperator().toString() + " " + binOp.getRightOperand().toString() and
      defaultValue = "" and
      type = "return"
    )
    or
    exists(ReturnStmt ret, UnaryOperation unOp, FunctionCall fc |
      ret.getEnclosingFunction() = method and
      unOp = ret.getExpr() and
      fc = unOp.getOperand() and
      if exists(fc.getQualifier())
      then expression = unOp.getOperator().toString() + fc.getQualifier().toString() + "." + fc.getTarget().getName() + "()"
      else expression = unOp.getOperator().toString() + fc.getTarget().getName() + "()" and
      defaultValue = "" and
      type = "return"
    )
    or
    exists(ReturnStmt ret, UnaryOperation unOp |
      ret.getEnclosingFunction() = method and
      unOp = ret.getExpr() and
      not unOp.getOperand() instanceof FunctionCall and
      expression = unOp.getOperator().toString() + unOp.getOperand().toString() and
      defaultValue = "" and
      type = "return"
    )
    or
    exists(ReturnStmt ret |
      ret.getEnclosingFunction() = method and
      ret.hasExpr() and
      not ret.getExpr() instanceof BinaryOperation and
      not ret.getExpr() instanceof UnaryOperation and
      expression = ret.getExpr().toString() and
      not expression.matches("call to%") and
      defaultValue = "" and
      type = "return"
    )
    or
    exists(AssignExpr assignExpr |
      assignExpr.getEnclosingFunction() = method and
      not exists(AssignExpr earlier |
        earlier.getEnclosingFunction() = method and
        earlier.getLocation().getStartLine() < assignExpr.getLocation().getStartLine()
      ) and
      expression = assignExpr.getLValue().toString() and
      defaultValue = assignExpr.getRValue().toString() and
      type = "assignment"
    )
    or
    exists(Parameter p, FunctionCall fc, Function calledFunc | 
      p = method.getAParameter() and
      exists(p.getInitializer()) and
      fc.getEnclosingFunction() = method and
      calledFunc = fc.getTarget() and
      calledFunc.getDeclaringType() = client and
      expression = calledFunc.getName() and
      defaultValue = p.getInitializer().getExpr().toString() and
      type = "function"
    )
    or
    exists(ReturnStmt ret, ExprStmt sideEffectStmt, FunctionCall sideEffectCall |
      ret.getEnclosingFunction() = method and
      sideEffectStmt.getEnclosingFunction() = method and
      sideEffectCall = sideEffectStmt.getExpr() and
      sideEffectStmt.getLocation().getStartLine() < ret.getLocation().getStartLine() and
      sideEffectStmt.getParent() = ret.getParent() and
      exists(string containerName, string operationEffect |
        containerName = sideEffectCall.getQualifier().toString() and
        (
          (
            sideEffectCall.getTarget().getName().matches("%pop%") or 
            sideEffectCall.getTarget().getName().matches("%remove%")
          ) and operationEffect = containerName + " - 1"
          or
          (
            sideEffectCall.getTarget().getName().matches("%push%") or 
            sideEffectCall.getTarget().getName().matches("%insert%")
          ) and operationEffect = containerName + " + 1"
          or
          (
            sideEffectCall.getTarget().getName().matches("%erase%") or 
            sideEffectCall.getTarget().getName().matches("%clear%")
          ) and operationEffect = "0"
        ) and
        expression = containerName and
        defaultValue = operationEffect and
        type = "assignment"
      )
    )
  )
select client.getName() as clientName, method.getName() as function, expression, defaultValue, type