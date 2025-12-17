package com.example.storeapp.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.example.storeapp.core.components.Loading
import com.example.storeapp.core.util.ResponseResult
import com.example.storeapp.presentation.ui.categories.CategoriesScreen
import com.example.storeapp.presentation.ui.login.LoginScreen

@Composable
fun AppRootNavigation(viewModel : AppRootNavigationViewModel = hiltViewModel()) {

    val navController: NavHostController = rememberNavController()

    val isUserLogged by viewModel.loginState.collectAsState()

    when (val currentState: ResponseResult<Boolean> = isUserLogged) {

        is ResponseResult.Loading -> {

            Loading()
        }

        is ResponseResult.Success -> {

            val isUserLogged: Boolean  = currentState.value

            RootNavigationGraph(navController = navController, startDestination = if (isUserLogged) MainGraph else AuthGraph)
        }

        is ResponseResult.Error -> {

            RootNavigationGraph(navController = navController, startDestination = AuthGraph)
        }
    }
}

@Composable
fun RootNavigationGraph(navController : NavHostController, startDestination : Any) {

    NavHost(navController = navController, startDestination = startDestination) {

        navigation<AuthGraph>(startDestination = Login) {

            composable<Login> {

                LoginScreen(navigateMainScreen = {

                    navController.navigate(route = MainGraph) {

                        popUpTo(route = AuthGraph) { inclusive = true }
                    }
                })
            }
        }

        composable<MainGraph> {

            CategoriesScreen(logout = {

                navController.navigate(AuthGraph) {

                    popUpTo(MainGraph) { inclusive = true }
                }
            })
        }
    }
}
