package tmg.f1stats.di

import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import tmg.f1stats.gallery.GalleryViewModel
import tmg.f1stats.home.swiping.HomeSwipingViewModel
import tmg.f1stats.home.datepicker.DatePickerViewModel
import tmg.f1stats.home.static.HomeStaticViewModel
import tmg.f1stats.home.trackpicker.TrackPickerViewModel
import tmg.f1stats.prefs.SharedPrefsDB
import tmg.f1stats.repo.db.*
import tmg.f1stats.repo_firebase.CrashReporterFirebase
import tmg.f1stats.season.race.RaceViewModel
import tmg.f1stats.season.swiper.SeasonViewModel
import tmg.f1stats.repo_firebase.repos.CircuitFirestore
import tmg.f1stats.repo_firebase.repos.HistoryFirestore
import tmg.f1stats.repo_firebase.repos.SeasonOverviewFirestore

var f1Module = module {
    viewModel { HomeSwipingViewModel(get()) }
    viewModel { HomeStaticViewModel(get(), get(), get()) }

    viewModel { DatePickerViewModel() }
    viewModel { TrackPickerViewModel(get()) }
    viewModel { SeasonViewModel(get()) }
    viewModel { RaceViewModel(get()) }
    viewModel { GalleryViewModel() }

    single<SeasonOverviewDB> { SeasonOverviewFirestore(get()) }
    single<HistoryDB> { HistoryFirestore(get()) }
    single<PrefsDB> { SharedPrefsDB(get()) }
    single<CircuitDB> { CircuitFirestore() }
    single<CrashReporter> { CrashReporterFirebase(get(), get()) }
}