package com.acs.scala.server.mad

import java.util.concurrent.atomic.AtomicBoolean

import com.acs.scala.server.mad.utils.{LogLevel, StopWatch}

trait App extends LogSupport {
  private var configureActions: List[() => Unit] = List()
  private var startActions: List[() => Unit] = List()
  private var stopActions: List[() => Unit] = List()

  private val started = new AtomicBoolean(false)

  val name: String

  def main(args: Array[String]): Unit = {
    sys.addShutdownHook {
      stop()
    }
    start()
  }

  def onConfigure(action: => Unit):Unit = configureActions = configureActions ++ List(() => action)

  def onStart(action: => Unit):Unit = startActions = startActions  ++ List(() => action)

  def onStop(action: => Unit):Unit = stopActions = stopActions  ++ List(() => action)

  def start(): Unit = {
    if (started.compareAndSet(false, true)) {
      executeAll("Configure", configureActions)
      executeAll("Start", startActions)
    }
  }

  def stop(): Unit = {
    if (started.compareAndSet(true, false)) {
      executeAll("Stop", stopActions)
    }
  }

  private def executeAll(actionName: String, actions: List[() => Unit]): Unit = {
    val stopWatch = new StopWatch().start()
    try {
      actions.foreach(_ ())
    } catch {
      case t: Throwable =>
        log.warn(s"Unable to ${actionName.toLowerCase}", t)
    } finally {
      stopWatch.printElapseTime(s"$actionName $name", log, LogLevel.INFO)
    }
  }
}
