package tmg.flashback.rss.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import tmg.flashback.rss.network.RSSService
import tmg.flashback.rss.repo.RssAPI

@Module
@InstallIn(SingletonComponent::class)
internal abstract class RssModule {

    @Binds
    abstract fun bindsRssApi(impl: RSSService): RssAPI
}