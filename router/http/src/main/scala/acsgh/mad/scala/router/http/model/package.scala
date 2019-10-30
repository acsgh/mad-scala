package acsgh.mad.scala.router.http

package object model {

  trait RouteError {
    val status: ResponseStatus
  }

  case class RouteMessage(status: ResponseStatus, message: Option[String] = None) extends RouteError

  case class RouteException(status: ResponseStatus, exception: Exception) extends RouteError

  type RouteSuccess = Left[RouteError, Response]

  type RouteResult = Either[RouteError, Response]

  type Route[T] = RequestContext => T => RouteResult
}
