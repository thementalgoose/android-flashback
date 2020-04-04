package tmg.f1stats.repo_firebase.converters

import tmg.f1stats.repo.models.Constructor
import tmg.f1stats.repo.models.Driver
import tmg.f1stats.repo.models.RoundDriver
import tmg.f1stats.repo_firebase.models.FSeasonOverviewConstructor
import tmg.f1stats.repo_firebase.models.FSeasonOverviewDriver

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

fun FSeasonOverviewDriver.convert(constructors: Map<String, FSeasonOverviewConstructor>): RoundDriver {
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
        constructor = constructors.values.first { it.id == constructorId }.convert()
    )
}