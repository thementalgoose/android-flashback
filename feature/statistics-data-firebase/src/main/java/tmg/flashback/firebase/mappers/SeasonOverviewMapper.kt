package tmg.flashback.firebase.mappers

import tmg.crash_reporting.controllers.CrashController
import tmg.flashback.data.models.stats.Constructor
import tmg.flashback.data.models.stats.Driver
import tmg.flashback.data.models.stats.Round
import tmg.flashback.data.models.stats.Season
import tmg.flashback.firebase.base.ConverterUtils.fromDateRequired
import tmg.flashback.firebase.base.ConverterUtils.fromTime
import tmg.flashback.firebase.mappers.seasonoverview.SeasonOverviewConstructorMapper
import tmg.flashback.firebase.mappers.seasonoverview.SeasonOverviewDriverMapper
import tmg.flashback.firebase.mappers.seasonoverview.SeasonOverviewRaceMapper
import tmg.flashback.firebase.mappers.seasonoverview.SeasonOverviewRaceMapper.Qualifying.Q1
import tmg.flashback.firebase.mappers.seasonoverview.SeasonOverviewRaceMapper.Qualifying.Q2
import tmg.flashback.firebase.mappers.seasonoverview.SeasonOverviewRaceMapper.Qualifying.Q3
import tmg.flashback.firebase.mappers.seasonoverview.SeasonOverviewStandingsMapper
import tmg.flashback.firebase.models.FRound
import tmg.flashback.firebase.models.FSeason

class SeasonOverviewMapper(
    private val raceMapper: SeasonOverviewRaceMapper,
    private val driverMapper: SeasonOverviewDriverMapper,
    private val standingsMapper: SeasonOverviewStandingsMapper,
    private val constructorMapper: SeasonOverviewConstructorMapper
) {

    /**
     * Map a season entire season object
     */
    fun mapSeason(input: FSeason, season: Int): Season {
        val drivers = (input.drivers ?: emptyMap()).mapNotNull { (_, value) -> driverMapper.mapDriver(input, value.id) }
        val fConstructors = (input.constructors ?: emptyMap()).map { it.value }
        val constructors = fConstructors.map { constructorMapper.mapConstructor(it) }

        return Season(
            season = season,
            drivers = drivers,
            constructors = constructors,
            rounds = (input.race ?: emptyMap())
                .map { (_, round) ->
                    mapRound(round, drivers, constructors)
                },
            driverStandings = input.standings?.let { standingsMapper.mapDriverStandings(it, drivers) } ?: emptyList(),
            constructorStandings = input.standings?.let { standingsMapper.mapConstructorStandings(it, fConstructors) } ?: emptyList(),
        )
    }

    /**
     * Map a round of a season
     */
    fun mapRound(input: FRound, allDrivers: List<Driver>, allConstructors: List<Constructor>): Round {
        return Round(
            season = input.season,
            round = input.round,
            date = fromDateRequired(input.date),
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