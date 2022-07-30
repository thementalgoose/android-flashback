package tmg.flashback.statistics.repo.mappers.app

import tmg.flashback.formula1.model.*
import tmg.flashback.formula1.model.RaceQualifyingType.*
import tmg.flashback.formula1.utils.toLapTime
import tmg.flashback.statistics.room.models.race.QualifyingDriverResult
import tmg.flashback.statistics.room.models.race.RaceDriverResult
import tmg.flashback.statistics.room.models.race.RaceInfoWithCircuit
import tmg.flashback.statistics.room.models.race.SprintDriverResult
import tmg.utilities.utils.LocalDateUtils.Companion.requireFromDate
import tmg.utilities.utils.LocalTimeUtils.Companion.fromTime
import javax.inject.Inject

class RaceMapper @Inject constructor(
    private val circuitMapper: CircuitMapper,
    private val driverDataMapper: DriverDataMapper,
    private val constructorDataMapper: ConstructorDataMapper,
    private val scheduleMapper: ScheduleMapper
) {

    private enum class Qualifying { Q1, Q2, Q3 }

    fun mapRaceInfo(data: tmg.flashback.statistics.room.models.race.Race): RaceInfo {
        return RaceInfo(
            season = data.raceInfo.season,
            round = data.raceInfo.round,
            date = requireFromDate(data.raceInfo.date),
            time = fromTime(data.raceInfo.time),
            name = data.raceInfo.name,
            laps = data.raceInfo.laps,
            wikipediaUrl = data.raceInfo.wikiUrl,
            youtube = data.raceInfo.youtube,
            circuit = circuitMapper.mapCircuit(data.circuit)!!,
        )
    }

    fun mapRaceInfoWithCircuit(data: RaceInfoWithCircuit): RaceInfo {
        return RaceInfo(
            season = data.raceInfo.season,
            round = data.raceInfo.round,
            date = requireFromDate(data.raceInfo.date),
            time = fromTime(data.raceInfo.time),
            name = data.raceInfo.name,
            laps = data.raceInfo.laps,
            wikipediaUrl = data.raceInfo.wikiUrl,
            youtube = data.raceInfo.youtube,
            circuit = circuitMapper.mapCircuit(data.circuit)!!,
        )
    }

    fun mapRace(data: tmg.flashback.statistics.room.models.race.Race?): Race? {
        if (data == null) return null
        return Race(
            raceInfo = mapRaceInfo(data),
            sprint = mapSprint(data.sprint),
            qualifying = mapQualifying(data.qualifying),
            race = mapRace(
                input = data.race
            ),
            schedule = data.schedule.mapNotNull { scheduleMapper.mapSchedule(it) }
        )
    }


    private fun mapQualifying(input: List<QualifyingDriverResult>): List<RaceQualifyingRound> {
        val qualifyingData: MutableList<RaceQualifyingRound> = mutableListOf()

        // Q1
        if (input.any { it.qualifyingResult.q1 != null }) {
            val lapTimeOrder = input
                .sortedBy { it.qualifyingResult.q1?.toLapTime()?.totalMillis ?: Int.MAX_VALUE }
                .mapIndexed { index, item -> Pair(item.driver.id, index + 1) }
            val driverListForRound = input.map {
                RaceQualifyingResult(
                    driver = DriverConstructor(driverDataMapper.mapDriver(it.driver), constructorDataMapper.mapConstructorData(it.constructor)),
                    lapTime = it.qualifyingResult.q1?.toLapTime(),
                    position = lapTimeOrder.firstOrNull { (id, _) -> id == it.driver.id }?.second
                        ?: it.qualifyingResult.qualified
                        ?: -1
                )
            }
            qualifyingData.add(RaceQualifyingRound(Q1, 1,driverListForRound.sortedBy { it.position }))
        }
        // Q2
        if (input.any { it.qualifyingResult.q2 != null }) {
            val lapTimeOrder = input
                .sortedBy { it.qualifyingResult.q2?.toLapTime()?.totalMillis ?: Int.MAX_VALUE }
                .mapIndexed { index, item -> Pair(item.driver.id, index + 1) }
            val driverListForRound = input.map {
                RaceQualifyingResult(
                    driver = DriverConstructor(driverDataMapper.mapDriver(it.driver), constructorDataMapper.mapConstructorData(it.constructor)),
                    lapTime = it.qualifyingResult.q2?.toLapTime(),
                    position = lapTimeOrder.firstOrNull { (id, _) -> id == it.driver.id }?.second
                        ?: qualifyingData.firstOrNull { it.order == 1 }?.results?.firstOrNull { model -> model.driver.driver.id == it.driver.id }?.position
                        ?: it.qualifyingResult.qualified
                        ?: -1
                )
            }
            qualifyingData.add(RaceQualifyingRound(Q2, 2, driverListForRound.sortedBy { it.position }))
        }
        // Q3
        if (input.any { it.qualifyingResult.q3 != null }) {
            val lapTimeOrder = input
                .sortedBy { it.qualifyingResult.q3?.toLapTime()?.totalMillis ?: Int.MAX_VALUE }
                .mapIndexed { index, item -> Pair(item.driver.id, index + 1) }
            val driverListForRound = input.map {
                RaceQualifyingResult(
                    driver = DriverConstructor(driverDataMapper.mapDriver(it.driver), constructorDataMapper.mapConstructorData(it.constructor)),
                    lapTime = it.qualifyingResult.q3?.toLapTime(),
                    position = lapTimeOrder.firstOrNull { (id, _) -> id == it.driver.id }?.second
                        ?: qualifyingData.firstOrNull { it.order == 2 }?.results?.firstOrNull { model -> model.driver.driver.id == it.driver.id }?.position
                        ?: qualifyingData.firstOrNull { it.order == 1 }?.results?.firstOrNull { model -> model.driver.driver.id == it.driver.id }?.position
                        ?: it.qualifyingResult.qualified
                        ?: -1
                )
            }
            qualifyingData.add(RaceQualifyingRound(Q3, 3, driverListForRound.sortedBy { it.position }))
        }

        return qualifyingData
    }

    private fun mapSprint(input: List<SprintDriverResult>?): List<RaceSprintResult> {
        if (input == null || input.isEmpty()) return emptyList()
        val allDrivers = input.map { result ->
            DriverConstructor(
                driver = driverDataMapper.mapDriver(result.driver),
                constructor = constructorDataMapper.mapConstructorData(result.constructor)
            )
        }
        return input
            .map { result ->
                RaceSprintResult(
                    driver = DriverConstructor(driverDataMapper.mapDriver(result.driver), constructorDataMapper.mapConstructorData(result.constructor)),
                    time = result.sprintResult.time?.toLapTime(),
                    points = result.sprintResult.points,
                    grid = result.sprintResult.gridPos ?: 0,
                    finish = result.sprintResult.finished,
                    status = result.sprintResult.status
                )
            }
    }

    /**
     * Map sprint data
     *
     * @param input Map of driver id -> sprint quali result
     */
    private fun mapRace(input: List<RaceDriverResult>?): List<RaceRaceResult> {
        if (input == null || input.isEmpty()) return emptyList()
        val allDrivers = input.map { result ->
            DriverConstructor(
                driver = driverDataMapper.mapDriver(result.driver),
                constructor = constructorDataMapper.mapConstructorData(result.constructor)
            )
        }
        return input
            .map { race ->
                return@map allDrivers
                    .first { it.driver.id == race.driver.id }
                    .let { Pair(it, race.raceResult) }
            }
            .map { (driver, raceResult) ->
                RaceRaceResult(
                    driver = driver,
                    time = raceResult.time?.toLapTime(),
                    points = raceResult.points,
                    grid = raceResult.gridPos ?: 0,
                    qualified = raceResult.qualified,
                    finish = raceResult.finished,
                    status = raceResult.status,
                    fastestLap = raceResult.fastestLap?.let { mapFastestLap(it) }
                )
            }
            .sortedBy { it.finish }
    }

    private fun mapFastestLap(fastestLap: tmg.flashback.statistics.room.models.race.FastestLap?): FastestLap? {
        if (fastestLap == null) return null
        return FastestLap(
            rank = fastestLap.position,
            lapTime = fastestLap.time.toLapTime(),
        )
    }
}