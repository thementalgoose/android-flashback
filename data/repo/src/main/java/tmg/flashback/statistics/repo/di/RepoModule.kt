package tmg.flashback.statistics.repo.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import tmg.flashback.statistics.repo.repository.CacheRepository
import tmg.flashback.statistics.repo.repository.RepoCacheRepository

@Module
@InstallIn(SingletonComponent::class)
internal abstract class RepoModule {

    @Binds
    abstract fun bindsCacheRepository(impl: RepoCacheRepository): CacheRepository
}