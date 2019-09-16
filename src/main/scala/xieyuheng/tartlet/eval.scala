package xieyuheng.tartlet

object eval {

  def apply(exp: Exp, env: Env): Either[Err, Val] =
    eval(exp: Exp, env: Env)

  def eval(exp: Exp, env: Env): Either[Err, Val] = {
    exp match {
      case Var(name: String) =>
        env.lookup_val(name) match {
          case Some(value) =>
            Right(value)
          case None =>
            Left(Err(s"can not find var: ${this} in env: ${env}"))
        }
      case Atom =>
        Right(ValAtom)
      case Quote(sym: String) =>
        Right(ValQuote(sym))
      case Eqv(t: Exp, from: Exp, to: Exp) =>
        for {
          t <- eval(t, env)
          from <- eval(from, env)
          to <- eval(to, env)
        } yield ValEqv(t, from, to)
      case Replace(target: Exp, motive: Exp, base: Exp) =>
        for {
          targetVal <- eval(target, env)
          motiveVal <- eval(motive, env)
          baseVal <- eval(base, env)
          res <- Replace.exe(
            targetVal,
            motiveVal,
            baseVal)
        } yield res
      case Same =>
        Right(ValSame)
      case Succ(prev: Exp) =>
        for {
          prevVal <- eval(prev, env)
        } yield ValSucc(prevVal)
      case NatInd(target: Exp, motive: Exp, base: Exp, step: Exp) =>
        for {
          targetVal <- eval(target, env)
          motiveVal <- eval(motive, env)
          baseVal <- eval(base, env)
          stepVal <- eval(step, env)
          res <- NatInd.exe(
            targetVal,
            motiveVal,
            baseVal,
            stepVal)
        } yield res
      case Nat =>
        Right(ValNat)
      case Zero =>
        Right(ValZero)
      case Ap(rator: Exp, rand: Exp) =>
        for {
          fn <- eval(rator, env)
          arg <- eval(rand, env)
          res <- Ap.exe(fn, arg)
        } yield res
      case Fn(name: String, body: Exp) =>
        Right(ValFn(EnvClo(env, name, body)))
      case Absurd =>
        Right(ValAbsurd)
      case AbsurdInd(target: Exp, motive: Exp) =>
        for {
          targetVal <- eval(target, env)
          motiveVal <- eval(motive, env)
          res <- AbsurdInd.exe(targetVal, motiveVal)
        } yield res
      case Sigma(name: String, arg_t: Exp, cdr_t: Exp) =>
        for {
          arg_tVal <- eval(arg_t, env)
        } yield ValSigma(arg_tVal, EnvClo(env, name, cdr_t))
      case Sole =>
        Right(ValSole)
      case Trivial =>
        Right(ValTrivial)
      case Universe =>
        Right(ValUniverse)
      case Pi(name: String, arg_t: Exp, ret_t: Exp) =>
        for {
          arg_tVal <- eval(arg_t, env)
        } yield ValPi(arg_tVal, EnvClo(env, name, ret_t))
      case Car(pair: Exp) =>
        for {
          pairVal <- eval(pair, env)
          res <- Car.exe(pairVal)
        } yield res
      case Cdr(pair: Exp) =>
        for {
          pairVal <- eval(pair, env)
          res <- Cdr.exe(pairVal)
        } yield res
      case Cons(car: Exp, cdr: Exp) =>
        for {
          car <- eval(car, env)
          cdr <- eval(cdr, env)
        } yield ValCons(car, cdr)
      case The(t: Exp, value: Exp) =>
        eval(value, env)
    }
  }
}