package ru.graphorismo.data.database.products

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import ru.graphorismo.data.authentication.Credentials
import ru.graphorismo.data.database.authentication.UsersData
import ru.graphorismo.data.products.Product

class ProductsDataBase {

    var dataBase : Database = Database.connect("jdbc:sqlite:data/products.db", "org.sqlite.JDBC")

    init {
        transaction (dataBase) {
            SchemaUtils.create(ProductsData)
        }
    }

    fun getProductsOfType(type: String): List<Product> {
        var products = transaction(dataBase) {
            var query = ProductsData.select(ProductsData.type eq type)
            query.map { ProductsData.toProduct(it) }
        }
        return products
    }


}