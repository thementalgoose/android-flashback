package tmg.crash_reporting.di

import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import tmg.crash_reporting.controllers.CrashController
import tmg.crash_reporting.managers.CrashManager
import tmg.crash_reporting.managers.FirebaseCrashManager
import tmg.crash_reporting.repository.CrashRepository
import tmg.crash_reporting.ui.settings.SettingsCrashReportingViewModel

val crashReportingModule = module {

    viewModel { SettingsCrashReportingViewModel(get()) }

    single<CrashManager> { FirebaseCrashManager() }
    single { CrashRepository(get()) }
    single { CrashController(get(), get(), get()) }
}