package tmg.flashback.formula1.model

data class Season(
    val season: Int,
    val driverWithEmbeddedConstructors: List<DriverWithEmbeddedConstructor>,
    val constructors: List<Constructor>,
    val races: List<Race>,
    val driverStandings: DriverStandings,
    val constructorStandings: ConstructorStandings
) {
    val circuits: List<Circuit>
        get() = races.map { it.circuit }

    val firstRace: Race?
        get() = races.minByOrNull { it.round }

    val lastRace: Race?
        get() = races.maxByOrNull { it.round }
}

/**
 * Get the constructor standings for the season
 */
@Deprecated("Query the DAO directly", replaceWith = "SeasonStandingsDao.getConstructorStandings()")
fun Season.constructorStandings(): ConstructorStandingsRound = this.constructors
        .map { constructor ->
            // Driver map should contain driver id -> Driver, points for each constructor
            val driverMap = mutableMapOf<String, Pair<ConstructorDriver, Double>>()
            this.races.forEach { round ->
                val driversInRoundForConstructor = round.drivers
                        .filter { roundDriver -> roundDriver.constructor.id == constructor.id }
                        .map { driver ->
                            val points = (round.race[driver.id]?.points ?: 0.0) + (round.qSprint[driver.id]?.points ?: 0.0)
                            driver to points
                        }

                driversInRoundForConstructor.forEach { (roundDriver, points) ->
                    if (!driverMap.containsKey(roundDriver.id)) {
                        driverMap[roundDriver.id] = Pair(roundDriver, 0.0)
                    }
                    val existingValue = driverMap.getValue(roundDriver.id)

                    driverMap[roundDriver.id] = Pair(roundDriver, existingValue.second + points)
                }
            }

            // Constructor points
            val constructorPoints = this.constructorStandings.firstOrNull { it.item.id == constructor.id }
                    ?.let { (_, points) -> points }
                    ?: driverMap.map { it.value.second }.sum()

            constructor.id to Triple(constructor, driverMap, constructorPoints)
        }
        .toMap()

/**
 * Get the driver standings for the season
 */
// TODO: Re-implement this method to include the driver standings data to include penalties etc.
//@Deprecated("This method should be depended on, use the standings object inside the season model for a more accurate reflection")
@Deprecated("Query the DAO directly", replaceWith = "SeasonStandingsDao.getConstructorStandings()")
fun List<Race>.driverStandings(): DriverStandingsRound {
    val returnMap: MutableMap<String, Pair<ConstructorDriver, Double>> = mutableMapOf()

    this.forEach { round ->
        round.drivers.forEach {
            if (!returnMap.containsKey(it.id)) {
                returnMap[it.id] = Pair(it, 0.0)
            }

            val (driver, points) = returnMap[it.id]!!
            returnMap[it.id] = Pair(driver, points + (round.race[it.id]?.points ?: 0.0))
        }
    }

    return returnMap
}

/**
 * Get the best qualifying position for a given driver
 */
fun List<Race>.bestQualified(driverId: String): Int? {
    val round = this.minByOrNull { it.race[driverId]?.qualified ?: Int.MAX_VALUE }
    return round?.race?.get(driverId)?.qualified
}

fun List<Race>.bestQualifyingResultFor(driverId: String): Pair<Int, List<Race>>? {
    val bestQualifyingPosition: Int = this.bestQualified(driverId) ?: return null
    val listOfCircuits = this
            .filter { it.race[driverId]?.qualified == bestQualifyingPosition }
    return Pair(bestQualifyingPosition, listOfCircuits)
}

/**
 * Get the best finishing position for a given driver
 */
fun List<Race>.bestFinish(driverId: String): Int? {
    val round = this.minByOrNull { it.race[driverId]?.finish ?: Int.MAX_VALUE }
    return round?.race?.get(driverId)?.finish
}

fun List<Race>.bestRaceResultFor(driverId: String): Pair<Int, List<Race>>? {
    val bestQualifyingPosition: Int = this.bestFinish(driverId) ?: return null
    val listOfCircuits = this
            .filter { it.race[driverId]?.finish == bestQualifyingPosition }
    return Pair(bestQualifyingPosition, listOfCircuits)
}