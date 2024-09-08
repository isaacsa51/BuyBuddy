package com.serranoie.android.buybuddy.ui.core.biometrics

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.fragment.app.FragmentActivity
import com.serranoie.android.buybuddy.ui.util.UiConstants.basePadding

@Composable
fun BiometricLock(biometricAuthenticator: BiometricAuthenticator) {
    Column(
        modifier = Modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        val activity = LocalContext.current as FragmentActivity
        var message by remember { mutableStateOf("") }

        TextButton(onClick = {
            biometricAuthenticator.promptBiometricAuth(
                title = "Login",
                subtitle = "Authenticate to continue",
                negativeButtonText = "Cancel",
                fragmentActivity = activity,
                onSuccess = {
                    message = "Succeess"
                },
                onFailed = {
                    message = "Wrong fingerprint or Face ID"
                },
                onError = { _, errorString ->
                    message = errorString
                }
            )
        }) {
            Text(text = "Authenticate")
        }

        Spacer(modifier = Modifier.height(basePadding))

        Text(text = message)
    }
}