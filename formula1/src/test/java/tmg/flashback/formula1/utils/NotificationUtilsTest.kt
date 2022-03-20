package tmg.flashback.formula1.utils

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import tmg.flashback.formula1.enums.RaceWeekend
import tmg.flashback.formula1.utils.NotificationUtils.getCategoryBasedOnLabel

internal class NotificationUtilsTest {


    @ParameterizedTest(name = "label of {0} returns channel {1}")
    @CsvSource(
        "Grand Prix,RACE",
        "raCe,RACE",
        "race,RACE",
        "sprinting,RACE",
        "sprinter,RACE",

        "quali,QUALIFYING",
        "sprint quali,QUALIFYING",
        "sprint qualifying,QUALIFYING",
        "quali,QUALIFYING",
        "qualifying,QUALIFYING",

        "fp,FREE_PRACTICE",
        "FP2,FREE_PRACTICE",
        "FP4,FREE_PRACTICE",
        "Fp1,FREE_PRACTICE",
        "free practice,FREE_PRACTICE",
        "practice,FREE_PRACTICE",

        "free,",
        "day,",
        "winter test day 3,",
        "unveil 3,",
    )
    fun `get category based on label returns expected`(label: String, expectedRaceWeekend: RaceWeekend?) {
        assertEquals(expectedRaceWeekend, getCategoryBasedOnLabel(label))
    }
}