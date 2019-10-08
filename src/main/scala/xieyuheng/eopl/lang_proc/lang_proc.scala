package xieyuheng.eopl.lang_proc

import pretty._

import xieyuheng.eopl.Interpreter

import xieyuheng.partech.Parser

object lang_proc extends Interpreter(
  "lang_proc", "0.0.1", { case code =>
    Parser(grammar.lexer, grammar.exp).parse(code) match {
      case Right(tree) =>
        val env = EnvEmpty()
        val exp = grammar.exp_matcher(tree)
        eval.eval(exp, env) match {
          case Right(value) =>
            println(s">>> ${pretty_exp(exp)}")
            println(s"=== ${pretty_val(value)}")
          case Left(err) =>
            println(s"${err.msg}")
            System.exit(1)
        }
      case Left(error) =>
        println(s"[parse_error] ${error.msg}")
        System.exit(1)
    }
  }
)