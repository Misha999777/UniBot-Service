package tk.tcomad.unibot.load

import org.slf4j.LoggerFactory
import tk.tcomad.unibot.load.ConfigKeys._

import scala.sys.SystemProperties

object ConfigKeys {
  val ARG_BASE_URL = "baseUrl"
  val ARG_BASE_PATH = "basePath"
  val ARG_MAX_USERS = "maxUsers"
  val ARG_RAMP_SECS = "rampSeconds"
  val ARG_SUSTAINED_LOAD_SECS = "sustainedLoadSeconds"
}

object ConfigHelper {

  val props = new SystemProperties
  val baseUrl: String = props.getOrElse(ARG_BASE_URL, "https://dolores.tcomad.tk")
  val basePath: String = props.getOrElse(ARG_BASE_PATH, "/unibot")

  val maxUsers: Int = props.getOrElse(ConfigKeys.ARG_MAX_USERS, "1").toInt
  val rampSeconds: Int = props.getOrElse(ARG_RAMP_SECS, "60").toInt
  val sustainedLoadSeconds: Int = props.getOrElse(ARG_SUSTAINED_LOAD_SECS, "300").toInt

  {
    val logger = LoggerFactory.getLogger(getClass)
    logger.info("Hitting: {}", baseUrl + basePath)
    logger.info("Max Users: {}", maxUsers)
    logger.info("Ramp Up Seconds: {}", rampSeconds)
    logger.info("Sustained Load Seconds: {}", sustainedLoadSeconds)
  }
}
