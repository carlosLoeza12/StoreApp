package com.example.storeapp.core.util

sealed class DialogType {
    data object Amount : DialogType()
    data object Delete : DialogType()
}