package com.example.storeapp.presentation.ui.categories

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.storeapp.core.components.StoreAsyncImage
import com.example.storeapp.core.components.StoreText
import com.example.storeapp.core.ui.theme.StoreAppTheme
import com.example.storeapp.domain.model.StoreCategory
import com.example.storeapp.domain.model.StoreCategoryBuilder

@Composable
fun CategoriesTabScreen(
    categories: List<StoreCategory>,
    onCategoryClick: (Int) -> Unit
) {

    LazyVerticalGrid(
        columns = GridCells.Fixed(count = 2),
        modifier = Modifier
            .background(color = MaterialTheme.colorScheme.background)
            .fillMaxSize(),
        contentPadding = PaddingValues(all = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        items(items = categories, key = { it.id }) { category: StoreCategory ->

            CategoryItem(category, onCategoryClick)
        }
    }
}

@Composable
fun CategoryItem(category: StoreCategory, onCategoryClick: (Int) -> Unit) {

    Box(
        modifier = Modifier
            .size(width = 173.dp, height = 200.dp)
            .clip(shape = MaterialTheme.shapes.medium)
            .clickable(onClick = { onCategoryClick(category.id) })
    ) {

        StoreAsyncImage(
            modifier = Modifier.fillMaxSize(),
            model = category.image
        )

        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.6f))
                    )
                )
                .padding(all = 12.dp)
        ) {

            StoreText(
                modifier = Modifier,
                text = category.name,
                color = MaterialTheme.colorScheme.onPrimary,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}

@Preview
@Composable
fun CategoriesTabScreenPreview() {

    val category = StoreCategoryBuilder().build()

    val categories: List<StoreCategory> = listOf(category, category.copy(id = 1))

    StoreAppTheme {

        CategoriesTabScreen(categories = categories, onCategoryClick = {})
    }
}