package acsgh.mad.scala.core

import java.util.concurrent.atomic.AtomicLong
import java.util.concurrent.{TimeUnit, _}

import com.acsgh.common.scala.log.LogSupport

case class WorkerExecutor(name: String, threads: Int) extends LogSupport {
  private val index = new AtomicLong(0)
  private val executor = Executors.newFixedThreadPool(threads, (runnable: Runnable) => new Thread(runnable, s"$name-${index.incrementAndGet()}"))

  def submit[T](action: Callable[T]): Future[T] = {
    val future = new FutureTask[T](action)
    execute(future)
    future
  }

  def execute(action: Runnable): Unit = executor.execute(action)

  def shutdownGracefully(): Unit = {
    executor.shutdown() // Disable new tasks from being submitted

    try // Wait a while for existing tasks to terminate
      if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
        executor.shutdownNow // Cancel currently executing tasks

        // Wait a while for tasks to respond to being cancelled
        if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
          log.error("Executor {} did not terminate", name)
        }
      }
    catch {
      case ie: InterruptedException =>
        // (Re-)Cancel if current thread also interrupted
        executor.shutdownNow
        // Preserve interrupt status
        Thread.currentThread.interrupt()
    }
  }

  def shutdownNow(): Unit = executor.shutdownNow()

}
