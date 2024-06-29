package com.serranoie.android.buybuddy.data.persistance

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.serranoie.android.buybuddy.data.persistance.dao.BuyBuddyDao
import com.serranoie.android.buybuddy.data.persistance.entity.CategoryEntity
import com.serranoie.android.buybuddy.data.persistance.entity.ItemEntity
import com.serranoie.android.buybuddy.data.util.Converters

@Database(entities = [ItemEntity::class, CategoryEntity::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class BuyBuddyDatabase : RoomDatabase() {
    abstract fun buyBuddyDao(): BuyBuddyDao
}