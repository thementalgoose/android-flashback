package tmg.flashback.crashlytics.di

import android.content.Context
import dagger.Binds
import dagger.Module
import dagger.hilt.EntryPoint
import dagger.hilt.EntryPoints
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import tmg.flashback.crashlytics.manager.CrashlyticsManager
import tmg.flashback.crashlytics.services.FirebaseCrashService
import tmg.flashback.crashlytics.services.FirebaseFirebaseCrashServiceImpl

@Module
@InstallIn(SingletonComponent::class)
internal abstract class CrashReportingModule {

    @Binds
}

@EntryPoint
@InstallIn(SingletonComponent::class)
interface CrashModule {
    fun crashManager(): CrashlyticsManager

    companion object {
        fun entryPoints(context: Context): CrashModule {
            return EntryPoints.get(context.applicationContext, CrashModule::class.java)
        }
    }
    abstract fun bindCrashService(impl: FirebaseCrashServiceImpl): FirebaseCrashService
}