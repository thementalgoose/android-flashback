package tmg.flashback.firebase.mappers.seasonoverview

import tmg.flashback.firebase.models.FSeason
import tmg.flashback.formula1.model.Constructor
import tmg.utilities.utils.LocalDateUtils.Companion.requireFromDate

class SeasonOverviewDriverMapper(
    private val constructorMapper: SeasonOverviewConstructorMapper
) {

    /**
     * Map a driver object
     * @param input The entire season in the database
     * @param driverId the driver to map
     */
    fun mapDriver(input: FSeason, driverId: String): tmg.flashback.formula1.model.DriverWithEmbeddedConstructor? {
        val driver = input.drivers?.get(driverId) ?: return null
        val constructors = (input.constructors?.values?.toList() ?: emptyList()).map { constructorMapper.mapConstructor(it) }

        val constructorMap: Map<Int, Constructor> = (input.race ?: emptyMap())
            .map { (_, value) -> Pair(value.round, value.driverCon) }
            .mapNotNull { (round, driverCon) ->
                val constructorId = driverCon?.get(driverId)
                if (constructorId != null) {
                    val constructor = constructors.firstOrNull { it.id == constructorId }
                    if (constructor != null) {
                        return@mapNotNull round to constructor
                    }
                }
                // Don't assign the default if the map is not found, as it means the driver didn't race!
//                val defaultConstructorValue = constructors.firstOrNull { it.id == startingConstructorId }
//                if (defaultConstructorValue != null) {
//                    return@mapNotNull round to defaultConstructorValue
//                }
                return@mapNotNull null
            }
            .toMap()

        var startingConstructor = constructors.firstOrNull { it.id == driver.constructorId }
        if (startingConstructor == null) {
            // Attempt to find fallback
            if (constructorMap.isNotEmpty()) {
                startingConstructor = constructorMap.toList().firstOrNull()?.second
            }
        }
        if (startingConstructor == null) {
            val potentialDebugInfo = input.race?.toList()?.firstOrNull()?.second
            throw NullPointerException("Converting driver, no constructor value for ${driver.constructorId} in list of available constructors (${potentialDebugInfo?.season} / ${potentialDebugInfo?.round}")
        }

        return tmg.flashback.formula1.model.DriverWithEmbeddedConstructor(
            id = driver.id,
            firstName = driver.firstName,
            lastName = driver.lastName,
            code = driver.code,
            number = driver.number ?: 0,
            wikiUrl = driver.wikiUrl,
            photoUrl = driver.photoUrl,
            dateOfBirth = requireFromDate(driver.dob),
            nationality = driver.nationality,
            nationalityISO = driver.nationalityISO,
            constructors = constructorMap,
            startingConstructor = startingConstructor
        )
    }
}