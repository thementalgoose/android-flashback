package tmg.flashback.formula1.model

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

internal class LapTimeTest {

    @Test
    fun `no time returns true when using default initializer`() {
        val model = LapTime()
        assertTrue(model.noTime)
        assertEquals(0, model.totalMillis)
    }

    @Test
    fun `no time returns false when specifying a laptime via millis`() {
        val model = LapTime(1001)
        assertFalse(model.noTime)
    }

    @Test
    fun `no time returns false when specifying a laptime via hours, mins, seconds`() {
        val model = LapTime(0, 1, 2, 3)
        assertFalse(model.noTime)
    }

    @Test
    fun `time string returns no time when no time true`() {
        val model = LapTime()
        assertTrue(model.noTime)
        assertEquals("No time", model.time)
    }

    @ParameterizedTest(name = "LapTime {0} hrs {1} mins {2} sec {3} millis shows contentDescription = {4}")
    @CsvSource(
        "-1,-1,-1,-1,No time",
        "0,0,0,0,0.000",
        "0,0,1,1,1.001",
        "0,1,1,1,1:01.001",
        "1,1,1,1,1:01:01.001"
    )
    fun `time returns valid value`(hours: Int, mins: Int, seconds: Int, milliseconds: Int, expected: String) {
        val model = LapTime(hours, mins, seconds, milliseconds)
        assertEquals(expected, model.time)
    }

    @ParameterizedTest(name = "LapTime {0} hrs {1} mins {2} sec {3} millis shows contentDescription = {4}")
    @CsvSource(
        "-1|-1|-1|-1|No time",
        "0|0|0|0|0 seconds and 000 milliseconds",
        "0|0|1|1|1 seconds and 001 milliseconds",
        "0|1|1|1|1 minutes, 01 seconds and 001 milliseconds",
        "1|1|1|1|1 hours, 1 minutes, 01 seconds and 001 milliseconds",
    delimiter = '|')
    fun `content description returns valid value`(hours: Int, mins: Int, seconds: Int, milliseconds: Int, expected: String) {
        val model = LapTime(hours, mins, seconds, milliseconds)
        assertEquals(expected, model.contentDescription)
    }

    @ParameterizedTest(name = "{0} to {1} shows delta of {2}")
    @CsvSource(
        "1001,1002,+0.001",
        "1003,1002,-0.001",
        "132456,654321,+8:41.865"
    )
    fun `deltaTo shows correct delta`(fromMillis: Int?, toMillis: Int?, delta: String) {
        val model = LapTime(fromMillis ?: 0)
        val toModel = LapTime(toMillis ?: 0)

        assertEquals(delta, model.deltaTo(toModel))
    }

    @Test
    fun `deltaTo returns 0 if delta is identical`() {
        val model = LapTime(10010)
        assertEquals("0.000", model.deltaTo(model))
    }

    @Test
    fun `total millis counts hours mins seconds`() {
        val hours = 1
        val mins = 6
        val seconds = 6
        val millis = 101
        val expected = 3966101

        val model = LapTime(hours, mins, seconds, millis)

        assertEquals(expected, model.totalMillis)
    }
}