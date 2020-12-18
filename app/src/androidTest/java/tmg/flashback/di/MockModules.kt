package tmg.flashback.di

import org.koin.dsl.module
import tmg.flashback.di.repositories.MockRemoteConfigRepository
import tmg.flashback.repo.config.RemoteConfigRepository
import tmg.flashback.repo.db.stats.SeasonOverviewRepository

val mockModules = module(override = true) {
    single<RemoteConfigRepository> { MockRemoteConfigRepository() }
}