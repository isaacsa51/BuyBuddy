package com.serranoie.android.buybuddy.di

import android.app.Application
import androidx.room.Room
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.serranoie.android.buybuddy.data.persistance.BuyBuddyDatabase
import com.serranoie.android.buybuddy.data.persistance.dao.BuyBuddyDao
import com.serranoie.android.buybuddy.data.persistance.prefs.manager.LocalUserManagerImpl
import com.serranoie.android.buybuddy.data.repository.CategoryRepositoryImpl
import com.serranoie.android.buybuddy.data.repository.ItemRepositoryImpl
import com.serranoie.android.buybuddy.domain.usecase.appentry.AppEntryUseCase
import com.serranoie.android.buybuddy.domain.usecase.appentry.CheckThemeUseCase
import com.serranoie.android.buybuddy.domain.usecase.appentry.ReadAppEntry
import com.serranoie.android.buybuddy.domain.usecase.appentry.SaveAppEntry
import com.serranoie.android.buybuddy.domain.usecase.appentry.SaveThemeUseCase
import com.serranoie.android.buybuddy.domain.usecase.category.GetCategoriesWithItemsUseCase
import com.serranoie.android.buybuddy.domain.usecase.item.DeleteItemUseCase
import com.serranoie.android.buybuddy.domain.usecase.item.GetItemByIdUseCase
import com.serranoie.android.buybuddy.domain.usecase.item.GetItemsUseCase
import com.serranoie.android.buybuddy.domain.usecase.item.GetTotalPriceOfItemsBoughtUseCase
import com.serranoie.android.buybuddy.domain.usecase.item.GetTotalPriceOfItemsToBuyUseCase
import com.serranoie.android.buybuddy.domain.usecase.item.InsertItemUseCase
import com.serranoie.android.buybuddy.domain.usecase.item.InsertItemWithCategoryUseCase
import com.serranoie.android.buybuddy.domain.usecase.item.UpdateItemStatusUseCase
import com.serranoie.android.buybuddy.domain.usecase.item.UpdateItemUseCase
import com.serranoie.android.buybuddy.ui.core.analytics.UserEventsTracker
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TestDependenciesProvider {

    @Provides
    @Singleton
    fun provideBuyBuddyDatabase(app: Application): BuyBuddyDatabase {
        return Room.inMemoryDatabaseBuilder(
            app,
            BuyBuddyDatabase::class.java
        ).build()
    }

    @Provides
    @Singleton
    fun provideFirebaseCrashlytics(): FirebaseCrashlytics {
        val mockCrashlytics = mockk<FirebaseCrashlytics>(relaxed = true)
        every { mockCrashlytics.log(any()) } just Runs
        every { mockCrashlytics.setCustomKey(any<String>(), any<String>()) } just Runs
        every { mockCrashlytics.setCustomKey(any<String>(), any<Int>()) } just Runs
        every { mockCrashlytics.setCustomKey(any<String>(), any<Boolean>()) } just Runs
        every { mockCrashlytics.setCustomKey(any<String>(), any<Long>()) } just Runs
        every { mockCrashlytics.setCustomKey(any<String>(), any<Float>()) } just Runs
        every { mockCrashlytics.setCustomKey(any<String>(), any<Double>()) } just Runs
        return mockCrashlytics
    }

    @Provides
    @Singleton
    fun provideUserEventsTracker(crashlytics: FirebaseCrashlytics): UserEventsTracker {
        return UserEventsTracker(crashlytics)
    }

    @Provides
    @Singleton
    fun provideItemRepository(db: BuyBuddyDatabase): ItemRepositoryImpl {
        return ItemRepositoryImpl(db.buyBuddyDao())
    }

    @Provides
    @Singleton
    fun provideBuyBuddyDao(buyBuddyDatabase: BuyBuddyDatabase): BuyBuddyDao {
        return buyBuddyDatabase.buyBuddyDao()
    }

    @Provides
    @Singleton
    fun provideLocalUserManager(app: Application) = LocalUserManagerImpl(app)

    @Provides
    @Singleton
    fun provideAppEntryUseCase(localUserManager: LocalUserManagerImpl) = AppEntryUseCase(
        readAppEntry = ReadAppEntry(localUserManager),
        saveAppEntry = SaveAppEntry(localUserManager),
        checkTheme = CheckThemeUseCase(localUserManager),
        saveTheme = SaveThemeUseCase(localUserManager),
    )

    @Provides
    fun provideGetCategoriesWithItemsUseCase(repository: CategoryRepositoryImpl): GetCategoriesWithItemsUseCase {
        return GetCategoriesWithItemsUseCase(repository)
    }

    @Provides
    fun provideDeleteItemUseCase(repository: ItemRepositoryImpl): DeleteItemUseCase {
        return DeleteItemUseCase(repository)
    }

    @Provides
    fun provideGetItemByIdUseCase(repository: ItemRepositoryImpl): GetItemByIdUseCase {
        return GetItemByIdUseCase(repository)
    }

    @Provides
    fun provideGetItemsUseCase(repository: ItemRepositoryImpl): GetItemsUseCase {
        return GetItemsUseCase(repository)
    }

    @Provides
    fun provideGetTotalPriceOfItemsBoughtUseCase(repository: ItemRepositoryImpl): GetTotalPriceOfItemsBoughtUseCase {
        return GetTotalPriceOfItemsBoughtUseCase(repository)
    }

    @Provides
    fun provideGetTotalPriceOfItemsToBuyUseCase(repository: ItemRepositoryImpl): GetTotalPriceOfItemsToBuyUseCase {
        return GetTotalPriceOfItemsToBuyUseCase(repository)
    }

    @Provides
    fun provideInsertItemUseCase(repository: ItemRepositoryImpl): InsertItemUseCase {
        return InsertItemUseCase(repository)
    }

    @Provides
    fun provideInsertItemWithCategoryUseCase(repository: ItemRepositoryImpl): InsertItemWithCategoryUseCase {
        return InsertItemWithCategoryUseCase(repository)
    }

    @Provides
    fun provideUpdateItemStatusUseCase(repository: ItemRepositoryImpl): UpdateItemStatusUseCase {
        return UpdateItemStatusUseCase(repository)
    }

    @Provides
    fun provideUpdateItemUseCase(repository: ItemRepositoryImpl): UpdateItemUseCase {
        return UpdateItemUseCase(repository)
    }
}