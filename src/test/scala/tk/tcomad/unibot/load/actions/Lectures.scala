package tk.tcomad.unibot.load.actions

import io.gatling.core.Predef._
import io.gatling.core.structure.ChainBuilder
import io.gatling.http.Predef._

object Lectures {

  val add: ChainBuilder = repeat(5, "n") {
    exec(http("Add lecture")
      .post("/lectures")
      .header("Authorization", "Bearer ${accessToken}")
      .header(HttpHeaderNames.ContentType, HttpHeaderValues.ApplicationJson)
      .header(HttpHeaderNames.Accept, HttpHeaderValues.ApplicationJson)
      .body(StringBody("""{ "name": "${name}", "data": "${data}" }""")).asJson
      .check(status.is(201)))
      .pause(2)
  }

  val get: ChainBuilder = exec(http("Get lectures")
    .get("/lectures")
    .header("Authorization", "Bearer ${accessToken}")
    .header(HttpHeaderNames.ContentType, HttpHeaderValues.ApplicationJson)
    .header(HttpHeaderNames.Accept, HttpHeaderValues.ApplicationJson)
    .check(status.is(200),
      jsonPath("$._embedded.lectures[*]").ofType[Map[String,Any]].findAll.saveAs("list")))
    .pause(2)

  val update: ChainBuilder =
    foreach("${list}", "item") {
      exec(http("Update lecture")
        .put("/lectures/${item.id}")
        .header("Authorization", "Bearer ${accessToken}")
        .header(HttpHeaderNames.ContentType, HttpHeaderValues.ApplicationJson)
        .header(HttpHeaderNames.Accept, HttpHeaderValues.ApplicationJson)
        .body(StringBody("""{ "name": "${name}", "data": "${data}" }""")).asJson
        .check(status.is(200)))
        .pause(2)
    }

  val delete: ChainBuilder =
    foreach("${list}", "item") {
      exec(http("Delete lecture")
        .delete("/lectures/${item.id}")
        .header("Authorization", "Bearer ${accessToken}")
        .header(HttpHeaderNames.ContentType, HttpHeaderValues.ApplicationJson)
        .header(HttpHeaderNames.Accept, HttpHeaderValues.ApplicationJson)
        .check(status.is(204)))
        .pause(2)
    }

}
