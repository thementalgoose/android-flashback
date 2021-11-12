package tmg.flashback.statistics.repo.mappers.app

import tmg.flashback.formula1.model.*
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
            wikipediaUrl = data.raceInfo.wikiUrl,
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
            wikipediaUrl = data.raceInfo.wikiUrl,
            circuit = circuitMapper.mapCircuit(data.circuit)!!,
        )
    }

    fun mapRace(data: tmg.flashback.statistics.room.models.race.Race?): Race? {
        if (data == null) return null
        return Race(
            raceInfo = mapRaceInfo(data),
            q1 = mapQualifying(
                fieldToBaseFilteringOn = Qualifying.Q1,
                input = data.qualifying
            ),
            q2 = mapQualifying(
                fieldToBaseFilteringOn = Qualifying.Q2,
                input = data.qualifying
            ),
            q3 = mapQualifying(
                fieldToBaseFilteringOn = Qualifying.Q3,
                input = data.qualifying
            ),
            qSprint = mapSprintQualifying(
                input = data.qualifying
            ),
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
                .filter { it.qualifyingResult.q1 != null }
                .sortedBy { it.qualifyingResult.q1!!.toLapTime().totalMillis }
                .mapIndexed { index, item -> Pair(item.driver.id, index + 1) }
            println(lapTimeOrder)
            val driverListForRound = input.map {
                RaceQualifyingRoundDriver.Qualifying(
                    driver = DriverConstructor(driverDataMapper.mapDriver(it.driver), constructorDataMapper.mapConstructorData(it.constructor)),
                    lapTime = it.qualifyingResult.q1?.toLapTime(),
                    position = lapTimeOrder.firstOrNull { (id, _) -> id == it.driver.id }?.second
                        ?: it.qualifyingResult.qualified
                        ?: -1
                )
            }
            qualifyingData.add(RaceQualifyingRound("Q1", 1,driverListForRound.sortedBy { it.position }))
        }
        // Q2
        if (input.any { it.qualifyingResult.q2 != null }) {
            val lapTimeOrder = input
                .filter { it.qualifyingResult.q2 != null }
                .sortedBy { it.qualifyingResult.q2!!.toLapTime().totalMillis }
                .mapIndexed { index, item -> Pair(item.driver.id, index + 1) }
            val driverListForRound = input.map {
                RaceQualifyingRoundDriver.Qualifying(
                    driver = DriverConstructor(driverDataMapper.mapDriver(it.driver), constructorDataMapper.mapConstructorData(it.constructor)),
                    lapTime = it.qualifyingResult.q2?.toLapTime(),
                    position = lapTimeOrder.firstOrNull { (id, _) -> id == it.driver.id }?.second
                        ?: qualifyingData.firstOrNull { it.order == 1 }?.results?.firstOrNull { model -> model.driver.driver.id == it.driver.id }?.position
                        ?: it.qualifyingResult.qualified
                        ?: -1
                )
            }
            qualifyingData.add(RaceQualifyingRound("Q2", 2, driverListForRound.sortedBy { it.position }))
        }
        // Q3
        if (input.any { it.qualifyingResult.q3 != null }) {
            val lapTimeOrder = input
                .filter { it.qualifyingResult.q3 != null }
                .sortedBy { it.qualifyingResult.q3!!.toLapTime().totalMillis }
                .mapIndexed { index, item -> Pair(item.driver.id, index + 1) }
            val driverListForRound = input.map {
                RaceQualifyingRoundDriver.Qualifying(
                    driver = DriverConstructor(driverDataMapper.mapDriver(it.driver), constructorDataMapper.mapConstructorData(it.constructor)),
                    lapTime = it.qualifyingResult.q3?.toLapTime(),
                    position = lapTimeOrder.firstOrNull { (id, _) -> id == it.driver.id }?.second
                        ?: qualifyingData.firstOrNull { it.order == 2 }?.results?.firstOrNull { model -> model.driver.driver.id == it.driver.id }?.position
                        ?: qualifyingData.firstOrNull { it.order == 1 }?.results?.firstOrNull { model -> model.driver.driver.id == it.driver.id }?.position
                        ?: it.qualifyingResult.qualified
                        ?: -1
                )
            }
            qualifyingData.add(RaceQualifyingRound("Q3", 3, driverListForRound.sortedBy { it.position }))
        }

        // Sprint Qualifying
        if (input.any { it.qualifyingResult.qSprint != null }) {
            val driverListForRound = input
                .sortedBy { it.qualifyingResult.qSprint?.finished }
                .map {
                    RaceQualifyingRoundDriver.SprintQualifying(
                        driver = DriverConstructor(driverDataMapper.mapDriver(it.driver), constructorDataMapper.mapConstructorData(it.constructor)),
                        lapTime = it.qualifyingResult.qSprint?.time?.toLapTime(),
                        position = it.qualifyingResult.qSprint?.finished ?: -1,
                        finished = it.qualifyingResult.qSprint?.finished ?: -1,
                        gridPos = it.qualifyingResult.qSprint?.gridPos ?: -1,
                        points = it.qualifyingResult.qSprint?.points ?: 0.0,
                        status = it.qualifyingResult.qSprint?.status ?: "Unknown"
                    )
                }
            qualifyingData.add(RaceQualifyingRound("Sprint", 4, driverListForRound.sortedBy { it.position }))
        }

        return qualifyingData
    }

    /**
     * Map qualifying data
     *
     * @param fieldToBaseFilteringOn Q1, Q2 or Q3
     * @param input Map of driver id -> qualifying result (pos,q1,q2,q3)
     */
    private fun mapQualifying(fieldToBaseFilteringOn: Qualifying, input: List<QualifyingDriverResult>): Map<String, RaceQualifyingResult_Legacy> {
        val allDrivers = input.map {
            DriverConstructor(
                driver = driverDataMapper.mapDriver(it.driver),
                constructor = constructorDataMapper.mapConstructorData(it.constructor)
            )
        }
        return input
            .map { qualifyingResult ->
                return@map allDrivers
                    .first { it.driver.id == qualifyingResult.driver.id }
                    .let { Pair(it, qualifyingResult) }
            }
            .map { (driver, qualifyingResult) ->
                Triple(driver, qualifyingResult, when (fieldToBaseFilteringOn) {
                    Qualifying.Q1 -> qualifyingResult.qualifyingResult.q1?.toLapTime()
                    Qualifying.Q2 -> qualifyingResult.qualifyingResult.q2?.toLapTime()
                    Qualifying.Q3 -> qualifyingResult.qualifyingResult.q3?.toLapTime()
                })
            }
            .sortedBy { (_, _, lapTime) ->
                if (lapTime?.totalMillis == null || lapTime.totalMillis == 0) {
                    return@sortedBy Int.MAX_VALUE
                }
                return@sortedBy lapTime.totalMillis
            }
            .mapIndexed { index, (driver, qualifyingResult, lapTime) ->
                driver.driver.id to RaceQualifyingResult_Legacy(
                    driver = driver,
                    time = lapTime,
                    position = (if (lapTime == null) qualifyingResult.qualifyingResult.qualified else null) ?: (index + 1)
                )
            }
            .toMap()
    }



    /**
     * Map sprint data
     *
     * @param input Map of driver id -> sprint quali result
     */
    fun mapSprintQualifying(input: List<QualifyingDriverResult>?): Map<String, RaceSprintQualifyingResult_Legacy> {
        if (input == null || input.isEmpty()) return emptyMap()
        val allDrivers = input.map {
            DriverConstructor(
                driver = driverDataMapper.mapDriver(it.driver),
                constructor = constructorDataMapper.mapConstructorData(it.constructor)
            )
        }
        return input
            .mapNotNull { qualifying ->
                if (qualifying.qualifyingResult.qSprint == null) return@mapNotNull null
                return@mapNotNull allDrivers
                    .first { it.driver.id == qualifying.driver.id }
                    .let { Triple(it, qualifying.qualifyingResult.qualified, qualifying.qualifyingResult.qSprint!!) }
            }
            .map { (driver, qualified, sprintQualifying) ->
                driver.driver.id to RaceSprintQualifyingResult_Legacy(
                    driver = driver,
                    time = sprintQualifying.time?.toLapTime(),
                    points = sprintQualifying.points,
                    grid = sprintQualifying.gridPos ?: 0,
                    qualified = qualified,
                    finish = sprintQualifying.finished,
                    status = sprintQualifying.status
                )
            }
            .toMap()
    }

    /**
     * Map sprint data
     *
     * @param input Map of driver id -> sprint quali result
     */
    private fun mapRace(input: List<RaceDriverResult>?): Map<String, RaceRaceResult> {
        if (input == null || input.isEmpty()) return emptyMap()
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
                driver.driver.id to RaceRaceResult(
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
            .toMap()
    }

    private fun mapFastestLap(fastestLap: tmg.flashback.statistics.room.models.race.FastestLap?): FastestLap? {
        if (fastestLap == null) return null
        return FastestLap(
            rank = fastestLap.position,
            lapTime = fastestLap.time.toLapTime(),
        )
    }
}