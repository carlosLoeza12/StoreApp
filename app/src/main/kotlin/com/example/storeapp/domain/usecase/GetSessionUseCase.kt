package com.example.storeapp.domain.usecase

import com.example.storeapp.domain.model.User
import com.example.storeapp.domain.repository.StoreRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSessionUseCase @Inject constructor(private val repository: StoreRepository) {

    operator fun invoke(): Flow<User> {

        return repository.getUserSession()
    }
}