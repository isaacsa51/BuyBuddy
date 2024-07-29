package com.serranoie.android.buybuddy.ui.onboard

import com.serranoie.android.buybuddy.domain.usecase.appentry.AppEntryUseCase
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.just
import io.mockk.runs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class OnBoardingViewModelTest {

    @MockK
    private lateinit var appEntryUseCase: AppEntryUseCase

    private lateinit var viewModel: OnBoardingViewModel

    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(testDispatcher)
        viewModel = OnBoardingViewModel(appEntryUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test SaveAppEntry event triggers saveUserEntry`(): Unit = runTest {
        // Given
        coEvery { appEntryUseCase.saveAppEntry() } just runs

        // When
        viewModel.onEvent(OnBoardingEvent.SaveAppEntry)

        // Then
        coVerify { appEntryUseCase.saveAppEntry() }
    }
}