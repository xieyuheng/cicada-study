package xieyuheng.eopl

object util {

  case class Err(msg: String) {
    def append_cause(cause: Err): Err = {
      Err(
        msg ++
          "because:\n" ++
          cause.msg)
    }
  }

  def result_maybe_err[A](result: Either[Err, A], err: Err): Either[Err, A] = {
    result match {
      case Right(value) => Right(value)
      case Left(cause) => Left(err.append_cause(cause))
    }
  }

}
