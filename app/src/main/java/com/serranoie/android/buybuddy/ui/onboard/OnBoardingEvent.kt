package com.serranoie.android.buybuddy.ui.onboard

sealed class OnBoardingEvent {
    data object SaveAppEntry : OnBoardingEvent()
}
