package tmg.flashback.di.repositories

import tmg.flashback.repo.config.RemoteConfigRepository

class MockRemoteConfigRepository: RemoteConfigRepository {

    override suspend fun update(andActivate: Boolean): Boolean {
        return true
    }

    override suspend fun activate(): Boolean {
        return true
    }

    override val defaultYear: Int
        get() = 2021
    override val banner: String
        get() = "MOCK BANNER"
    override val rss: Boolean
        get() = true
}