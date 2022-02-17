package tmg.flashback.statistics.repo.mappers.app

import tmg.flashback.formula1.enums.FormatQualifying
import tmg.flashback.formula1.enums.FormatRace
import tmg.flashback.formula1.enums.FormatSprint
import tmg.flashback.formula1.model.RaceFormat
import tmg.flashback.statistics.room.models.race.RaceInfo
import tmg.utilities.extensions.toEnum
import java.lang.RuntimeException
import kotlin.jvm.Throws

class RaceFormatMapper {

    @Throws(RuntimeException::class)
    fun mapRaceFormat(model: RaceInfo?): RaceFormat {
        return RaceFormat(
            qualifying = model?.formatQualifying?.toEnum<FormatQualifying> { it.key },
            sprint = model?.formatSprint?.toEnum<FormatSprint> { it.key },
            race = model?.formatRace?.toEnum<FormatRace> { it.key }
        )
    }
}