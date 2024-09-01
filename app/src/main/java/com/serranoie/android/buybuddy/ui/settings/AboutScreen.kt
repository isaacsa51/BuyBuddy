package com.serranoie.android.buybuddy.ui.settings

import android.os.Build
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Notes
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PrivacyTip
import androidx.compose.material.icons.filled.Web
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.serranoie.android.buybuddy.BuildConfig
import com.serranoie.android.buybuddy.R
import com.serranoie.android.buybuddy.ui.settings.common.SettingsItem
import com.serranoie.android.buybuddy.ui.util.Utils
import com.serranoie.android.buybuddy.ui.util.weakHapticFeedback

sealed class AboutLinks(val url: String) {
    data object ReadMe : AboutLinks("https://github.com/isaacsa51/BuyBuddy")
    data object Website : AboutLinks("https://github.com/isaacsa51")
    data object PrivacyPolicy :
        AboutLinks("https://github.com/isaacsa51/BuyBuddy/blob/master/PRIVACY-POLICY.md")

    data object GithubIssues : AboutLinks("https://github.com/isaacsa51/BuyBuddy/issues")
    data object Sponsor : AboutLinks("https://github.com/sponsors/isaacsa51")
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen(navController: NavController) {
    val view = LocalView.current
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    Scaffold(modifier = Modifier
        .fillMaxSize()
        .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            LargeTopAppBar(
                title = {
                    Text(
                        stringResource(id = R.string.about_screen_header),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        view.weakHapticFeedback()
                        navController.navigateUp()
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null
                        )
                    }
                },
                scrollBehavior = scrollBehavior, colors = TopAppBarDefaults.largeTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    scrolledContainerColor = MaterialTheme.colorScheme.surface,
                )
            )
        }) {
        LazyColumn(modifier = Modifier.padding(it)) {
            item {
                SettingsItem(title = stringResource(id = R.string.about_readme_title),
                    description = stringResource(id = R.string.about_readme_desc),
                    icon = Icons.AutoMirrored.Filled.Notes,
                    onClick = { Utils.openWebLink(context, AboutLinks.ReadMe.url) }
                )
            }
            item {
                SettingsItem(title = stringResource(id = R.string.about_website_title),
                    description = stringResource(id = R.string.about_website_desc),
                    icon = Icons.Filled.Web,
                    onClick = { Utils.openWebLink(context, AboutLinks.Website.url) }
                )
            }
            item {
                SettingsItem(title = stringResource(id = R.string.about_privacy_title),
                    description = stringResource(id = R.string.about_privacy_desc),
                    icon = Icons.Filled.PrivacyTip,
                    onClick = { Utils.openWebLink(context, AboutLinks.PrivacyPolicy.url) }
                )
            }
            item {
                SettingsItem(title = stringResource(id = R.string.about_gh_issue_title),
                    description = stringResource(id = R.string.about_gh_issue_desc),
                    icon = ImageVector.vectorResource(id = R.drawable.ic_about_gh_issue),
                    onClick = { Utils.openWebLink(context, AboutLinks.GithubIssues.url) }
                )
            }
            item {
                SettingsItem(title = stringResource(id = R.string.about_support_title),
                    description = stringResource(id = R.string.about_support_desc),
                    icon = Icons.Filled.Favorite,
                    onClick = { Utils.openWebLink(context, AboutLinks.Sponsor.url) }
                )
            }
            item {
                SettingsItem(title = stringResource(id = R.string.about_version_title),
                    description = stringResource(id = R.string.about_version_desc).format(BuildConfig.VERSION_NAME),
                    icon = Icons.Filled.Info,
                    onClick = { clipboardManager.setText(AnnotatedString(getVersionReport())) }
                )
            }
        }
    }
}

private fun getVersionReport(): String {
    val versionName = BuildConfig.VERSION_NAME
    val versionCode = BuildConfig.VERSION_CODE
    val release = if (Build.VERSION.SDK_INT >= 30) {
        Build.VERSION.RELEASE_OR_CODENAME
    } else {
        Build.VERSION.RELEASE
    }

    return StringBuilder().append("App version: $versionName ($versionCode)\n")
        .append("Android Version: Android $release (API ${Build.VERSION.SDK_INT})\n")
        .append("Device information: ${Build.MANUFACTURER} ${Build.MODEL} (${Build.DEVICE})\n")
        .append("Supported ABIs: ${Build.SUPPORTED_ABIS.contentToString()}\n")
        .toString()
}

@Preview
@Composable
fun AboutScreenPreview() {
    AboutScreen(navController = rememberNavController())
}
