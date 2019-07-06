package com.acs.scala.server.mad.router.directives

abstract class Directive[L](implicit val ev: Tuple[L]) {
  def tapply(f: L ⇒ Route): Route
}

object Directive {

  val Empty: Directive0 = Directive(_(()))

  /**
    * Constructs a directive from a function literal.
    */
  def apply[T: Tuple](f: (T ⇒ Route) ⇒ Route): Directive[T] =
    new Directive[T] {
      def tapply(inner: T ⇒ Route) = f(inner)
    }

}
