package tmg.flashback.firebase.mappers

import tmg.crash_reporting.controllers.CrashController

class SeasonOverviewMapper(
    private val crashController: CrashController
) {

//    fun mapSeason(input: FSeason, season: Int): Season {
//        val drivers = (input.drivers ?: emptyMap()).mapNotNull { (_, value) -> mapDriver(input, value.id) }
//        val fConstructors = (input.constructors ?: emptyMap()).map { it.value }
//
//        return Season(
//            season = season,
//            drivers = drivers,
//            constructors = fConstructors.map { mapConstructor(it) },
//            rounds = input.race
//                ?.map { (_, value) ->
//                    mapRound(value, drivers, fConstructors)
//                }
//                ?: emptyList(),
//            driverStandings = input.standings?.let { mapDriverStandings(it, drivers) } ?: emptyList(),
//            constructorStandings = input.standings?.let { mapConstructorStandings(it, fConstructors) } ?: emptyList()
//        )
//    }
//
//    fun mapRound(input: FRound, allDrivers: List<Driver>, allConstructors: List<FSeasonOverviewConstructor>): Round {
//
//        return Round(
//            season = input.season,
//            round = input.round,
//            date = fromDateRequired(input.date),
//            time = fromTime(input.time),
//            name = input.name,
//            wikipediaUrl = input.wiki,
//            drivers = allDrivers.map { it.toConstructorDriver(input.round) },
//            constructors = allConstructors.map { mapConstructor(it) },
//            circuit = mapCircuit(input.circuit),
//        )
//    }
}