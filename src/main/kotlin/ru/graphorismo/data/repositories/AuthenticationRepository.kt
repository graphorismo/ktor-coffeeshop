package ru.graphorismo.data.repositories

import ru.graphorismo.data.authentication.Credentials
import ru.graphorismo.data.database.authentication.AuthenticationDataBase

class AuthenticationRepository private constructor() {

    var authenticationDataBase = AuthenticationDataBase()
    //some day here could be a cache


    fun getCredentialsForLogin(login: String): Credentials?{
        return authenticationDataBase.getCredentialsForLogin(login)
    }

    fun putCredentialsForRegistration(credentials: Credentials) : Boolean{
        return authenticationDataBase.putCredentialsForRegistration(credentials)
    }

    companion object {
        private var instance : AuthenticationRepository? = null

        fun  getInstance(): AuthenticationRepository {
            if (instance == null)  // NOT thread safe!
                instance = AuthenticationRepository()

            return instance!!
        }

    }


}