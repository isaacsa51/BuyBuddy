package com.serranoie.android.buybuddy.ui.settings

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.serranoie.android.buybuddy.ui.core.analytics.UserEventsTracker
import com.serranoie.android.buybuddy.ui.util.PreferenceUtil
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test


@ExperimentalCoroutinesApi
class SettingsViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @MockK
    private lateinit var preferenceUtil: PreferenceUtil

    @MockK
    private lateinit var userEventsTracker: UserEventsTracker

    private lateinit var viewModel: SettingsViewModel

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxed = true)

        every { preferenceUtil.getInt(any(), any()) } returns ThemeMode.Auto.ordinal
        every { preferenceUtil.getBoolean(any(), any()) } returns false

        viewModel = SettingsViewModel(preferenceUtil, userEventsTracker)
    }

    @Test
    fun `initial theme value should be Auto`() = runTest(testDispatcher) {
        viewModel.theme.test {
            assert(awaitItem() == ThemeMode.Auto)
        }
    }

    @Test
    fun `setTheme should update theme and call preferenceUtil`() = runTest(testDispatcher) {
        viewModel.setTheme(ThemeMode.Dark)

        viewModel.theme.test {
            assert(awaitItem() == ThemeMode.Dark)
        }

        verify { preferenceUtil.putInt(PreferenceUtil.APP_THEME_INT, ThemeMode.Dark.ordinal) }
    }

    @Test
    fun `setMaterialYou should update materialYou and call preferenceUtil`() =
        runTest(testDispatcher) {
            viewModel.setMaterialYou(true)

            viewModel.materialYou.test {
                assert(awaitItem())
            }

            verify { preferenceUtil.putBoolean(PreferenceUtil.MATERIAL_YOU_BOOL, true) }
        }

    @Test
    fun `logSettingsInfo should log settings info using UserEventsTracker`() =
        runTest(testDispatcher) {
            viewModel.logSettingsInfo()

            verify {
                userEventsTracker.logLoadedSettingsInfo(
                    "settings_loaded", mapOf(
                        "theme" to ThemeMode.Auto.toString(),
                        "materialYou" to "false",
                        "categoryVisibility" to "false"
                    )
                )
            }
        }
}