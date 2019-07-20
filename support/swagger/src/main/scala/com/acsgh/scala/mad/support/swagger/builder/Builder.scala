package com.acsgh.scala.mad.support.swagger.builder

trait Builder[T <: Builder[_, _], B] {

  val parent: T

  def root: OpenApiBuilder = parent.root

  protected val delegate: B

  def build: B = delegate
}
