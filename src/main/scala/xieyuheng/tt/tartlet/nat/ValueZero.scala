package xieyuheng.tt.tartlet

case object ValueZero extends Value {
  def readBack (ctx: Ctx, t: Value): Either[ErrorMsg, Exp] =
    Right(Zero)
}
