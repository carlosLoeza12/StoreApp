package com.example.storeapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.storeapp.core.ui.theme.StoreAppTheme
import com.example.storeapp.presentation.navigation.AppRootNavigation
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        this.enableEdgeToEdge()

        setContent {

            StoreAppTheme {

                AppRootNavigation()
            }
        }
    }
}