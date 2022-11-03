package ru.graphorismo.data.database.authentication

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table
import ru.graphorismo.data.authentication.Credentials

object UsersData : Table(name = "users_data") {
    val id: Column<Int> = integer("id").autoIncrement()
    val login: Column<String> = text("login").uniqueIndex()
    val password: Column<String> = text("password")
    override val primaryKey = PrimaryKey(id)

    fun toCredentials(row: ResultRow): Credentials{
        return Credentials(row[UsersData.login], row[UsersData.password])
    }
}