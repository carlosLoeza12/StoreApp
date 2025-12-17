package com.example.storeapp.presentation.navigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storeapp.core.util.ResponseResult
import com.example.storeapp.di.IoDispatcher
import com.example.storeapp.domain.model.User
import com.example.storeapp.domain.usecase.GetSessionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class AppRootNavigationViewModel @Inject constructor (
    private val getSessionUseCase : GetSessionUseCase,
    @param:IoDispatcher private val dispatcher: CoroutineDispatcher
): ViewModel() {

    private val _loginState: MutableStateFlow<ResponseResult<Boolean>> = MutableStateFlow(ResponseResult.Loading)
    val loginState: StateFlow<ResponseResult<Boolean>> = this._loginState

    init {

        viewModelScope.launch {

            try {

                _loginState.value = ResponseResult.Loading

                val isUserLogged: Boolean = withContext(dispatcher) {

                    val user: User? = getSessionUseCase().firstOrNull()

                    user != null && user.id.isNotEmpty()
                }

                _loginState.value = ResponseResult.Success(value = isUserLogged)
            } catch (e: Exception) {

                _loginState.value = ResponseResult.Error(message = e.localizedMessage ?: "An unexpected error occurred")
            }
        }
    }
}