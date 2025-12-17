package com.example.storeapp.data.datasource

import com.example.storeapp.data.remote.api.ApiService
import com.example.storeapp.data.remote.model.StoreCategoryResponse
import com.example.storeapp.data.remote.model.StoreProductResponse
import com.example.storeapp.data.remote.model.toDomain
import com.example.storeapp.domain.model.User
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class RemoteStoreDataSource @Inject constructor(
    private val apiService: ApiService,
    private val firebaseAuth: FirebaseAuth
) {

    // region store

    suspend fun getStoreCategories(): List<StoreCategoryResponse> {

        return apiService.getStoreCategories()
    }

    suspend fun getProductsByCategory(categoryId: Int): List<StoreProductResponse> {

        return apiService.getProductsByCategory(categoryId)
    }

    // endregion

    // region login

    suspend fun signInWithGoogle(idToken: String): User? {

        return try {

            val credential: AuthCredential = GoogleAuthProvider.getCredential(idToken, null)

            val result: AuthResult = firebaseAuth.signInWithCredential(credential).await()

            return result.user?.toDomain()

        } catch (e: Exception) {

            null
        }
    }

    // endregion
}