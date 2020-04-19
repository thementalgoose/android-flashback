package tmg.flashback.repo.models

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import tmg.flashback.repo.utils.add
import tmg.flashback.repo.utils.addDelta

class LapTimeTest {

    @Test
    fun `initialising empty lap time prints no time`() {
        val lapTime: LapTime = LapTime()
        assertEquals(lapTime.totalMillis, 0)
        assertEquals(lapTime.time, "No time")
        assertEquals(lapTime.toString(), "No time")
    }

    @Test
    fun `empty lap time object prints no time`() {
        val lapTime: LapTime = noTime
        assertEquals(lapTime.totalMillis, 0)
        assertEquals(lapTime.time, "No time")
        assertEquals(lapTime.toString(), "No time")
    }

    @ParameterizedTest
    @CsvSource(
        "234567,3:54.567",
        "21341,21.341",
        "1341,1.341"
    )
    fun `initialising lap time with millis prints correct values`(millis: Int, expectedTime: String) {

        val lapTime: LapTime = LapTime(millis)

        assertEquals(expectedTime, lapTime.time)
    }

    @ParameterizedTest
    @CsvSource(
        "1,2,3,456,1:02:03.456",
        ",2,3,456,2:03.456",
        ",,3,456,3.456",
        ",,,456,0.456"
    )
    fun `initialising lap time with hour, min, sec and millis prints correct values`(hour: Int?, mins: Int?, seconds: Int?, millis: Int, expectedTime: String) {

        val lapTime: LapTime = LapTime(hour ?: 0, mins ?: 0, seconds ?: 0, millis)

        assertEquals(expectedTime, lapTime.time)
    }

    @ParameterizedTest
    @CsvSource(
        "83748,12849,-1:10.899",
        "83748,86282,+2.534"
    )
    fun `delta calculation on lap time prints correct delta`(sourceLapTime: Int, lapTime: Int, expectedDelta: String) {

        val refLapTime: LapTime = LapTime(sourceLapTime)
        val actualLapTime: LapTime = LapTime(lapTime)

        assertEquals(expectedDelta, refLapTime.deltaTo(actualLapTime))
    }

    @Test
    fun `sample processing on lap time object`() {

        val hours = 2
        val mins = 23
        val seconds = 14
        val millis = 0

        val expectedMillis = millis + (1000 * seconds) + (1000 * 60 * mins) + (1000 * 60 * 60 * hours)

        val lapTime: LapTime = LapTime(
            hours = hours,
            mins = mins,
            seconds = seconds,
            millis = millis
        )

        assertEquals(hours, lapTime.hours)
        assertEquals(mins, lapTime.mins)
        assertEquals(seconds, lapTime.seconds)
        assertEquals(millis, lapTime.millis)

        assertEquals(expectedMillis, lapTime.totalMillis)
    }
}