package tmg.flashback.crash_reporting.di

import android.content.Context
import com.scottyab.rootbeer.RootBeer
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import tmg.flashback.crash_reporting.services.CrashService
import tmg.flashback.crash_reporting.services.FirebaseCrashService

@Module
@InstallIn(SingletonComponent::class)
internal class CrashReportingModule {

    @Provides
    fun bindCrashService(impl: FirebaseCrashService): CrashService = impl

    @Provides
    fun bindRootBeer(
        @ApplicationContext applicationContext: Context
    ): RootBeer = RootBeer(applicationContext)
}