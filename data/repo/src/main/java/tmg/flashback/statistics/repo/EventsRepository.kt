package tmg.flashback.statistics.repo

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import tmg.flashback.crash_reporting.controllers.CrashController
import tmg.flashback.formula1.model.Event
import tmg.flashback.statistics.network.api.FlashbackApi
import tmg.flashback.statistics.repo.base.BaseRepository
import tmg.flashback.statistics.repo.mappers.app.EventMapper
import tmg.flashback.statistics.repo.mappers.network.NetworkEventMapper
import tmg.flashback.statistics.room.FlashbackDatabase

class EventsRepository(
    private val api: FlashbackApi,
    private val persistence: FlashbackDatabase,
    crashController: CrashController,
    private val networkEventMapper: NetworkEventMapper,
    private val eventMapper: EventMapper
): BaseRepository(crashController) {

    /**
     * overview/{season}/events.json
     * Fetch overview events for a specific season [season]
     * @param season
     */
    suspend fun fetchEvents(season: Int): Boolean = attempt(
        apiCall = suspend { api.getOverviewEvents(season) },
        msgIfFailed = "overview/${season}/events.json"
    ) { data ->
        val events = data.mapNotNull { networkEventMapper.mapEvent(season, it) }

        persistence.eventsDao().replaceEventsForSeason(season, events)

        return@attempt true
    }

    fun getEvents(season: Int): Flow<List<Event>> {
        return persistence.eventsDao().getEvents(season)
            .map { list -> list.mapNotNull { eventMapper.mapEvent(it) } }
    }
}