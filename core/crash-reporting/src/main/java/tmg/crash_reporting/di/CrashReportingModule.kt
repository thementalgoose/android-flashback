package tmg.crash_reporting.di

import org.koin.dsl.module
import tmg.crash_reporting.controllers.CrashController
import tmg.crash_reporting.managers.CrashManager
import tmg.crash_reporting.managers.FirebaseCrashManager
import tmg.crash_reporting.repository.CrashRepository

val crashReportingModule = module {
    single<CrashManager> { FirebaseCrashManager() }
    single { CrashRepository(get()) }
    single { CrashController(get(), get(), get()) }
}