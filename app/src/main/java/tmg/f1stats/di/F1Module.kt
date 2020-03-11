package tmg.f1stats.di

import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import tmg.f1stats.home.season.race.SeasonRaceViewModel
import tmg.f1stats.home.season.SeasonViewModel
import tmg.f1stats.repo.db.SeasonOverviewDB
import tmg.f1stats.repo.db.SyncDB
import tmg.f1stats.repo_firebase.repos.SeasonOverviewFirestore
import tmg.f1stats.repo_firebase.repos.SyncFirestore

var f1Module = module {
    viewModel { SeasonViewModel(get()) }
    viewModel { SeasonRaceViewModel() }

    single<SeasonOverviewDB> { SeasonOverviewFirestore() }
    single<SyncDB> { SyncFirestore() }
}