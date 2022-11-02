package ru.graphorismo.routing

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ru.graphorismo.data.authentication.AuthResponse
import ru.graphorismo.data.authentication.Credentials

fun Routing.putLogin(){
    post("/login"){
        var credentials : Credentials = call.receive()
        if (credentials.login == "login"){
            call.respond(AuthResponse("ok","12345"))
        }
    }
}