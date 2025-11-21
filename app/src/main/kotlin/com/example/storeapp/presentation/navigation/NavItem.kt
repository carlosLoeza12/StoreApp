package com.example.storeapp.presentation.navigation

import androidx.annotation.StringRes
import com.example.storeapp.R

data class NavItem(@param:StringRes val name: Int, val route: Any, val iconId: Int) {

    companion object {

        val navItems: List<NavItem> = listOf(
            NavItem(
                name = R.string.nav_item_categories,
                route = CategoriesGraph,
                iconId = R.drawable.ic_list
            ),
            NavItem(
                name = R.string.nav_item_shopping_cart,
                route = CartTab,
                iconId = R.drawable.ic_shopping_cart
            ),
            NavItem(
                name = R.string.nav_item_profile,
                route = ProfileTab,
                iconId = R.drawable.ic_profile
            )
        )
    }
}
