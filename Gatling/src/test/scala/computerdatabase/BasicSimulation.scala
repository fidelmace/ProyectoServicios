package perro

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._


class BasicSimulation extends Simulation {

  val httpProtocol = http
    //.baseUrl("http://192.168.100.116:8000/apimicros/microservicio") // Here is the root for all relative URLs
    .baseUrl("http://192.168.18.119:8085/pas-repcrystal") // Here is the root for all relative URLs
    .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8") // Here are the common headers
 
    .acceptEncodingHeader("gzip, deflate")
    .acceptLanguageHeader("en-US,en;q=0.5")
    .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:16.0) Gecko/20100101 Firefox/16.0")

  val scn = scenario("Consulta Usuarios de impresoras") // Nombre de  scenario y se pueden encadenar is a chain of requests and pauses
    .exec(http("Consulta Impresoras")
      //.get("/users"))
      .get("/printers"))
    .pause(7) // Note that Gatling has recorded real time pauses
    //.exec(http("Validar Ping")
    //  .get("/ping"))
    //.pause(7) // Note that Gatling has recorded real time pauses
    
  setUp(
    scn.inject(
      atOnceUsers(1),
      rampUsers(1).during(5.seconds),
    ).protocols(httpProtocol))
}
