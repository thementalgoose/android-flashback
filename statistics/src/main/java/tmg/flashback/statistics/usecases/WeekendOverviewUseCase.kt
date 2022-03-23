package tmg.flashback.statistics.usecases

import kotlinx.coroutines.flow.*
import tmg.flashback.statistics.models.WeekendOverview
import tmg.flashback.statistics.repo.OverviewRepository
import tmg.flashback.statistics.repo.RaceRepository
import tmg.flashback.statistics.repo.SeasonRepository

class WeekendOverviewUseCase(
    private val overviewRepository: OverviewRepository,
    private val fetchSeasonUseCase: FetchSeasonUseCase,
) {
    fun get(season: Int): Flow<List<WeekendOverview>?> = fetchSeasonUseCase
        .fetch(season)
        .flatMapLatest { hasMadeRequest -> overviewRepository
            .getOverview(season)
            .map { overview ->
                if (!hasMadeRequest) {
                    return@map null
                }
                return@map overview.overviewRaces.map { race ->
                    WeekendOverview(
                        season = race.season,
                        raceName = race.raceName,
                        circuitName = race.circuitName,
                        circuitId = race.circuitId,
                        raceCountry = race.country,
                        raceCountryISO = race.countryISO,
                        date = race.date,
                        round = race.round,
                        hasQualifying = race.hasQualifying,
                        hasResults = race.hasResults,
                        schedule = race.schedule,
                    )
                }
            }
        }
}