package tmg.flashback.upnext.di

import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import tmg.flashback.upnext.controllers.UpNextController
import tmg.flashback.upnext.repository.UpNextRepository
import tmg.flashback.upnext.ui.dashboard.UpNextViewModel

val upNextModule = module {

    viewModel { UpNextViewModel() }

    single { UpNextController(get()) }
    single { UpNextRepository(get()) }
}