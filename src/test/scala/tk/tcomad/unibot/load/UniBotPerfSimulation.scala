package tk.tcomad.unibot.load

import io.gatling.core.Predef._
import io.gatling.core.scenario.Simulation
import io.gatling.core.structure.ScenarioBuilder
import io.gatling.http.Predef._
import io.gatling.http.protocol.HttpProtocolBuilder
import tk.tcomad.unibot.load.Feeders._
import tk.tcomad.unibot.load.actions._

import scala.concurrent.duration._

class UniBotPerfSimulation extends Simulation {

  val httpProtocol: HttpProtocolBuilder = http
    .baseUrl(ConfigHelper.baseUrl + ConfigHelper.basePath)

  val testDuration: Int = 2 * ConfigHelper.rampSeconds + ConfigHelper.sustainedLoadSeconds

  val walkThroughoutSidebar: ScenarioBuilder = scenario("Load test UniBot")
    .feed(credentialsGenerator)
    .feed(randomNameGenerator)
    .feed(randomDataGenerator)
    .feed(randomDateGenerator)
    .during(ConfigHelper.sustainedLoadSeconds) {
      exec(
        Auth.chain,
        Apps.add, Apps.get, Apps.update, Apps.delete,
        Books.add, Books.get, Books.update, Books.delete,
        Lectures.add, Lectures.get, Lectures.update, Lectures.delete,
        Lessons.add, Lessons.get, Lessons.update, Lessons.delete,
        Student.add, Student.get, Student.update, Student.delete,
        Teacher.add, Teacher.get, Teacher.update, Teacher.delete,
        Weeks.add, Weeks.get, Weeks.update, Weeks.delete
      )
    }

  private val scenarios = List(walkThroughoutSidebar)
    .map(scenario => scenario.inject(
      rampUsers(ConfigHelper.maxUsers) during ConfigHelper.rampSeconds.seconds,
      nothingFor(ConfigHelper.sustainedLoadSeconds.seconds)
    ))

  setUp(scenarios)
    .maxDuration(testDuration.seconds)
    .protocols(httpProtocol)
    .assertions(
      global.failedRequests.percent.is(0)
    )
}
