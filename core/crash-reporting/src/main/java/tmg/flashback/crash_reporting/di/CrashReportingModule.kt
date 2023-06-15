package tmg.flashback.crash_reporting.di

import android.content.Context
import dagger.Binds
import dagger.Module
import dagger.hilt.EntryPoint
import dagger.hilt.EntryPoints
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import tmg.flashback.crash_reporting.manager.CrashManager
import tmg.flashback.crash_reporting.services.CrashService
import tmg.flashback.crash_reporting.services.FirebaseCrashService

@Module
@InstallIn(SingletonComponent::class)
internal abstract class CrashReportingModule {

    @Binds
    abstract fun bindCrashService(impl: FirebaseCrashService): CrashService
}

@EntryPoint
@InstallIn(SingletonComponent::class)
interface CrashModule {
    fun crashManager(): CrashManager

    companion object {
        fun entryPoints(context: Context): CrashModule {
            return EntryPoints.get(context.applicationContext, CrashModule::class.java)
        }
    }
}