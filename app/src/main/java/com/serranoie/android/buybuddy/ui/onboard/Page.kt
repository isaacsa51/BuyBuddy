package com.serranoie.android.buybuddy.ui.onboard

import androidx.annotation.DrawableRes
import com.serranoie.android.buybuddy.R

data class Page(
    val title: Int,
    val description: Int,
    @DrawableRes val image: Int,
)

val pages = listOf(
    Page(
        title = R.string.onboarding_first_title,
        description = R.string.onboarding_first_description,
        image = R.drawable.image_shopping,
    ),
    Page(
        title = R.string.onboarding_second_title,
        description = R.string.onboarding_second_description,
        image = R.drawable.image_saving,
    )
)
