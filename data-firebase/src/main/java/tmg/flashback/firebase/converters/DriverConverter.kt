package tmg.flashback.firebase.converters

import tmg.flashback.firebase.base.ConverterUtils.fromDateRequired
import tmg.flashback.data.models.stats.Constructor
import tmg.flashback.data.models.stats.Driver
import tmg.flashback.data.models.stats.RoundDriver
import tmg.flashback.firebase.models.FSeasonOverviewConstructor
import tmg.flashback.firebase.models.FSeasonOverviewDriver

fun FSeasonOverviewDriver.convert(constructorAtEndOfSeason: Constructor): Driver {
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
        constructorAtEndOfSeason = constructorAtEndOfSeason
    )
}

fun FSeasonOverviewDriver.convert(constructors: Map<String, FSeasonOverviewConstructor>, overrideConstructorId: String?, constructorAtEndOfSeason: Constructor): RoundDriver {
    return RoundDriver(
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
        constructor = constructors.values.first { it.id == overrideConstructorId ?: constructorId }
            .convert(),
        constructorAtEndOfSeason = constructorAtEndOfSeason
    )
}