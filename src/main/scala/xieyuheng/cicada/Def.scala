package xieyuheng.cicada

sealed trait Def

final case class DefineValue(
  name: String,
  value: Value,
) extends Def

final case class DefineMemberType(
  name: String,
  map: MultiMap[String, Exp],
) extends Def

final case class DefineSumType(
  name: String,
  map: MultiMap[String, Exp],
  memberNames: List[String],
) extends Def
