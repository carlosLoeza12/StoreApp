package com.example.storeapp.presentation.ui.categories

import com.example.storeapp.domain.usecase.GetStoreCategoriesUseCase
import io.mockk.MockKAnnotations
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.rules.TestWatcher
import org.junit.runner.Description

class CategoriesViewModelTestRule: TestWatcher() {

    val testDispatcher: TestDispatcher = StandardTestDispatcher()

    val getCategoriesUseCase: GetStoreCategoriesUseCase = mockk(relaxed = true)

    lateinit var categoriesViewModel: CategoriesViewModel

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun starting(description: Description?) {
        super.starting(description)

        MockKAnnotations.init(this)
        Dispatchers.setMain(testDispatcher)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun finished(description: Description?) {
        super.finished(description)

        Dispatchers.resetMain()
    }
}