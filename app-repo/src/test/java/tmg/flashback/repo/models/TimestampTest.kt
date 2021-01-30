package tmg.flashback.repo.models

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.threeten.bp.*

internal class TimestampTest {

    @Test
    fun `Timestamp is date only returns true date if time is not supplied`() {
        val localDate: LocalDate = LocalDate.of(2020, 1, 1)

        val sut = Timestamp(localDate)

        assertTrue(sut.isDateOnly)
    }

    @Test
    fun `Timestamp is date only returns false date if time is supplied`() {
        val localDate: LocalDate = LocalDate.of(2020, 1, 1)
        val localTime: LocalTime = LocalTime.of(12, 1, 1)

        val sut = Timestamp(localDate, localTime)

        assertFalse(sut.isDateOnly)
    }

    @Test
    fun `Timestamp perform runs if date only when only date is supplied`() {
        val localDate: LocalDate = LocalDate.of(2020, 1, 1)
        val zone = ZoneId.ofOffset("", ZoneOffset.UTC)

        val sut = Timestamp(localDate, null, zone)

        var resultDateOnly: LocalDate? = null
        var resultDateTimeUTC: LocalDateTime? = null
        var resultDateTimeDevice: LocalDateTime? = null
        sut.perform(
                ifDateOnly = { date ->
                    resultDateOnly = date
                },
                ifTime = { utc, device ->
                    resultDateTimeUTC = utc
                    resultDateTimeDevice = device
                }
        )

        assertEquals(LocalDate.of(2020, 1, 1), resultDateOnly)
        assertNull(resultDateTimeUTC)
        assertNull(resultDateTimeDevice)
    }

    @Test
    fun `Timestamp perform runs if time when time is supplied`() {
        val localDate: LocalDate = LocalDate.of(2020, 1, 1)
        val localTime: LocalTime = LocalTime.of(12, 0, 0)
        val zone = ZoneId.ofOffset("", ZoneOffset.UTC)

        val sut = Timestamp(localDate, localTime, zone)

        var resultDateOnly: LocalDate? = null
        var resultDateTimeUTC: LocalDateTime? = null
        var resultDateTimeDevice: LocalDateTime? = null
        sut.perform(
                ifDateOnly = { date ->
                    resultDateOnly = date
                },
                ifTime = { utc, device ->
                    resultDateTimeUTC = utc
                    resultDateTimeDevice = device
                }
        )

        assertNull(resultDateOnly)
        assertEquals(LocalDateTime.of(2020, 1, 1, 12, 0), resultDateTimeUTC)
        assertEquals(LocalDateTime.of(2020, 1, 1, 12, 0), resultDateTimeDevice)
    }

    @ParameterizedTest(name = "Timestamp {1}:00 hour for {0} zone is {2}:00")
    @CsvSource(
            "-9,12,3",
            "-8,12,4",
            "-7,12,5",
            "-6,12,6",
            "-5,12,7",
            "-4,12,8",
            "-3,12,9",
            "-2,12,10",
            "-1,12,11",
            "0,12,12",
            "1,12,13",
            "2,12,14",
            "3,12,15",
            "4,12,16",
            "5,12,17",
            "6,12,18",
            "7,12,19",
            "8,12,20",
            "9,12,21"
    )
    fun `Timestamp perform runs if time when time is supplied and adheres to zone offset`(offset: Int, utcHour: Int, deviceHour: Int) {
        val localDate: LocalDate = LocalDate.of(2020, 1, 1)
        val localTime: LocalTime = LocalTime.of(utcHour, 0, 0)
        val zone = ZoneId.ofOffset("", ZoneOffset.ofHours(offset))

        val sut = Timestamp(localDate, localTime, zone)

        var resultDateOnly: LocalDate? = null
        var resultDateTimeUTC: LocalDateTime? = null
        var resultDateTimeDevice: LocalDateTime? = null
        sut.perform(
                ifDateOnly = { date ->
                    resultDateOnly = date
                },
                ifTime = { utc, device ->
                    resultDateTimeUTC = utc
                    resultDateTimeDevice = device
                }
        )

        assertNull(resultDateOnly)
        assertEquals(LocalDateTime.of(2020, 1, 1, 12, 0), resultDateTimeUTC)
        assertEquals(LocalDateTime.of(2020, 1, 1, deviceHour, 0), resultDateTimeDevice)
    }
}