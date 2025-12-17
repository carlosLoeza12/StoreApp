package com.example.storeapp.presentation.ui.categories

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.storeapp.R
import com.example.storeapp.core.components.Loading
import com.example.storeapp.core.components.StoreText
import com.example.storeapp.core.util.AppConstants.DELIMITER_DESTINATION
import com.example.storeapp.core.util.LottieAnimation
import com.example.storeapp.core.util.ResponseResult
import com.example.storeapp.domain.model.StoreCategory
import com.example.storeapp.presentation.navigation.BottomNavigationWrapper
import com.example.storeapp.presentation.navigation.BottonNavItem
import com.example.storeapp.presentation.navigation.BottonNavItem.Companion.bottonNavItems

@Composable
fun CategoriesScreen(viewModel: CategoriesViewModel = hiltViewModel(), logout : () -> Unit) {

    val state by viewModel.storeState.collectAsState()

    val navHostController: NavHostController = rememberNavController()
    val navStackEntry: NavBackStackEntry? by navHostController.currentBackStackEntryAsState()
    val currentRoute: String? = navStackEntry?.destination?.route

    val selectedTab: BottonNavItem = bottonNavItems.firstOrNull { bottonNavItem: BottonNavItem ->

        bottonNavItem.route::class.qualifiedName == currentRoute?.substringBefore(DELIMITER_DESTINATION)

    } ?: bottonNavItems.first()

    Scaffold(
        topBar = { StoreTopBar() },
        bottomBar = { BottomBar(navHostController, selectedTab) }
    ) { paddingValues: PaddingValues ->

        when (val currentState: ResponseResult<List<StoreCategory>> = state) {

            is ResponseResult.Loading -> {

                Loading()
            }

            is ResponseResult.Success -> {

                val categories: List<StoreCategory> = currentState.value

                if (categories.isNotEmpty()) {

                    BottomNavigationWrapper(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.background)
                            .padding(paddingValues),
                        navHostController = navHostController,
                        categories = categories,
                        logout = logout
                    )
                } else {

                    LottieAnimation(R.raw.data_not_found)
                }
            }

            is ResponseResult.Error -> {

                LottieAnimation(R.raw.error)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StoreTopBar() {

    CenterAlignedTopAppBar(
        title = {

            StoreText(
                modifier = Modifier,
                text = stringResource(id = R.string.app_name),
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.headlineMedium
            )
        },
        modifier = Modifier.fillMaxWidth(),
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background,
            titleContentColor = MaterialTheme.colorScheme.onBackground
        ),
        actions = {

            Icon(
                modifier = Modifier.padding(end = 12.dp),
                painter = painterResource(R.drawable.ic_info),
                tint = MaterialTheme.colorScheme.onBackground,
                contentDescription = stringResource(R.string.categories_screen_information_icon_description)
            )
        }
    )
}

@Composable
fun BottomBar(navHostController: NavHostController, selectedTab: BottonNavItem) {

    NavigationBar(containerColor = MaterialTheme.colorScheme.background) {

        bottonNavItems.forEach { bottonNavItem: BottonNavItem ->

            val isSelected: Boolean = selectedTab == bottonNavItem

            NavigationBarItem(
                bottonNavItem = bottonNavItem,
                isSelected = isSelected,
                onClickItem = { destination: Any ->

                    if (isSelected.not()) {

                        navHostController.navigate(route = destination) {

                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                }
            )
        }
    }
}

@Composable
fun RowScope.NavigationBarItem(bottonNavItem: BottonNavItem, isSelected: Boolean, onClickItem: (Any) -> Unit) {

    NavigationBarItem(
        selected = isSelected,
        onClick = { onClickItem(bottonNavItem.route) },
        icon = {
            Icon(
                painter = painterResource(bottonNavItem.iconId),
                contentDescription = stringResource(R.string.categories_screen_navigation_icon_description)
            )
        },
        label = { Text(text = stringResource(id = bottonNavItem.name)) },
        colors = NavigationBarItemDefaults.colors(
            selectedIconColor = MaterialTheme.colorScheme.primary,
            selectedTextColor = MaterialTheme.colorScheme.primary,
            indicatorColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.2f)
        )
    )
}