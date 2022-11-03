package ru.graphorismo.data.repositories

import ru.graphorismo.data.authentication.Credentials
import ru.graphorismo.data.database.authentication.AuthenticationDataBase

class AuthenticationRepository {

    var authenticationDataBase = AuthenticationDataBase()
    //some day here could be a cache


    fun getCredentialsForLogin(login: String): Credentials?{
        return authenticationDataBase.getCredentialsForLogin(login)
    }


}