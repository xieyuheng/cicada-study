import org.scalatest._
import xieyuheng.cicada._

class unifySpec extends FlatSpec with Matchers {
  "unify" should "not make bind weaker" in {
    val bind = Map(
      "2bda84b0-dd85-43c9-a94c-c00df82c9f9f" ->
        SumTypeValue(
          "Nat",
          MultiMap(List()),
          List("Zero", "Succ"),
          Map()))

    val srcValue = MemberTypeValue("Zero", MultiMap(List()), "List", Map())

    val tarValue = TypeOfType("2bda84b0-dd85-43c9-a94c-c00df82c9f9f")

    unify(srcValue, tarValue, bind, Env()) match {
      case Right(newBind) =>
        assert(bind.toSet.subsetOf(newBind.toSet))
      case Left(errorMsg) =>
        throw new Exception(errorMsg.toString)
    }
  }
}
