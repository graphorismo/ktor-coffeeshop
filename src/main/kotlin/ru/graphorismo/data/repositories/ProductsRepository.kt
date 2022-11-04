package ru.graphorismo.data.repositories

import ru.graphorismo.data.database.products.ProductsDataBase
import ru.graphorismo.data.products.Product

class ProductsRepository {

    var productsDataBase = ProductsDataBase()

    fun getProductsOfType(type: String): List<Product>{
        return productsDataBase.getProductsOfType(type)
    }

    companion object {
        private var instance : ProductsRepository? = null

        fun  getInstance(): ProductsRepository {
            if (instance == null)  // NOT thread safe!
                instance = ProductsRepository()

            return instance!!
        }

    }
}