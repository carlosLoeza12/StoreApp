package com.example.storeapp.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.example.storeapp.domain.model.StoreCategory
import com.example.storeapp.domain.model.StoreProduct
import com.example.storeapp.presentation.navigation.type.createNavType
import com.example.storeapp.presentation.ui.cart.ProductsCartTabScreen
import com.example.storeapp.presentation.ui.categories.CategoriesTabScreen
import com.example.storeapp.presentation.ui.productDetail.ProductDetailScreen
import com.example.storeapp.presentation.ui.products.ProductsByCategoryScreen
import com.example.storeapp.presentation.ui.profile.ProfileTabScreen
import kotlin.reflect.typeOf

@Composable
fun BottomNavigationWrapper(
    modifier: Modifier = Modifier,
    navHostController: NavHostController,
    categories: List<StoreCategory>,
    logout: () -> Unit
) {

    NavHost(
        modifier = modifier,
        navController = navHostController,
        startDestination = CategoriesGraph
    ) {

        navigation<CategoriesGraph>(startDestination = CategoriesTab) {

            composable<CategoriesTab> {

                CategoriesTabScreen(
                    categories = categories,
                    onCategoryClick = { id: Int ->

                        navHostController.navigate(ProductsByCategoryTab(id))
                    }
                )
            }

            composable<ProductsByCategoryTab> { navBackStackEntry: NavBackStackEntry ->

                val args: ProductsByCategoryTab = navBackStackEntry.toRoute<ProductsByCategoryTab>()

                ProductsByCategoryScreen(
                    categoryId = args.categoryId,
                    onProductClick = { product: StoreProduct ->

                        navHostController.navigate(ProductDetailTab(product))
                    }
                )
            }

            composable<ProductDetailTab>(typeMap = mapOf(typeOf<StoreProduct>() to createNavType<StoreProduct>())) { navBackStackEntry ->

                val args: ProductDetailTab = navBackStackEntry.toRoute<ProductDetailTab>()

                ProductDetailScreen(args.product)
            }
        }

        composable<CartTab> {

            ProductsCartTabScreen()
        }

        composable<ProfileTab> {

            ProfileTabScreen(navigateToLogin = logout)
        }
    }
}