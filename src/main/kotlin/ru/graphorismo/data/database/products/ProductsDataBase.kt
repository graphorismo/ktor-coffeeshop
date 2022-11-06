package ru.graphorismo.data.database.products

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import ru.graphorismo.data.products.Order
import ru.graphorismo.data.products.Product

class ProductsDataBase private constructor(){

    var dataBase : Database = Database.connect("jdbc:sqlite:data/products.db", "org.sqlite.JDBC")

    companion object{
        private var productsDataBase : ProductsDataBase? = null

        fun getInstance() : ProductsDataBase{
            if (productsDataBase == null){
                productsDataBase = ProductsDataBase()
            }
            return productsDataBase!!
        }
    }
    init {
        transaction (dataBase) {
            SchemaUtils.create(ProductsData)
            SchemaUtils.create(CartsData)
        }
    }

    fun putOrderToTheCartForLogin(order: Order, login: String){

        var alreadyInsertedOrders = transaction(dataBase) {
             CartsData
                .select {(CartsData.product eq order.product.name) and (CartsData.user eq login) }
                .map { toOrder(it) }
        }
        if(alreadyInsertedOrders.isEmpty()){
            transaction {
                CartsData.insert {
                    it[CartsData.user] = login
                    it[CartsData.product] =  order.product.name
                    it[CartsData.amount] = order.amount
                    it[CartsData.price] = order.product.price
                }
            }
        }else{
            transaction {
                CartsData.update({(CartsData.product eq order.product.name) and (CartsData.user eq login)}) {
                    it[CartsData.amount] = order.amount + alreadyInsertedOrders[0].amount
                }
            }
        }
    }

    fun getOrdersForLogin(login: String) :List<Order>{
        return transaction(dataBase) {
            CartsData.select {CartsData.user eq login }
                .map { toOrder(it) }
        }
    }
    fun toOrder(row: ResultRow): Order{
        var product = transaction(dataBase) {
            ProductsData
                .select{ProductsData.name eq row[CartsData.product]}
                .limit(1)
                .map { ProductsData.toProduct(it) }[0]
        }
        return Order(amount = row[CartsData.amount], product = product)
    }

    fun getProductsOfType(type: String): List<Product> {
        var products = transaction(dataBase) {
            var query = ProductsData.select(ProductsData.type eq type)
            query.map { ProductsData.toProduct(it) }
        }
        return products
    }


}