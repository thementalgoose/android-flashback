package tmg.flashback.firebase.mappers

import tmg.flashback.formula1.model.Constructor
import tmg.flashback.formula1.model.Driver
import tmg.flashback.formula1.model.Round
import tmg.flashback.formula1.model.Season
import tmg.flashback.firebase.mappers.seasonoverview.SeasonOverviewConstructorMapper
import tmg.flashback.firebase.mappers.seasonoverview.SeasonOverviewDriverMapper
import tmg.flashback.firebase.mappers.seasonoverview.SeasonOverviewRaceMapper
import tmg.flashback.firebase.mappers.seasonoverview.SeasonOverviewRaceMapper.Qualifying.Q1
import tmg.flashback.firebase.mappers.seasonoverview.SeasonOverviewRaceMapper.Qualifying.Q2
import tmg.flashback.firebase.mappers.seasonoverview.SeasonOverviewRaceMapper.Qualifying.Q3
import tmg.flashback.firebase.mappers.seasonoverview.SeasonOverviewStandingsMapper
import tmg.flashback.firebase.models.FRound
import tmg.flashback.firebase.models.FSeason
import tmg.utilities.utils.LocalDateUtils.Companion.requireFromDate
import tmg.utilities.utils.LocalTimeUtils.Companion.fromTime

class SeasonOverviewMapper(
    private val raceMapper: SeasonOverviewRaceMapper,
    private val driverMapper: SeasonOverviewDriverMapper,
    private val standingsMapper: SeasonOverviewStandingsMapper,
    private val constructorMapper: SeasonOverviewConstructorMapper
) {

    /**
     * Map a season entire season object
     */
    fun mapSeason(input: FSeason, season: Int): tmg.flashback.formula1.model.Season {
        val drivers = (input.drivers ?: emptyMap()).mapNotNull { (_, value) -> driverMapper.mapDriver(input, value.id) }
        val fConstructors = (input.constructors ?: emptyMap()).map { it.value }
        val constructors = fConstructors.map { constructorMapper.mapConstructor(it) }

        return tmg.flashback.formula1.model.Season(
            season = season,
            drivers = drivers,
            constructors = constructors,
            rounds = (input.race ?: emptyMap())
                .map { (_, round) ->
                    mapRound(round, drivers, constructors)
                },
            driverStandings = input.standings?.let {
                standingsMapper.mapDriverStandings(
                    it,
                    drivers
                )
            } ?: emptyList(),
            constructorStandings = input.standings?.let {
                standingsMapper.mapConstructorStandings(
                    it,
                    fConstructors
                )
            } ?: emptyList(),
        )
    }

    /**
     * Map a round of a season
     */
    fun mapRound(input: FRound, allDrivers: List<tmg.flashback.formula1.model.Driver>, allConstructors: List<tmg.flashback.formula1.model.Constructor>): tmg.flashback.formula1.model.Round {
        return tmg.flashback.formula1.model.Round(
            season = input.season,
            round = input.round,
            date = requireFromDate(input.date),
            time = fromTime(input.time),
            name = input.name,
            wikipediaUrl = input.wiki,
            drivers = allDrivers.map { it.toConstructorDriver(input.round) },
            constructors = allConstructors,
            circuit = raceMapper.mapCircuit(input.circuit),
            q1 = raceMapper.mapQualifying(
                forRound = input.round,
                fieldToBaseFilteringOn = Q1,
                input = input.qualifying,
                allDrivers = allDrivers
            ),
            q2 = raceMapper.mapQualifying(
                forRound = input.round,
                fieldToBaseFilteringOn = Q2,
                input = input.qualifying,
                allDrivers = allDrivers
            ),
            q3 = raceMapper.mapQualifying(
                forRound = input.round,
                fieldToBaseFilteringOn = Q3,
                input = input.qualifying,
                allDrivers = allDrivers
            ),
            qSprint = raceMapper.mapSprintQualifying(
                forRound = input.round,
                input = input.sprintQualifying,
                allDrivers = allDrivers
            ),
            race = raceMapper.mapRace(
                forRound = input.round,
                input = input.race,
                sprintQualifyingData = input.sprintQualifying,
                allDrivers = allDrivers
            )
        )
    }
}