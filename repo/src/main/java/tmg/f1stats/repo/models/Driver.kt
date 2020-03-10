package tmg.f1stats.repo.models

import org.threeten.bp.LocalDate

open class Driver(
    val driverId: String,
    val driverNumber: String,
    val driverCode: String,
    val wikiUrl: String,
    val name: String,
    val surname: String,
    val dateOfBirth: LocalDate,
    val nationality: String
)