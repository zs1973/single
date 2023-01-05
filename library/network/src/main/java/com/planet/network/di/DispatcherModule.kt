package com.planet.network.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

/**
 *作者：张硕
 *日期：2022/02/17
 *邮箱：305562245@qq.com
 *描述：
 **/
@Module
@InstallIn(SingletonComponent::class)
class DispatcherModule {
    @Singleton
    @Provides
    fun provideIoDispatcher(): CoroutineDispatcher = Dispatchers.IO
}