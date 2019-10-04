package xieyuheng.simple

import xieyuheng.util.pretty._

object pretty {

  def pretty_exp_map(map: Map[String, Exp]) =
    pretty_map(map) {
      case (name, exp) =>
        s"${name}: ${pretty_exp(exp)};" }

  def pretty_exp(exp: Exp): String = {
    exp match {
      case Var(name, type_annotation) =>
        name
      case Fn(name, arg_type_annotation, body) =>
        s"${name} => ${pretty_exp(body)}"
      case Ap(fn, arg) =>
        s"${pretty_exp(fn)}(${pretty_exp(arg)})"
    }
  }

  def pretty_decl(decl: Decl): String = {
    decl match {
      case DeclLet(name, e) =>
        s"let ${name} = ${pretty_exp(e)}"
    }
  }

}