package tmg.crash_reporting.di

import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import tmg.crash_reporting.controllers.CrashController
import tmg.crash_reporting.services.CrashService
import tmg.crash_reporting.services.FirebaseCrashService
import tmg.crash_reporting.repository.CrashRepository
import tmg.crash_reporting.ui.settings.SettingsCrashReportingViewModel

val crashReportingModule = module {

    single<CrashService> { FirebaseCrashService() }
    single { CrashRepository(get()) }
    single { CrashController(get(), get(), get()) }
}