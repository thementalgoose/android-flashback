package tmg.flashback.statistics.repo.repository

interface CacheRepository {

    fun shouldSyncCurrentSeason(): Boolean
    fun markedCurrentSeasonSynchronised()

    var seasonsSyncAtLeastOnce: Set<Int>
}