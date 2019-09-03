package xieyuheng.partech.example

import xieyuheng.partech._
import xieyuheng.partech.ruleDSL._
import xieyuheng.partech.example.collection._

object tdh extends ExampleRule {

  // regular grammar

  val sentences = Seq(
    "t,d&h",
  )

  val non_sentences = Seq(
    "t,d,h",
  )

  def main = Rule(
    "tdh", Map(
      "t" -> Seq("t"),
      "d" -> Seq("d"),
      "h" -> Seq("h"),
      "tdh_list" -> Seq(tdh_list)))

  def treeToMainType = ???

  def tdh_list = Rule(
    "tdh_list", Map(
      "t" -> Seq("t", tdh_list_tail),
      "d" -> Seq("d", tdh_list_tail),
      "h" -> Seq("h", tdh_list_tail)))

  def tdh_list_tail: Rule = Rule(
    "tdh_list_tail", Map(
      "list" -> Seq(",", tdh_list),
      "t" -> Seq("&t"),
      "d" -> Seq("&d"),
      "h" -> Seq("&h")))

}
