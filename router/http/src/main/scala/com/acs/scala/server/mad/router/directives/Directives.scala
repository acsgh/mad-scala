package com.acs.scala.server.mad.router.directives

import com.acs.scala.server.mad.router.{Request, Response, ResponseBuilder}

trait Directives {
  def pass: Directive0 = Directive.Empty

  def provide[T](value: T): Directive1[T] = tprovide(Tuple1(value))

  def tprovide[L: Tuple](values: L): Directive[L] = Directive { _(values) }

  def header(name:String)(action: String => Response)(implicit request:Request) : Response

  def query(name:String)(action: String => Response) : Response
}
