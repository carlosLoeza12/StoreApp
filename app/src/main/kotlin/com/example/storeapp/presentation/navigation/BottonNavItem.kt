package com.example.storeapp.presentation.navigation

import androidx.annotation.StringRes
import com.example.storeapp.R

data class BottonNavItem(@param:StringRes val name: Int, val route: Any, val iconId: Int) {

    companion object {

        val bottonNavItems: List<BottonNavItem> = listOf(
            BottonNavItem(
                name = R.string.nav_item_categories,
                route = CategoriesGraph,
                iconId = R.drawable.ic_list
            ),
            BottonNavItem(
                name = R.string.nav_item_shopping_cart,
                route = CartTab,
                iconId = R.drawable.ic_shopping_cart
            ),
            BottonNavItem(
                name = R.string.nav_item_profile,
                route = ProfileTab,
                iconId = R.drawable.ic_profile
            )
        )
    }
}
