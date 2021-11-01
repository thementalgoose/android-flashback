package tmg.flashback.statistics.repo.mappers.network

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey
import tmg.flashback.statistics.network.models.constructors.ConstructorData
import tmg.flashback.statistics.network.models.drivers.DriverData
import tmg.flashback.statistics.room.models.constructors.Constructor
import tmg.flashback.statistics.room.models.drivers.Driver
import java.lang.RuntimeException
import kotlin.jvm.Throws

class NetworkConstructorMapper {

    @Throws(RuntimeException::class)
    fun mapConstructorData(constructorData: ConstructorData): Constructor {
        return Constructor(
            id = constructorData.id,
            colour = constructorData.colour,
            name = constructorData.name,
            nationality = constructorData.nationality,
            nationalityISO = constructorData.nationalityISO,
            wikiUrl = constructorData.wikiUrl
        )
    }
}