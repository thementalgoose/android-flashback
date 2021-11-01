package tmg.flashback.statistics.repo.mappers.app

import androidx.core.graphics.toColorInt
import androidx.room.ColumnInfo
import androidx.room.PrimaryKey
import tmg.flashback.formula1.model.Constructor

class ConstructorMapper {

    fun mapConstructor(data: tmg.flashback.statistics.room.models.constructors.Constructor): Constructor {
        return Constructor(
            id = data.id,
            color = data.colour.toColorInt(),
            name = data.name,
            nationality = data.nationality,
            nationalityISO = data.nationalityISO,
            wikiUrl = data.wikiUrl,
        )
    }

}