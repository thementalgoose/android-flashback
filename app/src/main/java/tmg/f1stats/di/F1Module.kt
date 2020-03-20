package tmg.f1stats.di

import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import tmg.f1stats.gallery.GalleryViewModel
import tmg.f1stats.home.HomeViewModel
import tmg.f1stats.season.race.SeasonRaceViewModel
import tmg.f1stats.season.SeasonViewModel
import tmg.f1stats.repo.db.SeasonOverviewDB
import tmg.f1stats.repo.db.SyncDB
import tmg.f1stats.repo_firebase.repos.SeasonOverviewFirestore
import tmg.f1stats.repo_firebase.repos.SyncFirestore
import tmg.f1stats.settings.SettingsViewModel
import tmg.f1stats.settings.sync.SettingsSyncViewModel

var f1Module = module {
    viewModel { HomeViewModel() }
    viewModel { SeasonViewModel(get()) }
    viewModel { SeasonRaceViewModel(get()) }
    viewModel { GalleryViewModel() }

    viewModel { SettingsViewModel() }
    viewModel { SettingsSyncViewModel(get()) }

    single<SeasonOverviewDB> { SeasonOverviewFirestore() }
    single<SyncDB> { SyncFirestore() }
}