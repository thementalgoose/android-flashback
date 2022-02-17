package tmg.flashback.statistics.repo.mappers.app

import tmg.flashback.formula1.model.*
import tmg.flashback.formula1.model.RaceQualifyingType.*
import tmg.flashback.formula1.utils.toLapTime
import tmg.flashback.statistics.room.models.race.QualifyingDriverResult
import tmg.flashback.statistics.room.models.race.RaceDriverResult
import tmg.flashback.statistics.room.models.race.RaceInfoWithCircuit
import tmg.utilities.utils.LocalDateUtils.Companion.requireFromDate
import tmg.utilities.utils.LocalTimeUtils.Companion.fromTime

class RaceMapper(
    private val circuitMapper: CircuitMapper,
    private val driverDataMapper: DriverDataMapper,
    private val constructorDataMapper: ConstructorDataMapper,
    private val raceFormatMapper: RaceFormatMapper,
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
            format = raceFormatMapper.mapRaceFormat(data.raceInfo),
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
            format = raceFormatMapper.mapRaceFormat(data.raceInfo),
            wikipediaUrl = data.raceInfo.wikiUrl,
            youtube = data.raceInfo.youtube,
            circuit = circuitMapper.mapCircuit(data.circuit)!!,
        )
    }

    fun mapRace(data: tmg.flashback.statistics.room.models.race.Race?): Race? {
        if (data == null) return null
        return Race(
            raceInfo = mapRaceInfo(data),
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
                RaceQualifyingRoundDriver.Qualifying(
                    _driver = DriverConstructor(driverDataMapper.mapDriver(it.driver), constructorDataMapper.mapConstructorData(it.constructor)),
                    _lapTime = it.qualifyingResult.q1?.toLapTime(),
                    _position = lapTimeOrder.firstOrNull { (id, _) -> id == it.driver.id }?.second
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
                RaceQualifyingRoundDriver.Qualifying(
                    _driver = DriverConstructor(driverDataMapper.mapDriver(it.driver), constructorDataMapper.mapConstructorData(it.constructor)),
                    _lapTime = it.qualifyingResult.q2?.toLapTime(),
                    _position = lapTimeOrder.firstOrNull { (id, _) -> id == it.driver.id }?.second
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
                RaceQualifyingRoundDriver.Qualifying(
                    _driver = DriverConstructor(driverDataMapper.mapDriver(it.driver), constructorDataMapper.mapConstructorData(it.constructor)),
                    _lapTime = it.qualifyingResult.q3?.toLapTime(),
                    _position = lapTimeOrder.firstOrNull { (id, _) -> id == it.driver.id }?.second
                        ?: qualifyingData.firstOrNull { it.order == 2 }?.results?.firstOrNull { model -> model.driver.driver.id == it.driver.id }?.position
                        ?: qualifyingData.firstOrNull { it.order == 1 }?.results?.firstOrNull { model -> model.driver.driver.id == it.driver.id }?.position
                        ?: it.qualifyingResult.qualified
                        ?: -1
                )
            }
            qualifyingData.add(RaceQualifyingRound(Q3, 3, driverListForRound.sortedBy { it.position }))
        }

        // Sprint Qualifying
        if (input.any { it.qualifyingResult.qSprint != null }) {
            val driverListForRound = input
                .sortedBy { it.qualifyingResult.qSprint?.finished }
                .map {
                    RaceQualifyingRoundDriver.SprintQualifying(
                        _driver = DriverConstructor(driverDataMapper.mapDriver(it.driver), constructorDataMapper.mapConstructorData(it.constructor)),
                        _lapTime = it.qualifyingResult.qSprint?.time?.toLapTime(),
                        _position = it.qualifyingResult.qSprint?.finished ?: -1,
                        finished = it.qualifyingResult.qSprint?.finished ?: -1,
                        gridPos = it.qualifyingResult.qSprint?.gridPos ?: -1,
                        points = it.qualifyingResult.qSprint?.points ?: 0.0,
                        status = it.qualifyingResult.qSprint?.status ?: "Unknown"
                    )
                }
            qualifyingData.add(RaceQualifyingRound(SPRINT, 4, driverListForRound.sortedBy { it.position }))
        }

        return qualifyingData
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