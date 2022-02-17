package tmg.flashback.formula1.model

import tmg.flashback.formula1.enums.FormatQualifying
import tmg.flashback.formula1.enums.FormatRace
import tmg.flashback.formula1.enums.FormatSprint

fun RaceFormat.Companion.model(
    qualifying: FormatQualifying? = FormatQualifying.KNOCKOUT,
    sprint: FormatSprint? = null,
    race: FormatRace? = FormatRace.RACE
): RaceFormat = RaceFormat(
    qualifying = qualifying,
    sprint = sprint,
    race = race
)