package tmg.flashback.newrelic.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import tmg.flashback.device.BuildConfig
import tmg.flashback.device.managers.BuildConfigManager
import tmg.flashback.newrelic.services.NewRelicService
import tmg.flashback.newrelic.services.NewRelicServiceImpl
import tmg.flashback.newrelic.services.NewRelicServiceNoop
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal class NewRelicModule {

    // Update build.gradle to apply plugin newrelic to test in debug
    @Provides
    @Singleton
    fun provideNewRelicService(
        noop: NewRelicServiceNoop,
        impl: NewRelicServiceImpl,
        buildConfigManager: BuildConfigManager
    ): NewRelicService = when {
        BuildConfig.DEBUG -> noop
        buildConfigManager.isEmulator -> noop
        else -> impl
    }
}