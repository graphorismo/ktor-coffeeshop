package ru.graphorismo.data.database.products

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import ru.graphorismo.data.products.Order
import ru.graphorismo.data.products.Product
import ru.graphorismo.data.responses.CartResponse

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

    fun putOrderToTheCartForLogin(order: Order, login: String): CartResponse{

        var response = CartResponse("error")
        var alreadyInsertedOrders = transaction(dataBase) {
             CartsData
                .select {(CartsData.product eq order.product.name) and (CartsData.user eq login) }
                .map { toOrder(it) }
        }
        if(alreadyInsertedOrders.isEmpty()){
            transaction(dataBase) {
                CartsData.insert {
                    it[CartsData.user] = login
                    it[CartsData.product] =  order.product.name
                    it[CartsData.amount] = order.amount
                    it[CartsData.price] = order.product.price
                }
            }
            response = CartResponse("ok")
        }else{
            transaction(dataBase) {
                CartsData.update({(CartsData.product eq order.product.name) and (CartsData.user eq login)}) {
                    it[CartsData.amount] = order.amount + alreadyInsertedOrders[0].amount
                }
            }
            response = CartResponse("ok")
        }
        return response
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

    fun removeOrderFromTheCartForLogin(order: Order, login: String): CartResponse {

        var response = CartResponse("error")
        var alreadyInsertedOrders = transaction(dataBase) {
            CartsData
                .select {(CartsData.product eq order.product.name) and (CartsData.user eq login) }
                .map { toOrder(it) }
        }
        if(alreadyInsertedOrders.isEmpty() == false){
            var newAmount = alreadyInsertedOrders[0].amount - order.amount
            if(newAmount > 0){
                transaction(dataBase) {
                    CartsData.update({(CartsData.product eq order.product.name) and (CartsData.user eq login)}) {
                        it[CartsData.amount] = newAmount
                    }
                }
            }else{
                transaction(dataBase) {
                    CartsData.deleteWhere {(CartsData.product eq order.product.name) and (CartsData.user eq login)}
                }
            }
            response = CartResponse("ok")
        }
        return response
    }

    fun clearCartForLogin(login: String): CartResponse {
        var response = CartResponse("ok")
        transaction(dataBase) {
            CartsData.deleteWhere {(CartsData.user eq login)}
        }
        return response
    }


}