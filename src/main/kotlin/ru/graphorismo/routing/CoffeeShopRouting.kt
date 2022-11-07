package ru.graphorismo.routing

import io.ktor. http.cio.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ru.graphorismo.data.authentication.AuthController
import ru.graphorismo.data.authentication.Credentials
import ru.graphorismo.data.database.products.ProductsDataBase
import ru.graphorismo.data.products.Order
import ru.graphorismo.data.products.Product
import ru.graphorismo.data.repositories.ProductsRepository
import ru.graphorismo.data.responses.CartResponse


fun Routing.getCart(){

}

fun Routing.putCart(){

    var authController = AuthController.getInstance()
    var productsDataBase = ProductsDataBase.getInstance()

    post("/cart"){
        var receivedOrder : Order = call.receive()
        var response = CartResponse("net_error")
        if(call.request.queryParameters["token"] != null) {
            var token = call.request.queryParameters["token"]
            var login = authController.getLoginForToken(token!!)
            if (login != null) {
                response = productsDataBase.putOrderToTheCartForLogin(receivedOrder, login)
            }
        }
        call.respond(response)
    }
}

fun Routing.getProducts(){
    var productsRepository = ProductsRepository.getInstance()
    var authController = AuthController.getInstance()

    get("/products") {
        var products = listOf<Product>()
        if(call.request.queryParameters["token"] != null)
        {
            var token = call.request.queryParameters["token"]
            var login = authController.getLoginForToken(token!!)
            if( login != null
                && call.request.queryParameters["type"] != null){
            var productsType = call.request.queryParameters["type"]!!
            products = productsRepository.getProductsOfType(productsType)
        }

        }
        call.respond(products)
    }
}

fun Routing.putLogin(){

    var authController = AuthController.getInstance()

    post("/login"){
        var receivedCredentials : Credentials = call.receive()
        var response = authController.authenticate(receivedCredentials)
        call.respond(response)
    }
}

fun Routing.putRegistration(){

    var authController = AuthController.getInstance()

    post("/registrate"){
        var receivedCredentials : Credentials = call.receive()
        var response = authController.registrate(receivedCredentials)
        call.respond(response)
    }
}

