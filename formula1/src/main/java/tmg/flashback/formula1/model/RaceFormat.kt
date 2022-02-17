package tmg.flashback.formula1.model

import tmg.flashback.formula1.enums.FormatQualifying
import tmg.flashback.formula1.enums.FormatRace
import tmg.flashback.formula1.enums.FormatSprint

data class RaceFormat(
    val qualifying: FormatQualifying?,
    val sprint: FormatSprint?,
    val race: FormatRace?
) {
    companion object
}