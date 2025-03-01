package tmg.flashback.data.repo.repository

interface CacheRepository {

    fun shouldSyncCurrentSeason(): Boolean
    fun markedCurrentSeasonSynchronised()

    var seasonsSyncAtLeastOnce: Set<Int>

    var initialSync: Boolean
}