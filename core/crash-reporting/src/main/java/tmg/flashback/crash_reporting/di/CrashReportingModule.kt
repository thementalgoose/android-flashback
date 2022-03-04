package tmg.flashback.crash_reporting.di

import org.koin.dsl.module
import tmg.flashback.crash_reporting.controllers.CrashController
import tmg.flashback.crash_reporting.repository.CrashRepository
import tmg.flashback.crash_reporting.services.CrashService
import tmg.flashback.crash_reporting.services.FirebaseCrashService
import tmg.flashback.crash_reporting.usecases.InitialiseCrashReportingUseCase

val crashReportingModule = module {

    single<CrashService> { FirebaseCrashService() }

    single { CrashRepository(get()) }
    single { CrashController(get(), get()) }

    factory { InitialiseCrashReportingUseCase(get(), get()) }
}