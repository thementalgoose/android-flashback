package tmg.f1stats.di

import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import tmg.f1stats.gallery.GalleryViewModel
import tmg.f1stats.home.swiping.HomeSwipingViewModel
import tmg.f1stats.home.datepicker.DatePickerViewModel
import tmg.f1stats.home.static.HomeStaticViewModel
import tmg.f1stats.home.trackpicker.TrackPickerViewModel
import tmg.f1stats.prefs.SharedPrefsDB
import tmg.f1stats.repo.db.PrefsDB
import tmg.f1stats.season.race.RaceViewModel
import tmg.f1stats.season.swiper.SeasonViewModel
import tmg.f1stats.repo.db.SeasonOverviewDB
import tmg.f1stats.repo.db.SyncDB
import tmg.f1stats.repo_firebase.repos.SeasonOverviewFirestore
import tmg.f1stats.repo_firebase.repos.SyncFirestore
import tmg.f1stats.settings.SettingsViewModel
import tmg.f1stats.settings.sync.SettingsSyncViewModel

var f1Module = module {
    viewModel { HomeSwipingViewModel(get()) }
    viewModel { HomeStaticViewModel(get()) }

    viewModel { DatePickerViewModel() }
    viewModel { TrackPickerViewModel(get()) }
    viewModel { SeasonViewModel(get()) }
    viewModel { RaceViewModel(get()) }
    viewModel { GalleryViewModel() }

    viewModel { SettingsViewModel() }
    viewModel { SettingsSyncViewModel(get()) }

    single<SeasonOverviewDB> { SeasonOverviewFirestore() }
    single<SyncDB> { SyncFirestore() }
    single<PrefsDB> { SharedPrefsDB(get()) }
}