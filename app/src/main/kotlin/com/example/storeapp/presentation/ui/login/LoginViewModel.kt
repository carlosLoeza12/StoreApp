package com.example.storeapp.presentation.ui.login

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storeapp.core.util.ResponseResult
import com.example.storeapp.data.remote.GoogleAuthUiClient
import com.example.storeapp.di.IoDispatcher
import com.example.storeapp.domain.model.User
import com.example.storeapp.domain.usecase.SaveSessionUseCase
import com.example.storeapp.domain.usecase.SignInWithGoogleUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val googleAuthUiClient: GoogleAuthUiClient,
    private val signInWithGoogleUseCase: SignInWithGoogleUseCase,
    private val saveSessionUseCase: SaveSessionUseCase,
    @param:IoDispatcher private val dispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _loginState: MutableStateFlow<ResponseResult<Boolean>> = MutableStateFlow(ResponseResult.Loading)
    val loginState: StateFlow<ResponseResult<Boolean>> = this._loginState

    fun signInWithGoogle(context: Context) {

        viewModelScope.launch {

            _loginState.value = ResponseResult.Loading

            try {

                val idToken: String = withContext(context = dispatcher) {

                    googleAuthUiClient.getTokenId(activityContext = context)
                } ?: run {

                    _loginState.value = ResponseResult.Error("Token not found")

                    return@launch
                }

                val user: User = withContext(context = dispatcher) {

                    signInWithGoogleUseCase(idToken)
                } ?: run {

                    _loginState.value = ResponseResult.Error("User not found")

                    return@launch
                }

                saveSessionUseCase.invoke(user = user)

                _loginState.value = ResponseResult.Success(value = true)
            } catch (e: Exception) {

                _loginState.value = ResponseResult.Error(message = e.localizedMessage ?: "An unexpected error occurred")
            }
        }
    }
}