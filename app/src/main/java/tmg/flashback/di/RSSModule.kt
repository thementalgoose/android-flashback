package tmg.flashback.di

import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import tmg.flashback.prefs.SharedPrefsRepository
import tmg.flashback.rss.network.RSS
import tmg.flashback.rss.prefs.RSSPrefsRepository
import tmg.flashback.rss.repo.RSSRepository
import tmg.flashback.rss.ui.RSSViewModel
import tmg.flashback.rss.ui.configure.RSSConfigureViewModel
import tmg.flashback.rss.ui.settings.RSSSettingsViewModel

val rssModule = module {

    viewModel { RSSViewModel(get(), get(), get()) }
    viewModel { RSSSettingsViewModel(get()) }
    viewModel { RSSConfigureViewModel(get()) }

    single<RSSPrefsRepository> { SharedPrefsRepository(get()) }
    single<RSSRepository> { RSS(get()) }
}