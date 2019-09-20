package xieyuheng.cicada

sealed trait Decl
final case class DeclLet(name: String, t: Exp, body: Exp) extends Decl
final case class DeclLetType(name: String, t: Exp) extends Decl
final case class DeclFn(name: String, args: Map[String, Exp], dep_t: Exp, body: Exp) extends Decl
final case class DeclFnType(name: String, args: Map[String, Exp], dep_t: Exp) extends Decl
final case class DeclClub(name: String, members: List[Member], fileds: List[(String, Exp, Option[Exp])]) extends Decl
final case class DeclRecord(name: String, super_names: List[String], decls: List[Decl]) extends Decl

case class Member(name: String, club_name: String, fileds: List[(String, Exp, Option[Exp])])
