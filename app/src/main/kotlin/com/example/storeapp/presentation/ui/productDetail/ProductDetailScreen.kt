package com.example.storeapp.presentation.ui.productDetail

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.storeapp.R
import com.example.storeapp.core.components.StoreAsyncImage
import com.example.storeapp.core.components.StoreText
import com.example.storeapp.core.ui.theme.StoreAppTheme
import com.example.storeapp.domain.model.ProductCart
import com.example.storeapp.domain.model.StoreProduct
import com.example.storeapp.domain.model.StoreProductBuilder

@Composable
fun ProductDetailScreen(
    product: StoreProduct,
    viewModel: ProductDetailViewModel = hiltViewModel()
) {

    ProductDetailContent(product = product) { productCart: ProductCart ->

        viewModel.addProductToCart(productCart)
    }
}

@Composable
fun PeekCarousel(
    urlImages: List<String>
) {

    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = spacedBy(12.dp),
    ) {

        items(urlImages.size) { index: Int ->

            StoreAsyncImage(
                modifier = Modifier
                    .size(240.dp)
                    .clip(RoundedCornerShape(16.dp)),
                model = urlImages[index]
            )
        }
    }
}

@Composable
fun ProductDetailContent(product: StoreProduct, onAddProductToCart: (ProductCart) -> Unit) {

    val scrollState: ScrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 12.dp)
            .verticalScroll(scrollState)
    ) {

        PeekCarousel(urlImages = product.images)
        Spacer(modifier = Modifier.height(20.dp))

        ProductTitle(product.title)
        Spacer(Modifier.height(20.dp))

        ProductDescription(product.description)
        Spacer(Modifier.height(20.dp))

        ProductDetailsSection(product)

        AddToCartButton {

            onAddProductToCart(
                ProductCart(
                    productId = product.id,
                    title = product.title,
                    price = product.price,
                    image = product.images.first(),
                    amountInCart = 1,
                    isPurchased = false,
                    categoryName = product.category.name
                )
            )
        }

        Spacer(Modifier.height(10.dp))
    }
}

@Composable
fun ProductTitle(text: String) {

    StoreText(
        text = text,
        modifier = Modifier.fillMaxWidth(),
        fontWeight = FontWeight.Bold,
        maxLines = 2,
        style = MaterialTheme.typography.headlineSmall
    )
}

@Composable
fun ProductDescription(text: String) {

    StoreText(
        text = text,
        modifier = Modifier
            .fillMaxWidth(),
        fontWeight = FontWeight.Normal,
        maxLines = 8
    )
}

@Composable
fun ProductDetailsSection(product: StoreProduct) {

    StoreText(
        text = stringResource(R.string.product_detail_details_title),
        modifier = Modifier.fillMaxWidth(),
        fontWeight = FontWeight.Bold,
        style = MaterialTheme.typography.headlineSmall
    )

    HorizontalDivider(
        modifier = Modifier.padding(vertical = 20.dp),
        color = MaterialTheme.colorScheme.onBackground,
        thickness = 1.dp
    )

    DetailRow(label = stringResource(R.string.product_detail_product_id_label), value = "${product.id}")
    DetailRow(label = stringResource(R.string.product_detail_price_label), value = "$${product.price}")
    DetailRow(label = stringResource(R.string.product_detail_category_label), value = product.category.name)
}


@Composable
fun DetailRow(label: String, value: String) {

    StoreText(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp, vertical = 6.dp),
        text = label,
        color = MaterialTheme.colorScheme.onSecondary,
        style = MaterialTheme.typography.titleSmall
    )

    StoreText(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp),
        text = value,
        fontWeight = FontWeight.Normal,
        style = MaterialTheme.typography.titleSmall
    )
}

@Composable
fun AddToCartButton(onClick: () -> Unit) {

    Spacer(modifier = Modifier.height(20.dp))

    var isButtonEnable: Boolean by remember { mutableStateOf(true) }

    Button(
        onClick = {

            onClick()
            isButtonEnable = false
        },
        enabled = isButtonEnable,
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp),
        shape = MaterialTheme.shapes.small,
        colors = ButtonDefaults.buttonColors(contentColor = MaterialTheme.colorScheme.primary)
    ) {

        StoreText(
            text = stringResource(R.string.product_detail_add_to_cart_button),
            modifier = Modifier,
            color = MaterialTheme.colorScheme.onPrimary
        )
    }
}

@Preview
@Composable
fun ProductDetailScreenPreview() {

    val sampleProduct = StoreProductBuilder()
        .id(1)
        .title("Title")
        .description("Description")
        .price(29.99)
        .images(
            listOf(
                "https://via.placeholder.com/300.png",
                "https://via.placeholder.com/301.png",
                "https://via.placeholder.com/302.png"
            )
        )
        .build()

    StoreAppTheme {

        ProductDetailContent(sampleProduct) {}
    }
}