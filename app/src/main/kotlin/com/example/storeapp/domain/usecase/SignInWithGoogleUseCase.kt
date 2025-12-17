package com.example.storeapp.domain.usecase

import com.example.storeapp.domain.model.User
import com.example.storeapp.domain.repository.StoreRepository
import javax.inject.Inject

class SignInWithGoogleUseCase @Inject constructor(private val repository: StoreRepository)  {

    suspend operator fun invoke(idToken: String): User? {

        return repository.signInWithGoogle(idToken)
    }
}