package com.serranoie.android.buybuddy.domain.usecase.appentry

data class AppEntryUseCase(
    val readAppEntry: ReadAppEntry,
    val saveAppEntry: SaveAppEntry,
    val checkTheme: CheckThemeUseCase,
    val saveTheme: SaveThemeUseCase,
)