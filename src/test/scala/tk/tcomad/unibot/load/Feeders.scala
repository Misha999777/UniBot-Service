package tk.tcomad.unibot.load

import java.text.SimpleDateFormat
import java.util.Date

import io.gatling.core.Predef._
import io.gatling.core.feeder.BatchableFeederBuilder

import scala.util.Random

object Feeders {
  private def format( d: Date ): String = new SimpleDateFormat("dd-MM-yyyy").format(d)

  val credentialsGenerator: BatchableFeederBuilder[String] = csv(s"users/users.csv").shuffle.circular
  val randomNameGenerator: Iterator[Map[String, String]] = Iterator.continually(Map("name" -> Random.alphanumeric.take(8).mkString.toUpperCase))
  val randomDataGenerator: Iterator[Map[String, String]] = Iterator.continually(Map("data" -> Random.alphanumeric.take(15).mkString.toUpperCase))
  val randomDateGenerator: Iterator[Map[String, String]] = Iterator.continually(Map("date" -> format(new Date())))
}
