package com.serranoie.android.buybuddy.ui.core.biometrics

enum class BiometricAuthStatus(val id: Int) {
    READY(1),
    NOT_AVAILABLE(-1),
    TEMPORARY_NOT_AVAILABLE(-2),
    AVAILABLE_BUT_NOT_ENROLLED(-3)
}