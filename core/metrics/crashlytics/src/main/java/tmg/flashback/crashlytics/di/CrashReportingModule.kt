package tmg.flashback.crashlytics.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import tmg.flashback.crashlytics.services.FirebaseCrashService
import tmg.flashback.crashlytics.services.FirebaseCrashServiceImpl

@Module
@InstallIn(SingletonComponent::class)
internal abstract class CrashReportingModule {

    @Binds
    abstract fun bindCrashService(impl: FirebaseCrashServiceImpl): FirebaseCrashService
}