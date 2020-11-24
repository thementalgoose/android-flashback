package tmg.flashback.di

import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import tmg.flashback.prefs.SharedPrefsDB
import tmg.flashback.rss.network.RSS
import tmg.flashback.repo.db.news.RSSDB
import tmg.flashback.rss.prefs.RSSPrefsDB
import tmg.flashback.rss.ui.RSSViewModel
import tmg.flashback.rss.ui.configure.RSSConfigureViewModel
import tmg.flashback.rss.ui.settings.RSSSettingsViewModel

val rssModule = module {

    viewModel { RSSViewModel(get(), get(), get(), get()) }
    viewModel { RSSSettingsViewModel(get(), get()) }
    viewModel { RSSConfigureViewModel(get(), get()) }

    single<RSSPrefsDB> { SharedPrefsDB(get()) }
    single<RSSDB> { RSS(get()) }
}