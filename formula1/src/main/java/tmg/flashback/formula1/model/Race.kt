package tmg.flashback.formula1.model

data class Race(
    val raceInfo: RaceInfo,
    val q1: Map<String, RaceQualifyingResult_Legacy>,
    val q2: Map<String, RaceQualifyingResult_Legacy>,
    val q3: Map<String, RaceQualifyingResult_Legacy>,
    val qSprint: Map<String, RaceSprintQualifyingResult_Legacy>,

    // TODO: This is the field moving forward to use!
    val qualifying: List<RaceQualifyingRound>,

    val race: Map<String, RaceRaceResult>,
    val schedule: List<Schedule>
) {

    val drivers: List<DriverConstructor>
    val constructors: List<Constructor>

    init {
        val driverSet: MutableSet<DriverConstructor> = mutableSetOf()
        val constructorSet: MutableSet<Constructor> = mutableSetOf()
        q1.values.forEach {
            constructorSet.add(it.driver.constructor)
            driverSet.add(it.driver)
        }
        q2.values.forEach {
            constructorSet.add(it.driver.constructor)
            driverSet.add(it.driver)
        }
        q3.values.forEach {
            constructorSet.add(it.driver.constructor)
            driverSet.add(it.driver)
        }
        qSprint.values.forEach {
            constructorSet.add(it.driver.constructor)
            driverSet.add(it.driver)
        }
        race.values.forEach {
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
            q1 = q1[driverId],
            q2 = q2[driverId],
            q3 = q3[driverId],
            qSprint = qSprint[driverId],
            race = race[driverId]
        )
    }

    val hasSprintQualifying: Boolean
        get() = qSprint.isNotEmpty()

    val hasData: Boolean by lazy {
        q1.isNotEmpty() || q2.isNotEmpty() || q3.isNotEmpty() || qSprint.isNotEmpty() || race.isNotEmpty()
    }

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

    private fun Map<String, RaceQualifyingResult_Legacy>.fastest(): LapTime? = this
        .map { it.value.time }
        .filter { it != null && !it.noTime && it.totalMillis != 0 }
        .minByOrNull {
            it?.totalMillis ?: Int.MAX_VALUE
        }

    companion object
}




