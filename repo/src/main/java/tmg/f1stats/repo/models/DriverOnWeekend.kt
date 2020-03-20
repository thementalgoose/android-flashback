package tmg.f1stats.repo.models

import org.threeten.bp.LocalDate

data class DriverOnWeekend(
    val driverId: String,
    val driverNumber: String,
    val driverCode: String,
    val wikiUrl: String,
    val name: String,
    val surname: String,
    val dateOfBirth: LocalDate,
    val nationality: String,
    val constructor: Constructor
) {
    val fullName: String
        get() = "$name $surname"
}