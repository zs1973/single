package com.planet.single.repo.remote.di

import com.planet.single.repo.remote.Api
import com.planet.single.repo.remote.config.CustomNetWorkProvider
import com.planet.single.repo.remote.config.DefaultNetWorkProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton

/**
 *作者：张硕
 *日期：2022/02/22
 *邮箱：305562245@qq.com
 *描述：
 **/
@Module
@InstallIn(SingletonComponent::class)
class AppNetWorkModule {

    /**
     * 提供一个自定义配置的网络请求客户端。
     * @param customNetWorkProvider [CustomNetWorkProvider]
     * @return [Api]
     */
    @Singleton
    @Provides
    @BindCustomNetWorkProvider
    fun provideCustomNetWorkClient(customNetWorkProvider: CustomNetWorkProvider): Api {
        return customNetWorkProvider.getDefaultNetworkClient().create(Api::class.java)
    }


    /**
     * 提供一个默认配置的网络请求客户端。
     * @param defaultNetWorkProvider [DefaultNetWorkProvider]
     * @return [Api]
     */
    @Singleton
    @Provides
    @BindDefaultNetWorkProvider
    fun provideDefaultNetWorkClient(defaultNetWorkProvider: DefaultNetWorkProvider): Api {
        return defaultNetWorkProvider.getDefaultNetworkClient().create(Api::class.java)
    }
}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class BindDefaultNetWorkProvider

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class BindCustomNetWorkProvider