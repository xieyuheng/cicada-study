import org.scalatest._
import xieyuheng.cicada._

class evalSpec extends FlatSpec with Matchers {
  "eval" should "eval Type to LogicVar" in {
    val env = Env()
    for {
      t <- eval(Type(), env)
    } println(t)
  }

  it should "eval undefined Var to NeutralValue" in {
    val env = Env()
    for {
      neu <- eval(Var("x"), env)
    } assert(neu == NeutralValue(VarNeutral("x")))
  }

  it should "eval defined Var to value" in {
    val env = Env()
      .defineValue("x", LogicVar("#x"))
      .defineValue("y", LogicVar("#y"))

    for {
      x <- eval(Var("x"), env)
      y <- eval(Var("y"), env)
    } {
      assert(x == LogicVar("#x"))
      assert(y == LogicVar("#y"))
    }
  }

  def pp(exp: Exp, env: Env): Unit = {
    eval(exp, env) match {
      case Right(value) =>
        println(s"=> ${Pretty.fromValue(value, 0)}")
      case Left(errorMsg) =>
        println(s"?> ${errorMsg}")
    }
  }

  val NatModule = Env()
    .defineUnion("Nat", MultiMap(), List("Zero", "Succ"))
    .defineClass("Zero", MultiMap())
    .defineClass("Succ", MultiMap("prev" -> Var("Nat")))

  it should "eval NatModule" in {
    val module = NatModule

    pp(Var("Nat"), module)
    pp(Var("Zero"), module)
    pp(Var("Succ"), module)
    pp(Ap(Var("Succ"), MultiMap("prev" -> Var("Zero"))), module)
    pp(Field(Ap(Var("Succ"), MultiMap("prev" -> Var("Zero"))), "prev"), module)
  }

  val ListModule = Env()
    .defineUnion("List", MultiMap("A" -> Type()), List("Null", "Cons"))
    .defineClass("Null", MultiMap("A" -> Type()))
    .defineClass("Cons", MultiMap(
      "A" -> Type(),
      "head" -> Var("A"),
      "tail" -> Ap(Var("List"), MultiMap("A" -> Var("A"))),
    ))

  it should "eval ListModule" in {
    val module = ListModule.importAll(NatModule)

    pp(Var("List"), module)
    pp(Var("Null"), module)
    pp(Var("Cons"), module)

    pp(Var("Nat"), module)
    pp(Var("Zero"), module)
    pp(Var("Succ"), module)

    pp(
      Ap(Var("Cons"), MultiMap(
        "A" -> Var("Nat"),
        "head" -> Var("Zero"),
        "tail" -> Ap(Var("Cons"), MultiMap(
          "A" -> Var("Nat"),
          "head" -> Var("Zero"),
          "tail" -> Ap(Var("Cons"), MultiMap(
            "A" -> Var("Nat"),
            "head" -> Var("Zero"),
            "tail" -> Var("Null"))))))),
      module)

    pp(
      Ap(Var("Cons"), MultiMap(
        "A" -> Var("Nat"),
        "head" -> Var("Zero"),
        "tail" -> Var("Null"))),
      module)

    pp(
      Ap(Var("Cons"), MultiMap(
        "A" -> Var("Nat"),
        "head" -> Var("Zero"),
        "tail" -> Ap(Var("Null"), MultiMap("A" -> Var("Nat"))))),
      module)
  }
}
