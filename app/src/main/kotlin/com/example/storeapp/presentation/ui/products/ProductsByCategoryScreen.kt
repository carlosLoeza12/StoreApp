package com.example.storeapp.presentation.ui.products

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.storeapp.R
import com.example.storeapp.core.components.Loading
import com.example.storeapp.core.components.StoreAsyncImage
import com.example.storeapp.core.components.StoreText
import com.example.storeapp.core.ui.theme.StoreAppTheme
import com.example.storeapp.core.util.LottieAnimation
import com.example.storeapp.core.util.ResponseResult
import com.example.storeapp.domain.model.StoreCategoryBuilder
import com.example.storeapp.domain.model.StoreProduct
import com.example.storeapp.domain.model.StoreProductBuilder

@Composable
fun ProductsByCategoryScreen(
    categoryId: Int,
    viewModel: ProductsByCategoryViewModel = hiltViewModel(),
    onProductClick: (StoreProduct) -> Unit
) {

    LaunchedEffect(key1 = categoryId) {

        viewModel.getProductsByCategory(categoryId)
    }

    val productsState by viewModel.productsState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        when (val currentSates: ResponseResult<List<StoreProduct>> = productsState) {

            is ResponseResult.Loading -> {

                Loading()
            }

            is ResponseResult.Success -> {

                if (currentSates.value.isNotEmpty()) {

                    ProductsByCategories(products = currentSates.value, onProductClick)
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

@Composable
fun ProductsByCategories(products: List<StoreProduct>, onProductClick: (StoreProduct) -> Unit) {

    LazyVerticalGrid(
        modifier = Modifier.fillMaxSize(),
        columns = GridCells.Fixed(count = 2),
        contentPadding = PaddingValues(all = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        items(items = products) { product: StoreProduct ->

            ProductItem(product = product, onProductClick)
        }
    }
}

@Composable
fun ProductItem(product: StoreProduct, onProductClick: (StoreProduct) -> Unit) {

    Column(
        modifier = Modifier
            .width(width = 173.dp)
            .clip(shape = MaterialTheme.shapes.medium)
            .clickable(onClick = { onProductClick(product) })
    ) {

        StoreAsyncImage(
            modifier = Modifier
                .height(height = 230.dp)
                .clip(shape = MaterialTheme.shapes.medium),
            model = product.images.first()
        )

        Spacer(modifier = Modifier.height(12.dp))

        StoreText(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp),
            text = product.title,
            fontWeight = FontWeight.Normal,
            style = MaterialTheme.typography.titleSmall
        )

        StoreText(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp, vertical = 6.dp),
            text = "$${product.price}",
            color = MaterialTheme.colorScheme.onSecondary,
            style = MaterialTheme.typography.titleSmall
        )
    }
}

@Preview
@Composable
fun ProductsByCategoriesPreview() {

    val storeProduct = StoreProductBuilder()
        .id(1)
        .title("Product")
        .slug("slug")
        .price(1.0)
        .description("")
        .category(StoreCategoryBuilder().build())
        .images(listOf("", ""))
        .build()

    val list: List<StoreProduct> = listOf(storeProduct)

    StoreAppTheme {

        ProductsByCategories(products = list, onProductClick = {})
    }
}
