package com.example.storeapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.storeapp.data.local.entity.ProductCartEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductCartDao {

    @Query("SELECT * FROM products_cart WHERE productId = :productId AND isPurchased IS 0")
    suspend fun getProductById(productId: Int): ProductCartEntity?

    @Query("SELECT * FROM products_cart WHERE isPurchased IS 0")
    fun getProductsCart(): Flow<List<ProductCartEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(product: ProductCartEntity): Long

    @Update
    suspend fun updateProduct(product: ProductCartEntity): Int

    @Query("UPDATE products_cart SET amountInCart = :amount WHERE productId = :productId AND isPurchased IS 0")
    suspend fun updateAmountProduct(productId: Int, amount: Int): Int

    @Query("DELETE FROM products_cart WHERE productId = :productId AND isPurchased IS 0")
    suspend fun deleteProductFromCart(productId: Int): Int

    @Query("UPDATE products_cart SET isPurchased = 1 WHERE productId IN (:productsId) AND isPurchased IS 0")
    suspend fun updateProductsAsPurchased(productsId: List<Int>): Int
}
