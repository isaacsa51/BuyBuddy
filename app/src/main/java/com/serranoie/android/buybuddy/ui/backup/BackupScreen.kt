package com.serranoie.android.buybuddy.ui.backup

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.CreateDocument
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
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
import androidx.core.content.ContextCompat.getString
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.serranoie.android.buybuddy.R
import com.serranoie.android.buybuddy.ui.util.UiConstants.BACKUP_FILE_NAME
import com.serranoie.android.buybuddy.ui.util.UiConstants.basePadding
import com.serranoie.android.buybuddy.ui.util.UiConstants.smallPadding
import com.serranoie.android.buybuddy.ui.util.weakHapticFeedback

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BackupScreen(
    navController: NavController,
    onGenerateBackupFile: (Uri) -> Unit,
    onRestoreBackupFile: (Uri) -> Unit,
    backupResultState: BackupResultState,
    onSnackbarDismissed: () -> Unit
) {
    val context = LocalContext.current
    val view = LocalView.current
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    val snackbarHostState = remember { SnackbarHostState() }

    val launcherCreateDocument =
        rememberLauncherForActivityResult(CreateDocument("application/json")) { uri ->
            if (uri != null) {
                onGenerateBackupFile(uri)
            }
        }

    val launcherOpenDocument =
        rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
            if (uri != null) {
                onRestoreBackupFile(uri)
            }
        }

    LaunchedEffect(backupResultState) {
        when (backupResultState) {
            is BackupResultState.Success -> {
                snackbarHostState.showSnackbar(getString(context, R.string.backup_success))
                onSnackbarDismissed()
            }
            is BackupResultState.Error -> {
                snackbarHostState.showSnackbar("Error: ${backupResultState.message}")
                onSnackbarDismissed()
            }
            else -> {}
        }
    }

    Scaffold(
        topBar = {
            LargeTopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.backup_screen_title),
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
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
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
                    text = stringResource(id = R.string.backup_info),
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(basePadding),
                )

                Text(
                    text = stringResource(id = R.string.backup_sidenote),
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
                        onClick = {
                            launcherCreateDocument.launch(BACKUP_FILE_NAME)
                        },
                        modifier = Modifier
                            .padding(start = basePadding)
                            .weight(0.5f),
                    ) {
                        Text(text = stringResource(id = R.string.backup_generate_button))
                    }

                    Spacer(modifier = Modifier.weight(0.04f))

                    OutlinedButton(
                        onClick = {
                            launcherOpenDocument.launch(arrayOf("application/json"))
                        },
                        modifier = Modifier
                            .padding(end = basePadding)
                            .weight(0.5f),
                    ) {
                        Text(text = stringResource(id = R.string.backup_restore_button))
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun BackupScreenPreview() {
    val navController = rememberNavController()

    BackupScreen(navController, onGenerateBackupFile = {}, onRestoreBackupFile = {}, backupResultState = BackupResultState.Idle, onSnackbarDismissed = {})
}

sealed class BackupResultState {
    data object Idle : BackupResultState()
    data object Success : BackupResultState()
    data class Error(val message: String) : BackupResultState()
}