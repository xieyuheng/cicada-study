package cicada

import scala.collection.immutable.ListMap

import cicada.pretty._

case class Env(map: Map[String, Def] = Map())
  (implicit config: EnvConfig = EnvConfig.default) {
  def get(name: String): Option[Def] = {
    map.get(name)
  }

  def contains(name: String): Boolean = {
    get(name).isDefined
  }

  def extend(kv: (String, Def)): Env = {
    Env(map + kv)
  }

  def extendByValueMap(valueMap: ListMap[String, Value]): Env = {
    valueMap.foldLeft(this) { case (env, (name, value)) =>
      env.extend(name -> DefineValue(name, value))
    }
  }

  def defValue(
    name: String,
    value: Value,
  ): Env = {
    extend(name -> DefineValue(name, value))
  }

  def defExp(
    name: String,
    exp: Exp,
  ): Env = {
    eval(exp, this) match {
      case Right(value) =>
        extend(name -> DefineValue(name, value))
      case Left(errorMsg) =>
        println(errorMsg)
        this
    }
  }

  private def defMemberType(
    name: String,
    map: MultiMap[String, Exp],
    superName: String,
  ): Env = {
    extend(name -> DefineMemberType(name, map, superName))
  }

  private def defSumType(
    name: String,
    map: MultiMap[String, Exp],
    memberNames: List[String],
  ): Env = {
    extend(name -> DefineSumType(name, map, memberNames))
  }

  def defType(
    name: String,
    fields: MultiMap[String, Exp],
    members: MultiMap[String, MultiMap[String, Exp]],
  ): Env = {
    val initEnv = defSumType(name, fields, members.keys.toList)
    members.entries.foldLeft(initEnv) { case (env, (memberName, map)) =>
      env.defMemberType(memberName, fields.merge(map), name)
    }
  }

  def defFn(
    name: String,
    args: MultiMap[String, Exp],
    ret: Exp,
    body: Exp,
  ): Env = {
    extend(name -> DefineFn(name, args, ret, body))
  }

  def importAll(that: Env): Env = {
    that.map.foldLeft(this) { case (env, (name, newDef)) =>
      env.get(name) match {
        case Some(oldDef) =>
          if (newDef != oldDef) {
            if (config.get("on_redefinition") == Some("warn")) {
              println("[warn]")
              println(s"- redefining:\n${addIndentToBlock(prettyDef(newDef), 1)}")
              println(s"- old definition:\n${addIndentToBlock(prettyDef(oldDef), 1)}")
            }
          }
        case None => {}
      }
      env.extend(name -> newDef)
    }
  }
}