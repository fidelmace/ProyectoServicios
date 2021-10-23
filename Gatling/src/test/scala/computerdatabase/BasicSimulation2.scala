package computerdatabase

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._
import io.gatling.core.scenario.Simulation



class BasicSimulation2 extends Simulation {


 //val headers_10 = Map("API-KEY" -> "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ0ZWwtcGVkaWRvcy1wcm9kdWNlciIsImlkIjo1MDk4LCJleHAiOjE2MzQ5Mjg2ODgsImlhdCI6MTYzNDg0MjI4OCwiYXV0aG9yaXRpZXMiOlt7ImF1dGhvcml0eSI6IlJPTEVfUzAzMF9BRE1JTiJ9XX0.CW8WVVrfu-WyWfvQmahcEwT3wWBZJhCgXQBuAaJ0ZXLWiXOhQM-OiDGlEAKZYHEdFqZh5621z2fUYx_RqP6TGA", "Authorization Bearer" -> "bearer")
//    val sessionHeaders = Map("Authorization" -> "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ0ZWwtcGVkaWRvcy1wcm9kdWNlciIsImlkIjo1MDk4LCJleHAiOjE2MzQ5Mjg2ODgsImlhdCI6MTYzNDg0MjI4OCwiYXV0aG9yaXRpZXMiOlt7ImF1dGhvcml0eSI6IlJPTEVfUzAzMF9BRE1JTiJ9XX0.CW8WVVrfu-WyWfvQmahcEwT3wWBZJhCgXQBuAaJ0ZXLWiXOhQM-OiDGlEAKZYHEdFqZh5621z2fUYx_RqP6TGA",
//                           "Content-Type" -> "application/json")

//val sessionHeaders = Map("Authorization" -> "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ0ZWwtcGVkaWRvcy1wcm9kdWNlciIsImlkIjo1MDk4LCJleHAiOjE2MzQ5Mjg2ODgsImlhdCI6MTYzNDg0MjI4OCwiYXV0aG9yaXRpZXMiOlt7ImF1dGhvcml0eSI6IlJPTEVfUzAzMF9BRE1JTiJ9XX0.CW8WVVrfu-WyWfvQmahcEwT3wWBZJhCgXQBuAaJ0ZXLWiXOhQM-OiDGlEAKZYHEdFqZh5621z2fUYx_RqP6TGA",
//                           "Content-Type" -> "application/json",
 //                          "Accept" -> "application/json")
 val headers_1 = Map(
            "Accept" -> """text/css,*/*;q=0.1""",
            "Authorization" -> "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ0ZWwtcGVkaWRvcy1wcm9kdWNlciIsImlkIjo1MDk4LCJleHAiOjE2MzQ5NDcwMzksImlhdCI6MTYzNDg2MDYzOSwiYXV0aG9yaXRpZXMiOlt7ImF1dGhvcml0eSI6IlJPTEVfUzAzMF9BRE1JTiJ9XX0.Q4yD5IkCjXu7TwICx02nV96wqQDaDfnu3ogR5pjvI3M1m7MJJSp4nvlSI3bvENUgHtZerTnbjwu-tk2ndVyXwA"
    )

  val httpProtocol = http
    .baseUrl("https://apiqa.truper.com:8081/context/tel/pedidos-producer/api/V1")// Here is the root for all relative URLs
   
  val scn = scenario("Pedidos Nuevos") // Nombre de  scenario y se pueden encadenar is a chain of requests and pauses
    .exec(http("Consulta Pedidos")
    .get("/pedido")
    .headers(headers_1)
    .check(status.is(404))
    .body(StringBody("""{
"@id": 1,
  "id": 1633456602119,
  "btGew": 228,
  "orderType": 8,
  "plazo": 90,
  "centroSum": "SFOR",
  "cliente": {
    "@id": 1,
    "cliente": "516082",
    "destinatario": "516082"
  },
  "fechaReclamacion": null,
  "monto": 19253.37,
  "usuario": "Iecastillom",
  "volum": 1763117.42,
  "gerencia": "SJ80",
  "zona": "J243",
  "observaciones": "",
  "altitud": 0,
  "longitud": -1,
  "latitud": -1,
  "md5hash": "7f5c1026678e60d2d6f09de5355ffeb3",
  "partidas": 6,
  "valorPedido": 19253.365441398,
  "savedId": 1633456215948,
  "status": 1,
  "fecha": 20211005,
  "fechaStr": "2021/oct/05 12:56:42.197",
  "centroSuministro": null,
  "docType": "ZPTN",
  "salesOrg": "SJIL",
  "distrChan": "FT",
  "division": "01",
  "salesGrp": "369",
  "reqDateH": null,
  "purchDate": "2021-10-05",
  "poMethod": "TIPO",
  "purchNoC": "W110051250",
  "pmnttrms": 90,
  "shipcond": "",
  "loadedFromFile": false,
  "idescuentos": [],
  "orderItemsIn": [
    {
      "@id": 1,
      "material": "11753",
      "reqqty": 2,
      "cdpunt3": 0,
      "cdtype3": "",
      "condvalue": 0,
      "condtype": ""
    },
    {
      "@id": 2,
      "material": "16743",
      "reqqty": 2,
      "cdpunt3": 0,
      "cdtype3": "",
      "condvalue": 0,
      "condtype": ""
    },
    {
      "@id": 3,
      "material": "29975",
      "reqqty": 2,
      "cdpunt3": 0,
      "cdtype3": "",
      "condvalue": 0,
      "condtype": ""
    },
    {
      "@id": 4,
      "material": "11740",
      "reqqty": 2,
      "cdpunt3": 0,
      "cdtype3": "",
      "condvalue": 0,
      "condtype": ""
    },
    {
      "@id": 5,
      "material": "22647",
      "reqqty": 2,
      "cdpunt3": 0,
      "cdtype3": "",
      "condvalue": 0,
      "condtype": ""
    },
    {
      "@id": 6,
      "material": "45272",
      "reqqty": 2,
      "cdpunt3": 0,
      "cdtype3": "",
      "condvalue": 0,
      "condtype": ""
    }
  ],
  "mensajeError": null,
  "discount": false,
  "origen": 0,
  "discountMount": null,
  "idescZr24Zpnp": [],
  "centrega": "N"
} """)).asJson)


  .pause(7) // Note that Gatling has recorded real time pauses
    //.exec(http("Validar Ping")
    //  .get("/ping"))
    //.pause(7) // Note that Gatling has recorded real time pauses
    
  setUp(
    scn.inject(atOnceUsers(10),
    rampUsers(50).during(60.seconds),
    rampUsers(20).during(60.seconds),
).protocols(httpProtocol))

}
