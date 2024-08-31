package com.serranoie.android.buybuddy.di

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.serranoie.android.buybuddy.data.persistance.BuyBuddyDatabase
import com.serranoie.android.buybuddy.data.persistance.dao.BuyBuddyDao
import com.serranoie.android.buybuddy.data.persistance.prefs.manager.LocalUserManagerImpl
import com.serranoie.android.buybuddy.data.repository.CategoryRepositoryImpl
import com.serranoie.android.buybuddy.data.repository.ItemRepositoryImpl
import com.serranoie.android.buybuddy.data.util.Constants
import com.serranoie.android.buybuddy.domain.manager.LocalUserManager
import com.serranoie.android.buybuddy.domain.usecase.appentry.AppEntryUseCase
import com.serranoie.android.buybuddy.domain.usecase.appentry.CheckThemeUseCase
import com.serranoie.android.buybuddy.domain.usecase.appentry.ReadAppEntry
import com.serranoie.android.buybuddy.domain.usecase.appentry.SaveAppEntry
import com.serranoie.android.buybuddy.domain.usecase.appentry.SaveThemeUseCase
import com.serranoie.android.buybuddy.domain.usecase.category.GetCategoriesWithItemsUseCase
import com.serranoie.android.buybuddy.domain.usecase.category.GetCategoryByIdUseCase
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
import com.serranoie.android.buybuddy.ui.util.PreferenceUtil
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DependenciesProvider {

    // Extension function to create a DataStore instance
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

    @Provides
    fun provideUserEventsTracker(crashlytics: FirebaseCrashlytics): UserEventsTracker {
        return UserEventsTracker(crashlytics)
    }

    @Provides
    fun provideFirebaseCrashlytics(): FirebaseCrashlytics {
        return FirebaseCrashlytics.getInstance()
    }

    @Provides
    @Singleton
    fun provideBuyBuddyDatabase(@ApplicationContext appContext: Context): BuyBuddyDatabase {
        return Room.databaseBuilder(
            appContext, BuyBuddyDatabase::class.java, Constants.DB_NAME
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    @Singleton
    fun provideBuyBuddyDao(buyBuddyDatabase: BuyBuddyDatabase): BuyBuddyDao {
        return buyBuddyDatabase.buyBuddyDao()
    }

    @Provides
    @Singleton
    fun provideLocalUserManager(
        application: Application,
    ): LocalUserManager = LocalUserManagerImpl(application)

    @Provides
    @Singleton
    fun providePreferenceUtil(@ApplicationContext context: Context) = PreferenceUtil(context)

    @Provides
    @Singleton
    fun provideAppEntryUseCase(localUserManager: LocalUserManager) = AppEntryUseCase(
        readAppEntry = ReadAppEntry(localUserManager),
        saveAppEntry = SaveAppEntry(localUserManager),
        checkTheme = CheckThemeUseCase(localUserManager),
        saveTheme = SaveThemeUseCase(localUserManager),
    )

    @Provides
    fun provideGetCategoryByIdUseCase(repository: CategoryRepositoryImpl): GetCategoryByIdUseCase {
        return GetCategoryByIdUseCase(repository)
    }

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