package tmg.flashback.domain.repo.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import tmg.flashback.domain.repo.repository.CacheRepository
import tmg.flashback.domain.repo.repository.RepoCacheRepository

@Module
@InstallIn(SingletonComponent::class)
internal abstract class RepoModule {

    @Binds
    abstract fun bindsCacheRepository(impl: RepoCacheRepository): CacheRepository
}