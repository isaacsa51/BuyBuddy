package com.serranoie.android.buybuddy.domain.model

import java.util.Date

data class Item(
    val itemId: Int?,
    val name: String,
    val categoryId: Int,
    val description: String,
    val usage: String,
    val benefits: String,
    val disadvantages: String?,
    val price: Double,
    val reminderDate: Date?,
    val reminderTime: Date?,
    val status: Boolean = false,
)
