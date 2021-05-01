package tmg.flashback.di

import org.koin.dsl.module
import tmg.flashback.di.data.MockConstructorRepository
import tmg.flashback.di.data.MockDriverRepository
import tmg.flashback.di.data.MockHistoryRepository
import tmg.flashback.di.data.MockSeasonOverviewRepository
import tmg.flashback.di.remoteconfig.MockRemoteConfigRepository
import tmg.flashback.di.rss.MockRSS
import tmg.flashback.data.db.stats.ConstructorRepository
import tmg.flashback.data.db.stats.DriverRepository
import tmg.flashback.data.db.stats.HistoryRepository
import tmg.flashback.data.db.stats.SeasonOverviewRepository
import tmg.flashback.di.remoteconfig.MockRemoteConfigManager
import tmg.flashback.rss.repo.RssAPI

internal val mockModules = module(override = true) {
    // Data
    single<SeasonOverviewRepository> { MockSeasonOverviewRepository }
    single<DriverRepository> { MockDriverRepository }
    single<ConstructorRepository> { MockConstructorRepository }
    single<HistoryRepository> { MockHistoryRepository }

    // Remote config
    single<ConfigurationRepository> { MockRemoteConfigRepository }
    single<ConfigurationManager> { MockRemoteConfigManager }


    // RSS
    single<RssAPI> { MockRSS }
}