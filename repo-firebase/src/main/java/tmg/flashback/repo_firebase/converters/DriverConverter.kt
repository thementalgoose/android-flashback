package tmg.flashback.repo_firebase.converters

import tmg.flashback.repo.models.Driver
import tmg.flashback.repo.models.RoundDriver
import tmg.flashback.repo_firebase.models.FSeasonOverviewConstructor
import tmg.flashback.repo_firebase.models.FSeasonOverviewDriver

fun FSeasonOverviewDriver.convert(): Driver {
    return Driver(
        id = id,
        firstName = firstName,
        lastName = lastName,
        code = code,
        number = number ?: 0,
        wikiUrl = wikiUrl,
        photoUrl = photoUrl,
        dateOfBirth = fromDate(dob),
        nationality = nationality,
        nationalityISO = nationalityISO
    )
}

fun FSeasonOverviewDriver.convert(constructors: Map<String, FSeasonOverviewConstructor>, overrideConstructorId: String?): RoundDriver {
    return RoundDriver(
        id = id,
        firstName = firstName,
        lastName = lastName,
        code = code,
        number = number ?: 0,
        wikiUrl = wikiUrl,
        photoUrl = photoUrl,
        dateOfBirth = fromDate(dob),
        nationality = nationality,
        nationalityISO = nationalityISO,
        constructor = constructors.values.first { it.id == overrideConstructorId ?: constructorId }.convert()
    )
}