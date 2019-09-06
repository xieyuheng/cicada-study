package xieyuheng.cicada.prelude

import xieyuheng.cicada._
import xieyuheng.cicada.expDSL._
import xieyuheng.cicada.pretty._
import xieyuheng.cicada.json.rw._

import upickle.default._

object nat_test extends Module with App {

  import_all(nat)

  val succ_test = {
    eval_on_right("succ_t" ap %("prev" -> "zero_t")) {
      case value =>
        assert(nat.to_int(value) == 1)
    }

    eval_on_right("succ_t" ap %("prev" -> "zero_t") dot "prev") {
      case value =>
        assert(nat.to_int(value) == 0)
    }
  }

  val nat_add_test = {
    eval_on_right("nat_add" ap %("x" -> "one", "y" -> "one")) {
      case value =>
        assert(nat.to_int(value) == 2)
    }

    eval_on_right("nat_add" ap %("x" -> "two", "y" -> "two")) {
      case value =>
        assert(nat.to_int(value) == 4)
    }

    eval_on_right("nat_add" ap %("x" -> "two", "y" -> "three")) {
      case value =>
        assert(nat.to_int(value) == 5)
    }
  }

  val nat_mul_test = {
    eval_on_right("nat_mul" ap %("x" -> "one", "y" -> "one")) {
      case value =>
        assert(nat.to_int(value) == 1)
    }

    eval_on_right("nat_mul" ap %("x" -> "two", "y" -> "two")) {
      case value =>
        assert(nat.to_int(value) == 4)
    }

    eval_on_right("nat_mul" ap %("x" -> "two", "y" -> "three")) {
      case value =>
        assert(nat.to_int(value) == 6)
    }
  }

  val nat_factorial_test = {
    eval_on_right("nat_factorial" ap %("x" -> "zero")) {
      case value =>
        assert(nat.to_int(value) == 1)
    }

    eval_on_right("nat_factorial" ap %("x" -> "one")) {
      case value =>
        assert(nat.to_int(value) == 1)
    }

    eval_on_right("nat_factorial" ap %("x" -> "two")) {
      case value =>
        assert(nat.to_int(value) == 2)
    }

    eval_on_right("nat_factorial" ap %("x" -> "three")) {
      case value =>
        assert(nat.to_int(value) == 6)
    }

    eval_on_right("nat_factorial" ap %("x" -> "four")) {
      case value =>
        // PROBLEM the use of deepSelf in nat.to_int cost so much
        // assert(nat.to_int(value) == 24)
    }
  }
}
