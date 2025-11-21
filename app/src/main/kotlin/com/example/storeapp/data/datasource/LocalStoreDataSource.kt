package com.example.storeapp.data.datasource

import com.example.storeapp.data.local.dao.ProductCartDao
import com.example.storeapp.data.local.entity.ProductCartEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalStoreDataSource @Inject constructor(private val dao: ProductCartDao) {

    suspend fun getProductById(productId: Int): ProductCartEntity? {

        return dao.getProductById(productId = productId)
    }

    fun getProductsCart(): Flow<List<ProductCartEntity>> {

        return dao.getProductsCart()
    }

    suspend fun addProductToCart(product: ProductCartEntity): Long {

        return dao.insertProduct(product = product)
    }

    suspend fun updateProduct(product: ProductCartEntity): Long {

        return dao.updateProduct(product = product).toLong()
    }

    suspend fun updateAmountProduct(productId: Int, amount: Int): Int {

        return dao.updateAmountProduct(productId = productId, amount = amount)
    }

    suspend fun deleteProductFromCart(productId: Int): Int {

        return dao.deleteProductFromCart(productId = productId)
    }

    suspend fun updateProductsAsPurchased(productsId: List<Int>): Int {

        return dao.updateProductsAsPurchased(productsId)
    }
}