package ru.graphorismo.data.database.authentication

import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.exceptions.ExposedSQLException
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction
import ru.graphorismo.data.authentication.Credentials

class AuthenticationDataBase {

    var dataBase : Database = Database.connect("jdbc:sqlite:data/users.db", "org.sqlite.JDBC")

    init {
        transaction (dataBase) {
            SchemaUtils.create(UsersData)
        }
    }

    fun getCredentialsForLogin(login: String) : Credentials?{
        var credentials = transaction(dataBase) {
            var query = UsersData.select { UsersData.login eq login }.limit(1)
                query.map{UsersData.toCredentials(it)}
        }
        if(credentials.size > 0){
            return credentials[0]
        }else{
            return null
        }
    }

    fun putCredentialsForRegistration(credentials: Credentials): Boolean{
        var selectedCredentials = transaction(dataBase) {
            var query = UsersData.select { UsersData.login eq credentials.login }.limit(1)
            query.map{UsersData.toCredentials(it)}
        }
        if(selectedCredentials.size > 0){
            return false
        }else{
            transaction(dataBase) {
                UsersData.insert {
                    it[UsersData.login] = credentials.login
                    it[UsersData.password] = credentials.password
                }
            }
            return true
        }
    }


}