package com.serranoie.android.buybuddy.ui.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.wear.compose.material.Button
import com.serranoie.android.buybuddy.R
import com.serranoie.android.buybuddy.ui.core.AppEntryViewModel
import com.serranoie.android.buybuddy.ui.util.UiConstants.basePadding

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    navController: NavController,
    viewModel: AppEntryViewModel = hiltViewModel(),
) {
    var isDynamicColorsEnabled by remember { mutableStateOf(viewModel.dynamicColorsEnabled) }
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Settings",
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            Icons.Rounded.ArrowBack,
                            contentDescription = stringResource(id = R.string.back),
                        )
                    }
                },
                scrollBehavior = scrollBehavior,
            )
        },
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
    ) {
        Column(
            modifier = Modifier.padding(it),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(basePadding),
            ) {
                Text(
                    text = "Enable Dynamic Colors",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.weight(1f),
                )
                Checkbox(checked = isDynamicColorsEnabled, onCheckedChange = { isChecked ->
                    isDynamicColorsEnabled = isChecked
                    viewModel.toggleDynamicColors(isChecked)
                })
            }

            Text(
                text = "Material You design was launched with Android 12 and expanded in Android 13. On devices running Android 12 and later, the “dynamic theme” feature allows you to customize the appearance of apps and the operating system based on the colors of your wallpaper or individual accent colors.",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.outline,
            )

            Button(onClick = { throw RuntimeException("Test Crash") }) {
                Text(text = "Crash")
            }
        }
    }
}
