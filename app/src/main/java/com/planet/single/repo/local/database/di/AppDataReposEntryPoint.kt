package com.planet.single.repo.local.database.di

import com.planet.single.repo.AppDataRepository
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * 在Hilt不支持的类中注入依赖：
 * 参考：https://developer.android.com/training/dependency-injection/hilt-android#not-supported
 */
@EntryPoint
@InstallIn(SingletonComponent::class)
interface AppDataRepositoryEntryPoint {
    fun provideDataRepository(): AppDataRepository
}