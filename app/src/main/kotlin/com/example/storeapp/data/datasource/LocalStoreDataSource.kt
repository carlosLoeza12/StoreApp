package com.example.storeapp.data.datasource

import com.example.storeapp.data.local.dao.ProductCartDao
import com.example.storeapp.data.local.entity.ProductCartEntity
import com.example.storeapp.data.local.preferences.UserPreferencesDataStore
import com.example.storeapp.domain.model.User
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalStoreDataSource @Inject constructor(
    private val dao: ProductCartDao,
    private val userPreferences: UserPreferencesDataStore
) {

    // region store

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

    suspend fun clearProductCart() {

        dao.clearProductCart()
    }

    // endregion

    // region login

    suspend fun saveUser(user: User) {

        userPreferences.saveSession(user)
    }

    fun getUserSession(): Flow<User> {

        return userPreferences.sessionFlow
    }

    suspend fun logout(): Boolean {

        return userPreferences.clearSession()
    }

    // endregion
}