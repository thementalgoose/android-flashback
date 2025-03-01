package tmg.flashback.data.repo.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import tmg.flashback.data.repo.repository.CacheRepository
import tmg.flashback.data.repo.repository.RepoCacheRepository

@Module
@InstallIn(SingletonComponent::class)
internal abstract class RepoModule {

    @Binds
    abstract fun bindsCacheRepository(impl: RepoCacheRepository): CacheRepository
}