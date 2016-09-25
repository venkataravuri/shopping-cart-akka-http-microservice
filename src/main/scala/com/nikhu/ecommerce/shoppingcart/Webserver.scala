package com.nikhu.ecommerce.shoppingcart

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.marshalling.Marshal
import akka.http.scaladsl.model.{HttpResponse, MessageEntity}
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import com.typesafe.config.ConfigFactory
import com.typesafe.scalalogging.LazyLogging
import spray.json.DefaultJsonProtocol

/**
  * Created by vsubbarravuri on 9/25/16.
  */

// collect your json format instances into a support trait:
trait JsonSupport extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val userFormat = jsonFormat3(User)
  implicit val productFormat = jsonFormat5(Product)
  implicit val shoppingCartItemFormat = jsonFormat2(ShoppingCartItem)
  implicit val shoppingCartFormat = jsonFormat4(ShoppingCart)
}

object WebServer extends LazyLogging with JsonSupport with App {


  implicit val actorSystem = ActorSystem("shopping-cart-akka-http-microservice")
  implicit val materializer = ActorMaterializer()
  // needed for the future flatMap/onComplete in the end
  implicit val executionContext = actorSystem.dispatcher


  val nexus = Product("sku-272", 0, "Nexus 5", "Mobile", 339.0d)
  val john = User("john", 0, "Jhon Smith")
  val shoppingCartItem = ShoppingCartItem(nexus, 1)
  val shoppingCart = ShoppingCart("1234", 0, john, List.empty[ShoppingCartItem] :+ shoppingCartItem)
  val shoppingCarts = List.empty[ShoppingCart] :+ shoppingCart

  val routes = {
    logRequestResult("shopping-cart-akka-http-microservice") {
      pathPrefix("cart" / Remaining) { cartId =>
        get {
          complete {
            println(s"cartId: $cartId")
            shoppingCarts.filter(_.id == cartId)
          }
        }
      }
    }
  }

  val config = ConfigFactory.load()
  val (interface, port) = (config.getString("http.interface"), config.getInt("http.port"))
  val bindingFuture = Http().bindAndHandle(handler = routes, interface = interface, port = port)

  logger.info(s"Server online at, http://$interface:$port/")

  bindingFuture.onFailure {
    case ex: Exception ⇒
      logger.error(s"Failed to bind to $interface:$port!", ex)
  }
  sys.addShutdownHook(
    bindingFuture
      .flatMap(_.unbind()) // trigger unbinding from the port
      .onComplete(_ => actorSystem.terminate()) // and shutdown when done
  )
}