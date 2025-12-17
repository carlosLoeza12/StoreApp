package com.example.storeapp.presentation.ui.cart

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.storeapp.R
import com.example.storeapp.core.components.Loading
import com.example.storeapp.core.components.StoreAlertDialog
import com.example.storeapp.core.components.StoreAsyncImage
import com.example.storeapp.core.components.StoreText
import com.example.storeapp.core.ui.theme.StoreAppTheme
import com.example.storeapp.core.util.DialogType
import com.example.storeapp.core.util.LottieAnimation
import com.example.storeapp.core.util.ResponseResult
import com.example.storeapp.domain.model.ProductCart
import com.example.storeapp.domain.model.ProductCartBuilder

@Composable
fun ProductsCartTabScreen(viewModel: ProductsCartViewModel = hiltViewModel()) {

    val products by viewModel.productsCart.collectAsState()
    val cartTotal by viewModel.cartTotal.collectAsState()

    when (val currentState: ResponseResult<List<ProductCart>> = products) {

        is ResponseResult.Loading -> {

            Loading()
        }

        is ResponseResult.Success -> {

            val productsCart: List<ProductCart> = currentState.value

            if (productsCart.isNotEmpty()) {

                ProductCartContent(
                    productsCart = productsCart,
                    cartTotal = cartTotal,
                    onUpdateAmount = { productId: Int, amount: Int ->

                        viewModel.updateProductCart(
                            productId = productId,
                            amount = amount
                        )
                    },
                    onDeleteProduct = { productId: Int ->

                        viewModel.deleteProductCart(productId)
                    },
                    onCompleteCartPurchase =  {

                        viewModel.updateProductsAsPurchase(productsCart)
                    }
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


@Composable
fun ProductCartContent(
    productsCart: List<ProductCart>,
    cartTotal: Double,
    onUpdateAmount: (Int, Int) -> Unit,
    onDeleteProduct: (Int) -> Unit,
    onCompleteCartPurchase: () -> Unit
) {

    var showPayDialog: Boolean by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp)
    ) {

        LazyColumn(
            modifier = Modifier.weight(1F)
        ) {

            items(items = productsCart, key = { it.productId }) { productCart: ProductCart ->

                ProductCartItem(productCart, onUpdateAmount, onDeleteProduct)
            }
        }

        StoreText(
            text = stringResource(R.string.cart_total, cartTotal),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, bottom = 8.dp),
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onBackground
        )

        Button(
            onClick = {

                showPayDialog = true
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, bottom = 16.dp)
                .height(48.dp),
            shape = MaterialTheme.shapes.small,
            colors = ButtonDefaults.buttonColors(contentColor = MaterialTheme.colorScheme.primary)
        ) {

            StoreText(
                text = stringResource(R.string.cart_pay),
                modifier = Modifier,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }

        if(showPayDialog) {

            StoreAlertDialog(
                title = stringResource(R.string.cart_pay_dialog_title),
                text = stringResource(R.string.cart_pay_dialog_text),
                onConfirm = onCompleteCartPurchase,
                onDismiss = { showPayDialog = false }
            )
        }
    }
}

@Composable
fun ProductCartItem(
    productCart: ProductCart,
    onUpdateAmount: (Int, Int) -> Unit,
    onDeleteProduct: (Int) -> Unit
) {

    var dialogType by remember { mutableStateOf<DialogType?>(null) }

    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        StoreAsyncImage(
            modifier = Modifier
                .size(70.dp)
                .clip(RoundedCornerShape(16.dp)),
            model = productCart.image
        )

        Column(
            modifier = Modifier
                .padding(start = 16.dp)
                .weight(1f)
        ) {

            StoreText(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                text = productCart.title,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onBackground
            )

            Row(modifier = Modifier.padding(8.dp)) {

                StoreText(
                    modifier = Modifier
                        .padding()
                        .clickable { dialogType = DialogType.Amount },
                    text = stringResource(R.string.cart_quantity_label, productCart.amountInCart),
                    color = MaterialTheme.colorScheme.onSecondary,
                    textDecoration = TextDecoration.Underline
                )

                StoreText(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp),
                    text = stringResource(id = R.string.cart_product_price, productCart.price * productCart.amountInCart),
                    style = MaterialTheme.typography.titleSmall
                )
            }
        }

        Icon(
            modifier = Modifier.clickable { dialogType = DialogType.Delete },
            painter = painterResource(R.drawable.ic_delete),
            contentDescription = stringResource(R.string.cart_delete_product_icon_description)
        )
    }

    when (dialogType) {

        is DialogType.Amount -> {

            AmountDialog(
                productTitle = productCart.title,
                productAmount = productCart.amountInCart,
                onConfirm = { amount: Int ->

                    onUpdateAmount(productCart.productId, amount)
                    dialogType = null
                },
                onDismiss = { dialogType = null }
            )
        }

        is DialogType.Delete -> {

            StoreAlertDialog(
                title = productCart.title,
                text = stringResource(R.string.cart_delete_product_dialog_title),
                onConfirm = { onDeleteProduct(productCart.productId) },
                onDismiss = { dialogType = null }
            )
        }

        null -> {}
    }
}

@Composable
fun AmountDialog(
    productTitle: String,
    productAmount: Int,
    onConfirm: (Int) -> Unit,
    onDismiss: () -> Unit
) {
    var amount by remember { mutableIntStateOf(productAmount) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {

            StoreText(
                text = stringResource(R.string.cart_dialog_title),
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.headlineMedium,
                maxLines = 2
            )
        },
        text = {

            Column {

                StoreText(
                    text = productTitle,
                    modifier = Modifier.fillMaxWidth(),
                    style = MaterialTheme.typography.titleSmall,
                    maxLines = 2
                )

                Spacer(Modifier.height(16.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {

                    IconButton(
                        onClick = { if (amount > 1) amount-- }
                    ) {

                        Icon(
                            painter = painterResource(R.drawable.ic_subtract),
                            contentDescription = stringResource(R.string.cart_subtract_button_description)
                        )
                    }

                    StoreText(
                        text = amount.toString(),
                        style = MaterialTheme.typography.headlineMedium,
                        modifier = Modifier.padding(horizontal = 24.dp),
                        color = MaterialTheme.colorScheme.primary
                    )

                    IconButton(
                        onClick = { amount++ }
                    ) {

                        Icon(
                            painter = painterResource(R.drawable.ic_add),
                            contentDescription = stringResource(R.string.cart_add_button_description)
                        )
                    }
                }
            }
        },
        confirmButton = {

            TextButton(onClick = { onConfirm(amount) }) {

                StoreText(
                    text = stringResource(R.string.cart_confirm_button),
                    modifier = Modifier,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        },
        dismissButton = {

            TextButton(onClick = onDismiss) {

                StoreText(text = stringResource(R.string.cart_cancel_button), modifier = Modifier)
            }
        }
    )
}

@Preview
@Composable
fun ProductCartContentPreview() {

    val product: ProductCart = ProductCartBuilder()
        .withId(1)
        .withProductId(1)
        .withTitle("test 01")
        .withImage("https://fakestoreapi.com/img/81")
        .withAmountInCart(1)
        .withPrice(12.0)
        .build()

    val products: List<ProductCart> = listOf(product, product.copy(id = 2, productId = 2))

    StoreAppTheme {

        ProductCartContent(products, 24.0,  { a: Int, b: Int -> }, {}, {})
    }
}
