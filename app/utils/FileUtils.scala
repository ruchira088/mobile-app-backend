package utils

import java.nio.ByteBuffer
import java.nio.channels.{AsynchronousFileChannel, CompletionHandler}
import java.nio.file.{Path, StandardOpenOption}

import scala.concurrent.{Future, Promise}

object FileUtils
{
  val DEFAULT_BUFFER_SIZE = 1024

  def readFile(path: Path): Future[String] =
  {
    val buffer = ByteBuffer.allocate(DEFAULT_BUFFER_SIZE)
    val promise = Promise[String]

    AsynchronousFileChannel
      .open(path, StandardOpenOption.READ)
      .read(buffer, 0, buffer, new CompletionHandler[Integer, ByteBuffer]
      {
        override def failed(throwable: Throwable, byteBuffer: ByteBuffer) = {
          promise.failure(throwable)
        }

        override def completed(integer: Integer, byteBuffer: ByteBuffer) = {
          promise.success(new String(byteBuffer.array()).trim)
        }
      })

    promise.future
  }
}
