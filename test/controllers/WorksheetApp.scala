package controllers

import play.api.libs.json.Json
import utils.ConfigUtils
import utils.ConfigUtils.KeyValuePair

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

object WorksheetApp
{
  def main(args: Array[String]): Unit =
  {
    val future = ConfigUtils.getApplicationInfo()
      .map(_.foldLeft(Json.obj()) {
        case (json, KeyValuePair(key, value)) => json ++ Json.obj(key -> value)
      })

    println(Await.result(future, 10 seconds))
  }
}
