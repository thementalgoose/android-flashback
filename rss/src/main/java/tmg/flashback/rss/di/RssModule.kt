package tmg.flashback.rss.di

import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import tmg.flashback.rss.controllers.RSSController
import tmg.flashback.rss.network.RSS
import tmg.flashback.rss.repo.RSSRepository
import tmg.flashback.rss.repo.RssAPI
import tmg.flashback.rss.ui.RSSViewModel
import tmg.flashback.rss.ui.settings.configure.RSSConfigureViewModel
import tmg.flashback.rss.ui.settings.settings.RSSSettingsViewModel

val rssModule = module {

    // UI
    viewModel { RSSViewModel(get(), get(), get()) }
    viewModel { RSSSettingsViewModel(get()) }
    viewModel { RSSConfigureViewModel(get(), get()) }

    // API
    single<RssAPI> { RSS(get(), get()) }

    // Managers

    // Controllers
    single { RSSController(get()) }

    // Repositories
    single { RSSRepository(get(), get()) }
}