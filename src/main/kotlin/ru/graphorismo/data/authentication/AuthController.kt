package ru.graphorismo.data.authentication

import ru.graphorismo.data.responses.RegistrateResponse
import ru.graphorismo.data.repositories.AuthenticationRepository
import ru.graphorismo.data.responses.AuthResponse
import kotlin.random.Random

class AuthController {

    companion object{
        private var authController: AuthController? = null

        fun getInstance(): AuthController{
            if(authController == null){
                authController = AuthController()
            }
            return authController!!
        }
    }


    private var authenticationRepository = AuthenticationRepository.getInstance()
    private var tokens : MutableList<String> = mutableListOf()

    private fun generateToken(): String{
        var a = Random(123456789).nextInt()
        var b = Random(987654321).nextInt()
        var token = a*a*b*b
        return token.toString()
    }

    fun authenticate(credentials: Credentials) : AuthResponse {
        var rightCredentials = authenticationRepository.getCredentialsForLogin(credentials.login)
        if ( rightCredentials != null
            && credentials.login == rightCredentials.login
            && credentials.password == rightCredentials.password){
            var token = generateToken()
            tokens.add(token)
            return AuthResponse("ok", token)
        }else{
            return AuthResponse("deny","")
        }
    }

    fun registrate(credentials: Credentials) : RegistrateResponse {
        var registrationResult = authenticationRepository.putCredentialsForRegistration(credentials)
        if (registrationResult == false){
            return RegistrateResponse("deny")
        }
        else
        {
            return RegistrateResponse("ok")
        }
    }

    fun checkTocken(token: String): Boolean{
        return tokens.contains(token)
    }
}