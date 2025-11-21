package com.example.storeapp.data.repository

import com.example.storeapp.data.datasource.LocalStoreDataSource
import com.example.storeapp.data.datasource.RemoteStoreDataSource
import com.example.storeapp.data.local.entity.ProductCartEntity
import com.example.storeapp.data.local.entity.toDomain
import com.example.storeapp.data.local.entity.toEntity
import com.example.storeapp.data.remote.model.toDomain
import com.example.storeapp.domain.model.ProductCart
import com.example.storeapp.domain.model.StoreCategory
import com.example.storeapp.domain.model.StoreProduct
import com.example.storeapp.domain.repository.StoreRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class StoreRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteStoreDataSource,
    private val localDataSource: LocalStoreDataSource
) : StoreRepository {

    override suspend fun getStoreCategories(): List<StoreCategory> {

        return remoteDataSource.getStoreCategories().map { it.toDomain() }
    }

    override suspend fun getProductsByCategory(categoryId: Int): List<StoreProduct> {

        return remoteDataSource.getProductsByCategory(categoryId).map { it.toDomain() }
    }

    override fun getProductsCart(): Flow<List<ProductCart>> {

        return localDataSource.getProductsCart().map { entities ->

            entities.map { entity -> entity.toDomain() }
        }
    }

    override suspend fun addProductToCart(product: ProductCart): Long {

        val entity: ProductCartEntity = product.toEntity()

        val existing: ProductCartEntity? = localDataSource.getProductById(productId = entity.productId)

        return if (existing == null) {

            localDataSource.addProductToCart(product = entity.copy(amountInCart = 1))
        } else {

            localDataSource.updateProduct(product = existing.copy(amountInCart = existing.amountInCart + 1))
        }
    }

    override suspend fun updateAmountProductCart(productId: Int, amount: Int): Int {

        return localDataSource.updateAmountProduct(productId, amount)
    }

    override suspend fun deleteProductFromCart(productId: Int): Int {

        return localDataSource.deleteProductFromCart(productId = productId)
    }

    override suspend fun updateProductsAsPurchased(productsId: List<Int>): Int {

        return localDataSource.updateProductsAsPurchased(productsId)
    }
}
