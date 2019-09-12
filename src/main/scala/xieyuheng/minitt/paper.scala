package xieyuheng.minitt

import xieyuheng.partech._
import xieyuheng.partech.pretty._
import xieyuheng.partech.example._

import xieyuheng.minitt.expDSL._

object paper_test extends App {

  val code = s"""
  let id: (A: univ, A) -> A = (A, x) => x

  let bool_t: univ = sum {
    true;
    false;
  }

  let true: bool_t = true[]
  let false: bool_t = false[]

  eq! id(bool_t, true) true
  eq! id(bool_t, false) false
  eq! id((A: univ, A) -> A, id) id

  let bool_elim: (
    C: (bool_t) -> univ,
    C(true),
    C(false),
    b: bool_t,
  ) -> C(b) = (C, h0, h1) => match {
    true[] => h0;
    false[] => h1;
  }

  letrec nat_t: univ = sum {
    zero;
    succ nat_t;
  }

  let zero: nat_t = zero[]
  let one: nat_t = succ[zero]
  let two: nat_t = succ[one]
  let three: nat_t = succ[two]
  let four: nat_t = succ[three]
  let five: nat_t = succ[four]
  let six: nat_t = succ[five]
  let seven: nat_t = succ[six]
  let eight: nat_t = succ[seven]
  let nine: nat_t = succ[eight]
  let ten: nat_t = succ[nine]

  eval! zero
  eval! one
  eval! two
  eval! three

  letrec nat_rec:
    (C: (_: nat_t) -> univ) ->
    (a: C(zero)) ->
    (g: (n: nat_t) -> (_: C(n)) -> C(succ[n])) ->
    (n: nat_t) -> C(n) =
  C => a => g => match {
    zero[] => a;
    succ[prev] => g(prev, nat_rec(C, a, g, prev));
  }

  letrec add: (nat_t, nat_t) -> nat_t = match {
    zero[] => y => y;
    succ[prev] => y => succ[add(prev, y)];
  }

  eq! add(two, two) four

  let double: (nat_t) -> nat_t = (x) => add(x, x)

  letrec mul: (nat_t, nat_t) -> nat_t = match {
    zero[] => y => zero;
    succ[prev] => y => add(y, mul(prev, y));
  }

  let square: (nat_t) -> nat_t = x => mul(x, x)

  letrec nat_eq: (nat_t, nat_t) -> bool_t = match {
    zero[] => match {
      zero[] => true;
      succ[_] => false;
    };
    succ[x_prev] => match {
      zero[] => false;
      succ[y_prev] => nat_eq(x_prev, y_prev);
    };
  }

  letrec list_t: (A: univ) -> univ =
  A => sum {
    nil;
    cons (_: A) ** list_t(A);
  }

  letrec list_append:
    (A: univ) ->
    (x: list_t(A)) ->
    (y: list_t(A)) -> list_t(A) =
  A => match {
    nil[] => y => y;
    cons[head, tail] => y => cons[head, list_append(A, tail, y)];
  }

  eq! double(one) two
  eq! double(two) four
  eq! double(three) six

  eq! square(one) one
  eq! square(two) four
  eq! square(three) nine

  eq! nat_eq(one, one) true
  eq! nat_eq(two, two) true
  eq! nat_eq(one, two) false
  eq! nat_eq(two, one) false

  // eval! list_append(nat_t)
  // eval! list_append(nat_t, nil[])
  // eval! list_append(nat_t, nil[], nil[])

  let two_zeros: list_t(nat_t) =
  cons[zero, cons[zero, nil[]]]

  eval! list_append(nat_t, two_zeros, two_zeros)

  let one_two_three: list_t(nat_t) =
  cons[one, cons[two, cons[three, nil[]]]]

  // eval! list_append(nat_t, one_two_three, one_two_three)
  """

  var module = Parser(grammar.lexer, grammar.module).parse(code) match {
    case Right(tree) => grammar.module_matcher(tree)
    case Left(error) =>
      println(s"[paper_test] parse error")
      println(s"- code: ${code}")
      println(s"- error: ${error}")
      throw new Exception()
  }

  module.check()
  module.run()
}
