package tmg.flashback.formula1.model

data class Race(
    val raceInfo: RaceInfo,
    val q1: Map<String, RaceQualifyingResult>,
    val q2: Map<String, RaceQualifyingResult>,
    val q3: Map<String, RaceQualifyingResult>,
    val qSprint: Map<String, RaceSprintQualifyingResult>,
    val race: Map<String, RaceRaceResult>
) {

    val drivers: List<Driver>
    val constructors: List<Constructor>

    init {
        val driverSet: MutableSet<Driver> = mutableSetOf()
        val constructorSet: MutableSet<Constructor> = mutableSetOf()
        q1.values.forEach {
            constructorSet.add(it.driver.constructor)
            driverSet.add(it.driver.driver)
        }
        q2.values.forEach {
            constructorSet.add(it.driver.constructor)
            driverSet.add(it.driver.driver)
        }
        q3.values.forEach {
            constructorSet.add(it.driver.constructor)
            driverSet.add(it.driver.driver)
        }
        qSprint.values.forEach {
            constructorSet.add(it.driver.constructor)
            driverSet.add(it.driver.driver)
        }
        race.values.forEach {
            constructorSet.add(it.driver.constructor)
            driverSet.add(it.driver.driver)
        }
        this.drivers = driverSet.toList()
        this.constructors = constructorSet.toList()
    }

    fun driverOverview(driverId: String): RaceDriverOverview {
        return RaceDriverOverview(
            q1 = q1[driverId],
            q2 = q2[driverId],
            q3 = q3[driverId],
            qSprint = qSprint[driverId],
            race = race[driverId]
        )
    }

    val hasSprintQualifying: Boolean
        get() = qSprint.isNotEmpty()

    val q1FastestLap: LapTime?
        get() = q1.fastest()
    val q2FastestLap: LapTime?
        get() = q2.fastest()
    val q3FastestLap: LapTime?
        get() = q3.fastest()

    val constructorStandings: List<RaceConstructorStandings>
        get() {
            val standings: MutableMap<String, Double> = mutableMapOf()
            for ((_, raceResult) in race) {
                var previousPoints = standings.getOrPut(raceResult.driver.constructor.id) { 0.0 }
                previousPoints += raceResult.points
                standings[raceResult.driver.constructor.id] = previousPoints
            }
            return constructors.map {
                RaceConstructorStandings(
                    standings.getOrElse(
                        it.id
                    ) { 0.0 }, it
                )
            }
        }

    private fun Map<String, RaceQualifyingResult>.fastest(): LapTime? = this
        .map { it.value.time }
        .filter { it != null && !it.noTime && it.totalMillis != 0 }
        .minByOrNull {
            it?.totalMillis ?: Int.MAX_VALUE
        }
}


/**
 * Get the maximum points that a team has scored in the season
 * (ie. Points that the constructors champion has scored)
 */
fun Map<String, Triple<Constructor, Map<String, Pair<ConstructorDriver, Double>>, Double>>.maxConstructorPointsInSeason(): Double {
    return this.values.maxByOrNull { it.third }?.third ?: 0.0
}

/**
 * Get all the points that drivers in a constructor has achieved
 */
fun Map<String, Pair<DriverWithEmbeddedConstructor, Int>>.allPoints(): Int =
    this.map { it.value.second }.sum()






