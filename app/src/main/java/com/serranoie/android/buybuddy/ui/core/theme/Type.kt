package com.serranoie.android.buybuddy.ui.core.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.serranoie.android.buybuddy.R

val spaceGrotesk = FontFamily(
    Font(R.font.spacegrotesk_light, FontWeight.Light),
    Font(R.font.spacegrotesk_medium, FontWeight.Medium),
    Font(R.font.spacegrotesk_semibold, FontWeight.SemiBold),
    Font(R.font.spacegrotesk_bold, FontWeight.Bold),
    Font(R.font.spacegrotesk_regular, FontWeight.Normal),
)

val workSans = FontFamily(
    Font(R.font.worksans_regular, FontWeight.Normal),
    Font(R.font.worksans_bold, FontWeight.Bold),
    Font(R.font.worksans_medium, FontWeight.Medium),
    Font(R.font.worksans_semibold, FontWeight.SemiBold),
    Font(R.font.worksans_light, FontWeight.Light),
    Font(R.font.worksans_extrabold, FontWeight.ExtraBold),
    Font(R.font.worksans_black, FontWeight.Black),
    Font(R.font.worksans_thin, FontWeight.Thin),
    Font(R.font.worksans_extralight, FontWeight.ExtraLight),
    Font(R.font.worksans_italic, FontWeight.Normal, style = FontStyle.Italic),
    Font(R.font.worksans_bolditalic, FontWeight.Bold, style = FontStyle.Italic),
    Font(R.font.worksans_mediumitalic, FontWeight.Medium, style = FontStyle.Italic),
    Font(R.font.worksans_semibolditalic, FontWeight.SemiBold, style = FontStyle.Italic),
    Font(R.font.worksans_lightitalic, FontWeight.Light, style = FontStyle.Italic),
    Font(R.font.worksans_extrabolditalic, FontWeight.ExtraBold, style = FontStyle.Italic),
    Font(R.font.worksans_blackitalic, FontWeight.Black, style = FontStyle.Italic),
    Font(R.font.worksans_thinitalic, FontWeight.Thin, style = FontStyle.Italic),
    Font(R.font.worksans_extralightitalic, FontWeight.ExtraLight, style = FontStyle.Italic),
)

val Typography = Typography(
    displayLarge = TextStyle(
        fontFamily = spaceGrotesk,
        fontSize = 57.sp,
        fontWeight = FontWeight.Bold,
        lineHeight = 64.0.sp,
    ),
    displayMedium = TextStyle(
        fontFamily = spaceGrotesk,
        fontSize = 45.sp,
        fontWeight = FontWeight.Bold,
        lineHeight = 52.sp,
    ),
    displaySmall = TextStyle(
        fontFamily = spaceGrotesk,
        fontSize = 36.sp,
        fontWeight = FontWeight.Black,
        lineHeight = 44.sp,
    ),
    headlineLarge = TextStyle(
        fontFamily = spaceGrotesk,
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp,
    ),
    headlineMedium = TextStyle(
        fontFamily = spaceGrotesk,
        fontWeight = FontWeight.Bold,
        fontSize = 28.sp,
        lineHeight = 36.sp,
    ),
    headlineSmall = TextStyle(
        fontFamily = spaceGrotesk,
        fontWeight = FontWeight.Medium,
        fontSize = 24.sp,
        lineHeight = 32.sp,
    ),
    titleLarge = TextStyle(
        fontFamily = workSans,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
    ),
    titleMedium = TextStyle(
        fontFamily = workSans,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
    ),
    titleSmall = TextStyle(
        fontFamily = workSans,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
    ),
    bodyLarge = TextStyle(
        fontFamily = workSans,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp,
    ),
    bodyMedium = TextStyle(
        fontFamily = workSans,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.25.sp,
    ),
    bodySmall = TextStyle(
        fontFamily = workSans,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
    ),
    labelLarge = TextStyle(
        fontFamily = workSans,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        fontWeight = FontWeight.Medium,
    ),
    labelMedium = TextStyle(
        fontFamily = workSans,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        fontWeight = FontWeight.Medium,
    ),
    labelSmall = TextStyle(
        fontFamily = workSans,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        fontWeight = FontWeight.Medium,
    ),
)