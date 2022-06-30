package tmg.flashback.web.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import tmg.flashback.web.WebNavigationComponent
import tmg.flashback.web.repository.WebBrowserRepository
import tmg.flashback.web.ui.settings.SettingsWebBrowserViewModel
import tmg.flashback.web.usecases.PickBrowserUseCase

val webBrowserModule = module {
    single { WebNavigationComponent(get()) }

    single { WebBrowserRepository(get()) }

    single { PickBrowserUseCase(get()) }

    viewModel { SettingsWebBrowserViewModel(get()) }
}