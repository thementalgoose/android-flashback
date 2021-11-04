package tmg.flashback.statistics.repo.mappers.network

import java.lang.RuntimeException
import kotlin.jvm.Throws

class NetworkDriverDataMapper {

    @Throws(RuntimeException::class)
    fun mapDriverData(driver: tmg.flashback.statistics.network.models.drivers.Driver): Driver {
        return Driver(
            id = driver.id,
            firstName = driver.firstName,
            lastName = driver.lastName,
            dob = driver.dob,
            nationality = driver.nationality,
            nationalityISO = driver.nationalityISO,
            photoUrl = driver.photoUrl,
            wikiUrl = driver.wikiUrl,
            number = driver.permanentNumber?.toIntOrNull(),
            code = driver.code
        )
    }
}