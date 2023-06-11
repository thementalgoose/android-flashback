package tmg.flashback.rss.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import tmg.flashback.rss.contract.usecases.RSSAppShortcutUseCase
import tmg.flashback.rss.usecases.RSSAppShortcutUseCaseImpl

@Module
@InstallIn(SingletonComponent::class)
internal abstract class RssModule {

    @Binds
    abstract fun bindsRssAppShortcutUseCase(impl: RSSAppShortcutUseCaseImpl): RSSAppShortcutUseCase
}