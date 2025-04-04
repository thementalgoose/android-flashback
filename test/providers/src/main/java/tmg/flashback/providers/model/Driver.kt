package tmg.flashback.providers.model

import org.threeten.bp.LocalDate
import tmg.flashback.formula1.model.Driver

fun Driver.Companion.model(
    id: String = "driverId",
    firstName: String = "firstName",
    lastName: String = "lastName",
    code: String? = "code",
    number: Int? = 23,
    wikiUrl: String? = "wikiUrl",
    photoUrl: String? = "photoUrl",
    dateOfBirth: LocalDate = LocalDate.of(1995, 10, 12),
    nationality: String = "nationality",
    nationalityISO: String = "nationalityISO"
): Driver = Driver(
    id = id,
    firstName = firstName,
    lastName = lastName,
    code = code,
    number = number,
    wikiUrl = wikiUrl,
    photoUrl = photoUrl,
    dateOfBirth = dateOfBirth,
    nationality = nationality,
    nationalityISO = nationalityISO
)