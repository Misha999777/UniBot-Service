package tk.tcomad.unibot.load.actions

import io.gatling.core.Predef._
import io.gatling.core.structure.ChainBuilder
import io.gatling.http.Predef._

object Weeks {

    val add: ChainBuilder = exec(http("Add week")
    .post("/weeks")
    .header("Authorization", "Bearer ${accessToken}")
    .header(HttpHeaderNames.ContentType, HttpHeaderValues.ApplicationJson)
    .header(HttpHeaderNames.Accept, HttpHeaderValues.ApplicationJson)
    .body(StringBody("""{ "date": "${date}"}""")).asJson
    .check(status.is(201)))
    .pause(2)

    val get: ChainBuilder = exec(http("Get weeks")
    .get("/weeks")
    .header("Authorization", "Bearer ${accessToken}")
    .header(HttpHeaderNames.ContentType, HttpHeaderValues.ApplicationJson)
    .header(HttpHeaderNames.Accept, HttpHeaderValues.ApplicationJson)
    .check(status.is(200),
    jsonPath("$._embedded.weeks[*]").ofType[Map[String,Any]].findAll.saveAs("list")))
    .pause(2)

    val update: ChainBuilder =
    foreach("${list}", "item") {
    exec(http("Update week")
    .put("/weeks/${item.id}")
    .header("Authorization", "Bearer ${accessToken}")
    .header(HttpHeaderNames.ContentType, HttpHeaderValues.ApplicationJson)
    .header(HttpHeaderNames.Accept, HttpHeaderValues.ApplicationJson)
    .body(StringBody("""{ "date": "${date}"}""")).asJson
    .check(status.is(200)))
    .pause(2)
    }

    val delete: ChainBuilder =
    foreach("${list}", "item") {
    exec(http("Delete week")
    .delete("/weeks/${item.id}")
    .header("Authorization", "Bearer ${accessToken}")
    .header(HttpHeaderNames.ContentType, HttpHeaderValues.ApplicationJson)
    .header(HttpHeaderNames.Accept, HttpHeaderValues.ApplicationJson)
    .check(status.is(204)))
    .pause(2)
    }

}
