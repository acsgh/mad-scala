package com.acsgh.scala.mad

import java.util.concurrent.atomic.AtomicBoolean

import com.acsgh.common.scala.log.{LogLevel, LogSupport}
import com.acsgh.common.scala.time.StopWatch

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

  def onConfigure(action: => Unit): Unit = configureActions = configureActions ++ List(() => action)

  def onStart(action: => Unit): Unit = startActions = startActions ++ List(() => action)

  def onStop(action: => Unit): Unit = stopActions = stopActions ++ List(() => action)

  def start(): Unit = {
    if (started.compareAndSet(false, true)) {
      val stopWatch = StopWatch.createStarted()
      try {
        executeAll("Configure", configureActions)
        executeAll("Start", startActions)
      } finally {
        stopWatch.printElapseTime(s"Server $name started", log, LogLevel.INFO)
      }
    }
  }

  def stop(): Unit = {
    if (started.compareAndSet(true, false)) {
      val stopWatch = StopWatch.createStarted()
      try {
        executeAll("Stop", stopActions)
      } finally {
        stopWatch.printElapseTime(s"Server $name stopped", log, LogLevel.INFO)
      }
    }
  }

  private def executeAll(actionName: String, actions: List[() => Unit]): Unit = {
    val stopWatch = StopWatch.createStarted()
    try {
      actions.foreach(_ ())
    } catch {
      case t: Throwable =>
        log.warn(s"Unable to ${actionName.toLowerCase}", t)
    } finally {
      stopWatch.printElapseTime(s"Stage $actionName done", log, LogLevel.TRACE)
    }
  }
}
