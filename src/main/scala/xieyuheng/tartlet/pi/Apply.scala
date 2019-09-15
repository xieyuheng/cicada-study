package xieyuheng.tartlet

case class Ap (
  rator: Exp,
  rand: Exp,
) extends Eliminator {
  def eval(env: Env): Either[Err, Val] = {
    for {
      fn <- rator.eval(env)
      arg <- rand.eval(env)
      res <- Ap.exe(fn, arg)
    } yield res
  }

  def alphaEq(
    that: Exp,
    thisMap: Map[String, String],
    thatMap: Map[String, String],
  ): Boolean = {
    that match {
      case Ap(rator2, rand2) =>
        rator.alphaEq(rator2, thisMap, thatMap) &&
        rand.alphaEq(rand2, thisMap, thatMap)
      case _ => false
    }
  }

  /*
   ctx :- rator => Pi(x: A, R)
   ctx :- rand <= A
   -----------------
   ctx :- Ap(rator, rand) => R
   */
  def infer(ctx: Ctx): Either[Err, The] = {
    for {
      the <- rator.infer(ctx)
      t <- the.t.eval(ctx.toEnv)
      res <- t match {
        case ValPi(arg_t, ret_t) => {
          for {
            rand <- rand.check(ctx, arg_t)
            argVal <- rand.eval(ctx.toEnv)
            retVal <- ret_t.apply(argVal)
            retExp <- retVal.readback_val(ctx, ValUniverse)
          } yield The(retExp, Ap(the.value, rand))
        }
        case _ =>
          Left(Err("expected Pi, " + "found: ${t}"))
      }
    } yield res
  }
}

object Ap {
  def exe(
    fn: Val,
    arg: Val,
  ): Either[Err, Val] = {
    fn match {
      case ValLambda(clo) =>
        clo.apply(arg)
      case TheNeu(ValPi(arg_t, ret_t), neutral) =>
        for {
          t <- ret_t.apply(arg)
        } yield TheNeu(t, NeuAp(neutral, TheVal(arg_t, arg)))
      case _ =>
        Left(Err(
          "fn should be " +
            "ValLambda(clo) | " +
            "TheNeu(ValPi(arg_t, ret_t), neutral): " +
            s"${fn}"))
    }
  }
}
