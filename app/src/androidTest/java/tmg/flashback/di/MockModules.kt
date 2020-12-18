package tmg.flashback.di

import org.koin.dsl.module
import tmg.flashback.di.remoteconfig.MockRemoteConfigRepository
import tmg.flashback.di.rss.MockRSS
import tmg.flashback.repo.config.RemoteConfigRepository
import tmg.flashback.rss.network.RSS
import tmg.flashback.rss.repo.RSSRepository

val mockModules = module(override = true) {
    // Remote config
    single<RemoteConfigRepository> { MockRemoteConfigRepository }

    // RSS
    single<RSSRepository> { MockRSS }
}