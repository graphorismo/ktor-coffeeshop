package ru.graphorismo.routing

import io.ktor. http.cio.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ru.graphorismo.coffeeshop.data.auth.RegistrateResponse
import ru.graphorismo.data.authentication.AuthController
import ru.graphorismo.data.authentication.AuthResponse
import ru.graphorismo.data.authentication.Credentials
import ru.graphorismo.data.products.Product
import ru.graphorismo.data.repositories.AuthenticationRepository
import ru.graphorismo.data.repositories.ProductsRepository

fun Routing.getProducts(){
    var productsRepository = ProductsRepository.getInstance()

    get("/products") {
        var products = listOf<Product>()
        if(call.request.queryParameters["type"] != null){
            var productsType = call.request.queryParameters["type"]!!
            products = productsRepository.getProductsOfType(productsType)
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

