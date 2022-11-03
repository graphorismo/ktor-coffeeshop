package ru.graphorismo.routing

import io.ktor.http.cio.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ru.graphorismo.data.authentication.AuthResponse
import ru.graphorismo.data.authentication.Credentials
import ru.graphorismo.data.repositories.AuthenticationRepository

fun Routing.putLogin(){

    var authenticationRepository = AuthenticationRepository()

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