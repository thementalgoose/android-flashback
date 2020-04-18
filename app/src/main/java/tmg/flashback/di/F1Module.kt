package tmg.flashback.di

import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import tmg.flashback.admin.lockout.LockoutViewModel
import tmg.flashback.gallery.GalleryViewModel
import tmg.flashback.home.swiping.HomeSwipingViewModel
import tmg.flashback.home.datepicker.DatePickerViewModel
import tmg.flashback.home.static.HomeStaticViewModel
import tmg.flashback.home.trackpicker.TrackPickerViewModel
import tmg.flashback.prefs.SharedPrefsDB
import tmg.flashback.repo.db.*
import tmg.flashback.repo_firebase.CrashReporterFirebase
import tmg.flashback.season.race.RaceViewModel
import tmg.flashback.season.swiper.SeasonViewModel
import tmg.flashback.repo_firebase.repos.CircuitFirestore
import tmg.flashback.repo_firebase.repos.DataFirestore
import tmg.flashback.repo_firebase.repos.HistoryFirestore
import tmg.flashback.repo_firebase.repos.SeasonOverviewFirestore

var f1Module = module {

    viewModel { HomeSwipingViewModel(get()) }
    viewModel { HomeStaticViewModel(get(), get(), get(), get()) }

    viewModel { DatePickerViewModel() }
    viewModel { TrackPickerViewModel(get()) }
    viewModel { SeasonViewModel(get()) }
    viewModel { RaceViewModel(get()) }
    viewModel { GalleryViewModel() }
    viewModel { LockoutViewModel(get()) }

    single<SeasonOverviewDB> { SeasonOverviewFirestore(get()) }
    single<HistoryDB> { HistoryFirestore(get()) }
    single<PrefsDB> { SharedPrefsDB(get()) }
    single<CircuitDB> { CircuitFirestore() }
    single<CrashReporter> { CrashReporterFirebase(get(), get()) }
    single<DataDB> { DataFirestore() }
}