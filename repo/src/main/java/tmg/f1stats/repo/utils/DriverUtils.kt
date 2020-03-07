package tmg.f1stats.repo.utils

import tmg.f1stats.repo.models.Constructor
import tmg.f1stats.repo.models.Driver
import tmg.f1stats.repo.models.DriverOnWeekend

fun List<DriverOnWeekend>.findDriver(driverId: String): DriverOnWeekend? {
    return this.firstOrNull { it.driverId == driverId }
}

fun List<DriverOnWeekend>.fromConstructor(constructor: Constructor): List<DriverOnWeekend> {
    return this.fromConstructor(constructor.constructorId)
}

fun List<DriverOnWeekend>.fromConstructor(constructorId: String): List<DriverOnWeekend> {
    return this.filter {
        it.constructor.constructorId == constructorId
    }
}

fun Driver.toSeasonDriver(racingFor: Constructor): DriverOnWeekend {
    return DriverOnWeekend(
        driverId = driverId,
        driverNumber = driverNumber,
        driverCode = driverCode,
        wikiUrl = wikiUrl,
        name = name,
        surname = surname,
        dateOfBirth = dateOfBirth,
        nationality = nationality,
        constructor = racingFor
    )
}