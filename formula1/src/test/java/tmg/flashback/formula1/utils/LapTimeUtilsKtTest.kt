package tmg.flashback.formula1.utils

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import tmg.flashback.formula1.model.LapTime

class LapTimeUtilsKtTest {


    @ParameterizedTest(name = "Converting {0} to a standard format results in {1}")
    @CsvSource(
        "1.123,00:00:01.123",
        "-0.123,00:00:00.123",
        "3:20.183,00:03:20.183",
        "1:10:31.103,01:10:31.103",
        "+1:123,null",
        "invalid,null",
        ",null"
    )
    fun `converting a supported string to a local time is done correctly`(time: String?, expected: String?) {

        assertEquals(expected, time?.toLocalTime().toString())
    }

    @ParameterizedTest(name = "Total millis behind \"{0}\" is {1}")
    @CsvSource(
        "1.123,1123",
        "-0.123,123",
        "3:20.183,200183",
        "1:10:31.103,4231103",
        "+1:123,0",
        "invalid,0",
        ",0"
    )
    fun `converting a string to a lap time object is done correctly`(time: String?, expected: Int?) {

        assertEquals(expected, time?.toLapTime()?.totalMillis ?: 0)
    }

    @ParameterizedTest(name = "Taking {0} millis and adding millis delta of {1} results in {2} laptime")
    @CsvSource(
        "91274,4373,1:35.647",
        "187837,19489,3:27.326"
    )
    fun `adding millis to lap time results in correct lap time`(source: Int, delta: Int, expected: String) {

        val lapTime: LapTime = LapTime(source)
        val lapTimeWithData: LapTime = lapTime.add(delta)

        assertEquals(expected, lapTimeWithData.toString())
    }

    @ParameterizedTest(name = "Taking {0} millis and adding formatted delta of {1} results in {2} laptime")
    @CsvSource(
        "38122,+1.342,39.464",
        "438122,+23:01.923,30:20.045"
    )
    fun `adding delta string to lap time results in correct lap time`(source: Int, delta: String, expected: String) {

        val lapTime: LapTime = LapTime(source)
        val lapTimeWithDelta: LapTime = lapTime.addDelta(delta)

        assertEquals(expected, lapTimeWithDelta.toString())
    }

    @ParameterizedTest(name = "Taking {0} millis and adding {1}h {2}m {3}s {4}ms results in {6} ({5} millis)")
    @CsvSource(
        "5788,0,1,2,3,67791,1:07.791",
        "323294,3,23,42,232,12545526,3:29:05.526"
    )
    fun `adding delta hours mins seconds millis to lap time results in correct lap time`(source: Int, hour: Int, min: Int, sec: Int, millis: Int, expectedMillis: Int, expectedString: String) {

        val lapTime: LapTime = LapTime(source)
        val lapTimeWithDelta = lapTime.addDelta(hour, min, sec, millis)

        assertEquals(expectedMillis, lapTimeWithDelta.totalMillis)
        assertEquals(expectedString, lapTimeWithDelta.toString())
    }
}