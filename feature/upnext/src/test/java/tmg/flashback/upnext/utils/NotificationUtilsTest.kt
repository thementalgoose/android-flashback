package tmg.flashback.upnext.utils

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import tmg.flashback.upnext.model.NotificationChannel
import tmg.flashback.upnext.utils.NotificationUtils.getCategoryBasedOnLabel
import tmg.flashback.upnext.utils.NotificationUtils.getRequestCode

internal class NotificationUtilsTest {

    @ParameterizedTest(name = "get request code for season {0} round {1} value {2} returns {3}")
    @CsvSource(
        "2012,10,1,20121001",
        "2021,14,5,20211405"
    )
    fun `get request code for season round and value returns unique request code`(season: Int, round: Int, value: Int, expected: Int) {
        assertEquals(expected, getRequestCode(season, round, value))
    }

    @ParameterizedTest(name = "label of {0} returns channel {1}")
    @CsvSource(
        "Grand Prix,RACE",
        "raCe,RACE",
        "race,RACE",

        "quali,QUALIFYING",
        "sprint quali,QUALIFYING",
        "sprinting,QUALIFYING",
        "sprint qualifying,QUALIFYING",
        "quali,QUALIFYING",
        "qualifying,QUALIFYING",
        "sprinter,QUALIFYING",

        "fp,FREE_PRACTICE",
        "FP2,FREE_PRACTICE",
        "FP4,FREE_PRACTICE",
        "Fp1,FREE_PRACTICE",
        "free practice,FREE_PRACTICE",
        "practice,FREE_PRACTICE",

        "free,SEASON_INFO",
        "day,SEASON_INFO",
        "winter test day 3,SEASON_INFO",
        "unveil 3,SEASON_INFO",
    )
    fun `get category based on label returns expected`(label: String, expectedNotificationChannel: NotificationChannel) {
        assertEquals(expectedNotificationChannel, getCategoryBasedOnLabel(label))
    }
}