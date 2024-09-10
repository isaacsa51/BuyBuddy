package com.serranoie.android.buybuddy.ui.backup

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.serranoie.android.buybuddy.R
import com.serranoie.android.buybuddy.ui.util.UiConstants.basePadding
import com.serranoie.android.buybuddy.ui.util.UiConstants.smallPadding
import com.serranoie.android.buybuddy.ui.util.weakHapticFeedback

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BackupScreen(
    navController: NavController,
    onGenerateBackupFile: () -> Unit,
    onRestoreBackupFile: () -> Unit,
) {
    val view = LocalView.current
    val context = LocalContext.current
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    val result = remember { mutableStateOf<Uri?>(null) }
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) {
        result.value = it
    }

    Scaffold(
        topBar = {
            LargeTopAppBar(
                title = {
                    Text(
                        text = "Backup",
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        view.weakHapticFeedback()
                        navController.navigateUp()
                    }) {
                        Icon(
                            Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = stringResource(id = R.string.back),
                        )
                    }
                },
                scrollBehavior = scrollBehavior,
            )
        },
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
    ) { padding ->
        Column {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
            ) {
                Card(
                    modifier = Modifier.padding(smallPadding),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(
                            3.dp,
                        ),
                    ),
                ) {
                    Column(modifier = Modifier.padding(top = 2.dp)) {
                        Image(
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight(fraction = 0.5f),
                            painter = painterResource(id = R.drawable.image_backup),
                            contentDescription = "Backup Image",
                            contentScale = ContentScale.FillWidth,
                        )
                    }
                }

                Text(
                    text = "Back up your app data including all your products, current reminders & transactions, this can be easily restored whenever you want.",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(basePadding),
                )

                Text(
                    text = "Take in consideration that backup data don't include app settings.",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Normal,
                    modifier = Modifier.padding(
                        start = basePadding, end = basePadding, bottom = basePadding
                    )
                )

                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    FilledTonalButton(
                        onClick = { onGenerateBackupFile() },
                        modifier = Modifier
                            .padding(start = basePadding)
                            .weight(0.5f),
                    ) {
                        Text(
                            text = "Generate Backup",
                        )
                    }

                    Spacer(modifier = Modifier.weight(0.04f))

                    OutlinedButton(
                        onClick = {
                            launcher.launch(arrayOf("application/json"))
                        },
                        modifier = Modifier
                            .padding(end = basePadding)
                            .weight(0.5f),
                    ) {
                        Text(
                            text = "Restore Backup",
                        )
                    }

                    result.value?.let { json ->
                        Text(text = "Document Path: " + json.path.toString())
                    }
                }

                Text(
                    text = "The backup file of your products is located at: /sdcard/Android/data/com.serranoie.android.buybuddy/files/buybuddy_backup.json",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.outline,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(basePadding)
                        .wrapContentSize(Alignment.BottomCenter)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun BackupScreenPreview() {
    val navController = rememberNavController()

    BackupScreen(navController, onGenerateBackupFile = {}, onRestoreBackupFile = {})
}