package ru.graphorismo.data.database.products

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table
import ru.graphorismo.data.products.Order
import ru.graphorismo.data.products.Product

object CartsData : Table(name = "carts_data") {
    val id: Column<Int> = integer("id").autoIncrement().uniqueIndex()
    val user: Column<String> = text("user")
    val product: Column<String> = text("product")
    val amount: Column<Int> = integer("amount")
    val price: Column<Int> = integer("price")

    override val primaryKey = PrimaryKey(id)
}