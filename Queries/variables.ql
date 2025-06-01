import cpp

from Class client, MemberVariable var, string value, Namespace ns
where
  var.getDeclaringType() = client and
  client.getNamespace() = ns and
  (var.getType().toString() in [
    "int", "double", "float", "bool"
  ] or
  var.getType().toString().matches("queue%") or
  var.getType().toString().matches("list%"))
  and
  (
    if exists(Initializer init | init = var.getInitializer())
    then value = var.getInitializer().getExpr().toString()
    else value = ""
  )
  and not ns.getQualifiedName().matches("boost%")
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
  and value != "- ..."
  and not value.matches("call to%")
select
  ns.getQualifiedName() as namespace,
  client.getName() as clientName,
  var.getName() as variable,
  var.getType().toString() as type,
  value
