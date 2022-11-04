package ru.graphorismo.routing

import io.ktor. http.cio.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ru.graphorismo.coffeeshop.data.auth.RegistrateResponse
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

    var authenticationRepository = AuthenticationRepository.getInstance()

    post("/login"){
        var receivedCredentials : Credentials = call.receive()
        var rightCredentials = authenticationRepository.getCredentialsForLogin(receivedCredentials.login)
        if (rightCredentials == null || rightCredentials.password != receivedCredentials.password){
            call.respond(AuthResponse("deny",""))
        }
        else
        {
            call.respond(AuthResponse("ok","12345"))
        }
    }
}

fun Routing.putRegistration(){

    var authenticationRepository = AuthenticationRepository.getInstance()

    post("/registrate"){
        var receivedCredentials : Credentials = call.receive()
        var registrationResult = authenticationRepository.putCredentialsForRegistration(receivedCredentials)
        if (registrationResult == false){
            call.respond(RegistrateResponse("deny"))
        }
        else
        {
            call.respond(RegistrateResponse("ok"))
        }
    }
}

