package tmg.flashback.rss.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import tmg.flashback.RssNavigationComponent
import tmg.flashback.rss.controllers.RSSController
import tmg.flashback.rss.network.RSSService
import tmg.flashback.rss.repo.RSSRepository
import tmg.flashback.rss.repo.RssAPI
import tmg.flashback.rss.ui.configure.ConfigureRSSViewModel
import tmg.flashback.rss.ui.feed.RSSViewModel
import tmg.flashback.rss.ui.settings.SettingsRSSViewModel

val rssModule = module {

    // Navigation
    single { RssNavigationComponent(get()) }

    // UI
    viewModel { RSSViewModel(get(), get(), get(), get(), get(), get()) }
    viewModel { SettingsRSSViewModel(get(), get()) }
    viewModel { ConfigureRSSViewModel(get(), get()) }

    // API
    single<RssAPI> { RSSService(get(), get()) }

    // Controllers
    single { RSSController(get(), get()) }

    // Repositories
    single { RSSRepository(get(), get()) }
}