package tmg.flashback.di

import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import tmg.flashback.rss.RSS
import tmg.flashback.repo.db.news.RSSDB
import tmg.flashback.rss.RSSViewModel
import tmg.flashback.rss.configure.RSSConfigureViewModel

val rssModule = module {

    viewModel { RSSViewModel(get(), get(), get(), get()) }
    viewModel { RSSConfigureViewModel(get(), get()) }

    single<RSSDB> { RSS(get()) }
}