package tk.tcomad.unibot.load.actions

import io.gatling.core.Predef._
import io.gatling.core.structure.ChainBuilder
import io.gatling.http.Predef._

object Auth {
  val chain: ChainBuilder = exec(http("Auth")
    .post("/auth")
    .header(HttpHeaderNames.ContentType, HttpHeaderValues.ApplicationJson)
    .header(HttpHeaderNames.Accept, HttpHeaderValues.ApplicationJson)
    .body(StringBody("""{ "username": "${username}", "api": "${api}" }""")).asJson
    .check(jsonPath("$.accessToken").exists.saveAs("accessToken")))
    .pause(2)
}
