package tmg.flashback.data.models.stats

data class Season(
        val season: Int,
        val drivers: List<Driver>,
        val constructors: List<Constructor>,
        val rounds: List<Round>,
        val driverStandings: DriverStandings,
        val constructorStandings: ConstructorStandings
) {
    val circuits: List<CircuitSummary>
        get() = rounds.map { it.circuit }

    val firstRound: Round?
        get() = rounds.minByOrNull { it.round }

    val lastRound: Round?
        get() = rounds.maxByOrNull { it.round }
}

/**
 * Get the constructor standings for the season
 */
fun Season.constructorStandings(): ConstructorStandingsRound = this.constructors
        .map { constructor ->
            // Driver map should contain driver id -> Driver, points for each constructor
            val driverMap = mutableMapOf<String, Pair<Driver, Double>>()
            this.rounds.forEach { round ->
                val driversInRoundForConstructor = round.drivers
                        .filter { roundDriver -> roundDriver.constructors.get(round.round)?.id == constructor.id }
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
fun List<Round>.driverStandings(): DriverStandingsRound {
    val returnMap: MutableMap<String, Pair<Driver, Double>> = mutableMapOf()
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
fun List<Round>.bestQualified(driverId: String): Int? {
    val round = this.minByOrNull { it.race[driverId]?.qualified ?: Int.MAX_VALUE }
    return round?.race?.get(driverId)?.qualified
}

fun List<Round>.bestQualifyingResultFor(driverId: String): Pair<Int, List<Round>>? {
    val bestQualifyingPosition: Int = this.bestQualified(driverId) ?: return null
    val listOfCircuits = this
            .filter { it.race[driverId]?.qualified == bestQualifyingPosition }
    return Pair(bestQualifyingPosition, listOfCircuits)
}

/**
 * Get the best finishing position for a given driver
 */
fun List<Round>.bestFinish(driverId: String): Int? {
    val round = this.minByOrNull { it.race[driverId]?.finish ?: Int.MAX_VALUE }
    return round?.race?.get(driverId)?.finish
}

fun List<Round>.bestRaceResultFor(driverId: String): Pair<Int, List<Round>>? {
    val bestQualifyingPosition: Int = this.bestFinish(driverId) ?: return null
    val listOfCircuits = this
            .filter { it.race[driverId]?.finish == bestQualifyingPosition }
    return Pair(bestQualifyingPosition, listOfCircuits)
}