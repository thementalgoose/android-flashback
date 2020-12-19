package tmg.flashback.di

import org.koin.dsl.module
import tmg.flashback.di.data.MockConstructorRepository
import tmg.flashback.di.data.MockDriverRepository
import tmg.flashback.di.data.MockHistoryRepository
import tmg.flashback.di.data.MockSeasonOverviewRepository
import tmg.flashback.di.remoteconfig.MockRemoteConfigRepository
import tmg.flashback.di.rss.MockRSS
import tmg.flashback.repo.config.RemoteConfigRepository
import tmg.flashback.repo.db.stats.ConstructorRepository
import tmg.flashback.repo.db.stats.DriverRepository
import tmg.flashback.repo.db.stats.HistoryRepository
import tmg.flashback.repo.db.stats.SeasonOverviewRepository
import tmg.flashback.rss.network.RSS
import tmg.flashback.rss.repo.RSSRepository

internal val mockModules = module(override = true) {
    // Data
    single<SeasonOverviewRepository> { MockSeasonOverviewRepository }
    single<DriverRepository> { MockDriverRepository }
    single<ConstructorRepository> { MockConstructorRepository }
    single<HistoryRepository> { MockHistoryRepository }

    // Remote config
    single<RemoteConfigRepository> { MockRemoteConfigRepository }

    // RSS
    single<RSSRepository> { MockRSS }
}