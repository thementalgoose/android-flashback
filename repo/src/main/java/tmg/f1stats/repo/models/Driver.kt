package tmg.f1stats.repo.models

import java.time.LocalDate

data class Driver(
    val driverId: String,
    val wikiUrl: String,
    val photoUrl: String,
    val name: String,
    val surname: String,
    val dateOfBirth: LocalDate,
    val nationality: String
)