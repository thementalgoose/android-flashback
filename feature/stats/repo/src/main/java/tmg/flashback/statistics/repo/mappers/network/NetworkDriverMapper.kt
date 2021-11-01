package tmg.flashback.statistics.repo.mappers.network

import tmg.flashback.statistics.network.models.drivers.DriverData
import tmg.flashback.statistics.room.models.drivers.Driver
import java.lang.RuntimeException
import kotlin.jvm.Throws

class NetworkDriverMapper {

    @Throws(RuntimeException::class)
    fun mapDriverData(driverData: DriverData): Driver {
        return Driver(
            id = driverData.id,
            firstName = driverData.firstName,
            lastName = driverData.lastName,
            dob = driverData.dob,
            nationality = driverData.nationality,
            nationalityISO = driverData.nationalityISO,
            photoUrl = driverData.photoUrl,
            wikiUrl = driverData.wikiUrl,
            number = driverData.permanentNumber?.toIntOrNull(),
            code = driverData.code
        )
    }
}