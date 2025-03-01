package tmg.flashback.data.repo

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import tmg.flashback.crashlytics.manager.CrashlyticsManager
import tmg.flashback.device.managers.NetworkConnectivityManager
import tmg.flashback.data.persistence.FlashbackDatabase
import tmg.flashback.data.repo.base.BaseRepository
import tmg.flashback.data.repo.mappers.app.EventMapper
import tmg.flashback.data.repo.mappers.network.NetworkEventMapper
import tmg.flashback.flashbackapi.api.api.FlashbackApi
import tmg.flashback.formula1.model.Event
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EventsRepository @Inject constructor(
    private val api: FlashbackApi,
    private val persistence: FlashbackDatabase,
    crashController: CrashlyticsManager,
    networkConnectivityManager: NetworkConnectivityManager,
    private val networkEventMapper: NetworkEventMapper,
    private val eventMapper: EventMapper
): BaseRepository(crashController, networkConnectivityManager) {

    /**
     * overview/{season}/events.json
     * Fetch overview events for a specific season [season]
     * @param season
     */
    internal suspend fun fetchEvents(season: Int): Boolean = attempt(
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