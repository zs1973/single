package com.planet.single.repo.local.database.di

import com.planet.single.repo.local.database.AppDatabase
import com.planet.single.repo.local.database.dao.AppDao
import com.planet.utils.AppUtils
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 *作者：张硕
 *日期：11/19/2021
 *邮箱：305562245@qq.com
 **/
@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {
    @Singleton
    @Provides
    fun provideDataBase(): AppDatabase {
        return AppDatabase.getDatabase(AppUtils.getApp())
    }

    @Singleton
    @Provides
    fun provideAppDao(appDatabase: AppDatabase): AppDao {
        return appDatabase.getAppDao()
    }

}