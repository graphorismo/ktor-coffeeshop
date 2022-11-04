package ru.graphorismo.data.database.products

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table
import ru.graphorismo.data.authentication.Credentials
import ru.graphorismo.data.products.Product

object ProductsData : Table(name = "products_data") {
    val id: Column<Int> = integer("id").autoIncrement()
    val name: Column<String> = text("name").uniqueIndex()
    val price: Column<Int> = integer("price")
    val type: Column<String> = text("type")
    val pictureUrl: Column<String> = text("picture_url")

    override val primaryKey = PrimaryKey(id)

    fun toProduct(row: ResultRow): Product {
        return Product(
            id = row[ProductsData.id],
            name = row[ProductsData.name],
            price = row[ProductsData.price],
            type =  row[ProductsData.type],
            pictureUrl = row[ProductsData.pictureUrl]
            )
    }
}