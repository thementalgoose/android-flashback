package tmg.flashback.formula1.model

import tmg.flashback.formula1.model.RaceQualifyingType.*

data class Race(
    val raceInfo: RaceInfo,
    val qualifying: List<RaceQualifyingRound>,
    val sprint: List<RaceSprintResult>,
    val race: List<RaceRaceResult>,
    val schedule: List<Schedule>
) {

    val drivers: List<DriverConstructor>
    val constructors: List<Constructor>

    init {
        val driverSet: MutableSet<DriverConstructor> = mutableSetOf()
        val constructorSet: MutableSet<Constructor> = mutableSetOf()
        qualifying.forEach {
            it.results.forEach {
                constructorSet.add(it.driver.constructor)
                driverSet.add(it.driver)
            }
        }
        race.forEach {
            constructorSet.add(it.driver.constructor)
            driverSet.add(it.driver)
        }
        this.drivers = driverSet.toList()
        this.constructors = constructorSet.toList()
    }

    fun driverOverview(driverId: String): RaceDriverOverview? {
        val driver = drivers.firstOrNull { it.driver.id == driverId } ?: return null
        return RaceDriverOverview(
            driver = driver,
            q1 = qualifying.firstOrNull { it.label == Q1 }?.results?.firstOrNull { it.driver.driver.id == driverId },
            q2 = qualifying.firstOrNull { it.label == Q2 }?.results?.firstOrNull { it.driver.driver.id == driverId },
            q3 = qualifying.firstOrNull { it.label == Q3 }?.results?.firstOrNull { it.driver.driver.id == driverId },
            qSprint = sprint.firstOrNull { it.driver.driver.id == driverId },
            race = race.firstOrNull { it.driver.driver.id == driverId }
        )
    }

    val hasSprintQualifying: Boolean = sprint.isNotEmpty()

    fun has(raceQualifyingType: RaceQualifyingType): Boolean {
        return qualifying.any { it.label == raceQualifyingType}
    }

    val q1FastestLap: LapTime? by lazy {
        qualifying.firstOrNull { it.label == Q1 }
            ?.results
            ?.minByOrNull { it.lapTime?.totalMillis ?: Int.MAX_VALUE }
            ?.lapTime
    }
    val q2FastestLap: LapTime? by lazy {
        qualifying.firstOrNull { it.label == Q2 }
            ?.results
            ?.minByOrNull { it.lapTime?.totalMillis ?: Int.MAX_VALUE }
            ?.lapTime
    }
    val q3FastestLap: LapTime? by lazy {
        qualifying.firstOrNull { it.label == Q3 }
            ?.results
            ?.minByOrNull { it.lapTime?.totalMillis ?: Int.MAX_VALUE }
            ?.lapTime
    }

    val hasData: Boolean by lazy {
        qualifying.isNotEmpty() || race.isNotEmpty()
    }

    val constructorStandings: List<RaceConstructorStandings>
        get() {
            val standings: MutableMap<String, Double> = mutableMapOf()
            for (raceResult in race) {
                var previousPoints = standings.getOrPut(raceResult.driver.constructor.id) { 0.0 }
                previousPoints += raceResult.points
                standings[raceResult.driver.constructor.id] = previousPoints
            }
            if (hasSprintQualifying) {
                sprint.forEach {
                    var previousPoints = standings.getOrPut(it.driver.constructor.id) { 0.0 }
                    previousPoints += it.points
                    standings[it.driver.constructor.id] = previousPoints
                }
            }
            return constructors
                .map {
                    RaceConstructorStandings(
                        standings.getOrElse(
                            it.id
                        ) { 0.0 }, it
                    )
                }
                .sortedByDescending { it.points }
        }

    companion object
}




