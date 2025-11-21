package com.example.storeapp.core.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import coil3.compose.AsyncImage
import com.example.storeapp.R

@Composable
fun StoreAsyncImage(
    modifier: Modifier,
    model: Any,
    contentScale: ContentScale = ContentScale.Crop
) {

    AsyncImage(
        modifier = modifier,
        model = model,
        error = painterResource(id = R.drawable.image_not_found_),
        contentScale = contentScale,
        contentDescription = stringResource(id = R.string.categories_screen_category_item_image_description_image)
    )
}