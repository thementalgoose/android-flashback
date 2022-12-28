package tmg.flashback.formula1.model

data class Season(
    val season: Int,
    val races: List<Race>,
    val drivers: Set<Driver> = races.map { it.drivers.map { it.driver } }.flatten().toSet(),
    val constructors: Set<Constructor> = races.map { it.constructors }.flatten().toSet(),
    val event: List<Event>
) {
    val circuits: List<Circuit>
        get() = races
            .map { it.raceInfo.circuit }
            .toSet()
            .toList()

    val firstRace: Race?
        get() = races.minByOrNull { it.raceInfo.round }

    val lastRace: Race?
        get() = races.maxByOrNull { it.raceInfo.round }

    fun getConstructors(driver: Driver): List<Constructor> {
        return races
            .map { race -> race.race
                .filter { it.driver.driver.id == driver.id }
                .map { it.driver.constructor }
            }
            .flatten()
            .distinct()
    }

    companion object
}