package tmg.flashback.di

import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import tmg.flashback.rss.ui.RSSViewModel
import tmg.flashback.rss.ui.configure.RSSConfigureViewModel
import tmg.flashback.rss.ui.settings.RSSSettingsViewModel

val rssViewModelModule = module {

    viewModel { RSSViewModel(get(), get(), get()) }
    viewModel { RSSSettingsViewModel(get()) }
    viewModel { RSSConfigureViewModel(get(), get()) }
}