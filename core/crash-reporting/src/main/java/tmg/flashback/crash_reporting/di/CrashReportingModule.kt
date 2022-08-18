package tmg.flashback.crash_reporting.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import tmg.flashback.crash_reporting.services.CrashService
import tmg.flashback.crash_reporting.services.FirebaseCrashService

@Module
@InstallIn(SingletonComponent::class)
internal abstract class CrashReportingModule {

    @Binds
    abstract fun bindCrashService(impl: FirebaseCrashService): CrashService
}