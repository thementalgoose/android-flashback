package tmg.flashback.firebase.converters

import tmg.flashback.firebase.base.ConverterUtils.fromDateRequired
import tmg.flashback.data.models.stats.Constructor
import tmg.flashback.data.models.stats.Driver
import tmg.flashback.firebase.models.FRound
import tmg.flashback.firebase.models.FSeasonOverviewDriver

fun FSeasonOverviewDriver.convert(races: Map<String, FRound>?, constructors: List<Constructor>): Driver {

    val constructorMap: Map<Int, Constructor> = (races ?: emptyMap())
        .map { (_, value) -> Pair(value.round, value.driverCon) }
        .mapNotNull { (round, driverCon) ->
            val constructorId = driverCon?.get(id)
            if (constructorId != null) {
                val constructor = constructors.firstOrNull { it.id == constructorId }
                if (constructor != null) {
                    return@mapNotNull round to constructor
                }
            }
            val defaultConstructorValue = constructors.firstOrNull { it.id == this.constructorId }
            if (defaultConstructorValue != null) {
                return@mapNotNull round to defaultConstructorValue
            }
            return@mapNotNull null
        }
        .toMap()

    val startingConstructor = constructors.firstOrNull { it.id == this.constructorId } ?: let {
        val potentialDebugInfo = races?.toList()?.firstOrNull()?.second
        throw NullPointerException("Converting driver, no constructor value for ${this.constructorId} in list of available constructors (${potentialDebugInfo?.season} / ${potentialDebugInfo?.round}")
    }

    return Driver(
        id = id,
        firstName = firstName,
        lastName = lastName,
        code = code,
        number = number ?: 0,
        wikiUrl = wikiUrl,
        photoUrl = photoUrl,
        dateOfBirth = fromDateRequired(dob),
        nationality = nationality,
        nationalityISO = nationalityISO,
        constructors = constructorMap,
        startingConstructor = startingConstructor
    )
}