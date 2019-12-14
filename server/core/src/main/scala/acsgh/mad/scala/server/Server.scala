package acsgh.mad.scala.server

import java.util.concurrent.atomic.AtomicBoolean

import acsgh.mad.scala.server.router.http.HttpRouter
import acsgh.mad.scala.server.router.ws.WSRouter
import com.acsgh.common.scala.log.LogSupport

trait Server extends LogSupport {
  val name: String
  val productionMode: Boolean
  val ipAddress: String
  val httpPort: Option[Int]
  val httpsPort: Option[Int]
  val sslConfig: Option[SSLConfig]
  val readerIdleTimeSeconds: Int
  val writerIdleTimeSeconds: Int
  val httpRouter: HttpRouter
  val wsRouter: WSRouter

  private val _started = new AtomicBoolean(false)

  def started: Boolean = _started.get()

  def start(): Unit = {
    if (_started.compareAndSet(false, true)) {
      doStart()
    }
  }

  def stop(): Unit = {
    if (_started.compareAndSet(true, false)) {
      doStop()
      httpRouter.close()
      wsRouter.close()
    }
  }

  protected def doStart(): Unit

  protected def doStop(): Unit
}
