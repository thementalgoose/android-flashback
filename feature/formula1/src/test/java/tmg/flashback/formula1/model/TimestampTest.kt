package tmg.flashback.formula1.model

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.threeten.bp.*

internal class TimestampTest {

    @Test
    fun `is date only returns true date if time is not supplied`() {
        val localDate: LocalDate = LocalDate.of(2020, 1, 1)

        val sut = tmg.flashback.formula1.model.Timestamp(localDate)

        assertTrue(sut.isDateOnly)
    }

    @Test
    fun `is date only returns false date if time is supplied`() {
        val localDate: LocalDate = LocalDate.of(2020, 1, 1)
        val localTime: LocalTime = LocalTime.of(12, 1, 1)

        val sut = tmg.flashback.formula1.model.Timestamp(localDate, localTime)

        assertFalse(sut.isDateOnly)
    }

    @Test
    fun `perform runs if date only when only date is supplied`() {
        val localDate: LocalDate = LocalDate.of(2020, 1, 1)
        val zone = ZoneId.ofOffset("", ZoneOffset.UTC)

        val sut = tmg.flashback.formula1.model.Timestamp(localDate, null, zone)

        var resultDateOnly: LocalDate? = null
        var resultDateTimeUTC: LocalDateTime? = null
        var resultDateTimeDevice: LocalDateTime? = null
        sut.ifDate { resultDateOnly = it }
        sut.ifDateAndTime { utc, local ->
            resultDateTimeDevice = local
            resultDateTimeUTC = utc
        }

        assertEquals(LocalDate.of(2020, 1, 1), resultDateOnly)
        assertNull(resultDateTimeUTC)
        assertNull(resultDateTimeDevice)
    }

    @Test
    fun `perform runs if time when time is supplied`() {
        val localDate: LocalDate = LocalDate.of(2020, 1, 1)
        val localTime: LocalTime = LocalTime.of(12, 0, 0)
        val zone = ZoneId.ofOffset("", ZoneOffset.UTC)

        val sut = tmg.flashback.formula1.model.Timestamp(localDate, localTime, zone)

        var resultDateOnly: LocalDate? = null
        var resultDateTimeUTC: LocalDateTime? = null
        var resultDateTimeDevice: LocalDateTime? = null
        sut.ifDate { resultDateOnly = it }
        sut.ifDateAndTime { utc, local ->
            resultDateTimeDevice = local
            resultDateTimeUTC = utc
        }

        assertNull(resultDateOnly)
        assertEquals(LocalDateTime.of(2020, 1, 1, 12, 0), resultDateTimeUTC)
        assertEquals(LocalDateTime.of(2020, 1, 1, 12, 0), resultDateTimeDevice)
    }

    @ParameterizedTest(name = "{1}:00 hour for {0} zone is {2}:00")
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
    fun `perform runs if time when time is supplied and adheres to zone offset`(offset: Int, utcHour: Int, deviceHour: Int) {
        val localDate: LocalDate = LocalDate.of(2020, 1, 1)
        val localTime: LocalTime = LocalTime.of(utcHour, 0, 0)
        val zone = ZoneId.ofOffset("", ZoneOffset.ofHours(offset))

        val sut = tmg.flashback.formula1.model.Timestamp(localDate, localTime, zone)

        var resultDateOnly: LocalDate? = null
        var resultDateTimeUTC: LocalDateTime? = null
        var resultDateTimeDevice: LocalDateTime? = null
        sut.ifDate { resultDateOnly = it }
        sut.ifDateAndTime { utc, local ->
            resultDateTimeDevice = local
            resultDateTimeUTC = utc
        }

        assertNull(resultDateOnly)
        assertEquals(LocalDateTime.of(2020, 1, 1, 12, 0), resultDateTimeUTC)
        assertEquals(LocalDateTime.of(2020, 1, 1, deviceHour, 0), resultDateTimeDevice)
    }

    @Test
    fun `device date when time is not supplied returns original date`() {
        val localDate: LocalDate = LocalDate.of(2020, 1, 1)
        val zone = ZoneId.ofOffset("", ZoneOffset.UTC)

        val sut = tmg.flashback.formula1.model.Timestamp(localDate, null, zone)

        assertEquals(localDate, sut.deviceDate)
    }

    @Test
    fun `device date when time is supplied returns device date`() {
        val localDate: LocalDate = LocalDate.of(2020, 1, 1)
        val localTime: LocalTime = LocalTime.of(12, 0, 0)
        val zone = ZoneId.ofOffset("", ZoneOffset.UTC)

        val sut = tmg.flashback.formula1.model.Timestamp(localDate, localTime, zone)

        assertEquals(localDate, sut.deviceDate)
    }

    @Test
    fun `device date when time is supplied that means with timezone it falls to different day, returns that different day`() {

        val expectedDate = LocalDate.of(2010, 12, 10)

        val localDate: LocalDate = LocalDate.of(2010, 12, 11)
        val localTime: LocalTime = LocalTime.of(1, 0, 0)
        val zone = ZoneId.ofOffset("", ZoneOffset.ofHours(-2))

        val sut = tmg.flashback.formula1.model.Timestamp(localDate, localTime, zone)

        assertEquals(expectedDate, sut.deviceDate)
    }

    @Test
    fun `string representation of original time`() {
        val localDate: LocalDate = LocalDate.of(2020, 1, 1)
        val localTime: LocalTime = LocalTime.of(12, 0, 0)
        val zone = ZoneId.ofOffset("", ZoneOffset.UTC)

        val sut = tmg.flashback.formula1.model.Timestamp(localDate, localTime, zone)

        assertEquals("2020010112:00:00", sut.string())
    }

    @Test
    fun `string representation of original time with no supplied time`() {
        val localDate: LocalDate = LocalDate.of(2020, 1, 1)
        val zone = ZoneId.ofOffset("", ZoneOffset.UTC)

        val sut = tmg.flashback.formula1.model.Timestamp(localDate, null, zone)

        assertEquals("2020010100:00:00", sut.string())
    }

    //region isInPast

    @Test
    fun `is in past returns true only date provided and date in past`() {
        val date = LocalDate.now().minusDays(1L)
        val zone = ZoneId.ofOffset("", ZoneOffset.UTC)

        val sut = tmg.flashback.formula1.model.Timestamp(date, null, zone)

        assertTrue(sut.isInPast)
    }

    @Test
    fun `is in past returns false only date provided and date is today`() {
        val date = LocalDate.now()
        val zone = ZoneId.ofOffset("", ZoneOffset.UTC)

        val sut = tmg.flashback.formula1.model.Timestamp(date, null, zone)

        assertFalse(sut.isInPast)
    }

    @Test
    fun `is in past returns false only date provided and date is tomorrow`() {
        val date = LocalDate.now().plusDays(1L)
        val zone = ZoneId.ofOffset("", ZoneOffset.UTC)

        val sut = tmg.flashback.formula1.model.Timestamp(date, null, zone)

        assertFalse(sut.isInPast)
    }

    @Test
    fun `is in past returns true when date is yesterday and original time is before`() {
        val date = LocalDate.now().minusDays(1L)
        val time = LocalTime.now().plusMinutes(1L)
        val zone = ZoneId.ofOffset("", ZoneOffset.UTC)

        val sut = tmg.flashback.formula1.model.Timestamp(date, time, zone)

        assertTrue(sut.isInPast)
    }

    @Test
    fun `is in past returns true when date is today and original time is in past`() {
        val date = LocalDate.now()
        val time = LocalTime.now().minusMinutes(1L)
        val zone = ZoneId.ofOffset("", ZoneOffset.UTC)

        val sut = tmg.flashback.formula1.model.Timestamp(date, time, zone)

        assertTrue(sut.isInPast)
    }

    @Test
    fun `is in past returns false when date is today and original time is now`() {
        val date = LocalDate.now()
        val time = LocalTime.now().plusMinutes(1L)
        val zone = ZoneId.ofOffset("", ZoneOffset.UTC)

        val sut = tmg.flashback.formula1.model.Timestamp(date, time, zone)

        assertFalse(sut.isInPast)
    }

    @Test
    fun `is in past returns false when date is today and original time is in future`() {
        val date = LocalDate.now()
        val time = LocalTime.now().plusMinutes(1L)
        val zone = ZoneId.ofOffset("", ZoneOffset.UTC)

        val sut = tmg.flashback.formula1.model.Timestamp(date, time, zone)

        assertFalse(sut.isInPast)
    }

    @Test
    fun `is in past returns false when date is tomorrow and original time is later`() {
        val date = LocalDate.now().plusDays(1L)
        val time = LocalTime.now().minusMinutes(1L)
        val zone = ZoneId.ofOffset("", ZoneOffset.UTC)

        val sut = tmg.flashback.formula1.model.Timestamp(date, time, zone)

        assertFalse(sut.isInPast)
    }

    //endregion

    //region isInPast

    @Test
    fun `is today returns false only date provided and date in past`() {
        val date = LocalDate.now().minusDays(1L)
        val zone = ZoneId.ofOffset("", ZoneOffset.UTC)

        val sut = tmg.flashback.formula1.model.Timestamp(date, null, zone)

        assertFalse(sut.isToday)
    }

    @Test
    fun `is today returns true only date provided and date is today`() {
        val date = LocalDate.now()
        val zone = ZoneId.ofOffset("", ZoneOffset.UTC)

        val sut = tmg.flashback.formula1.model.Timestamp(date, null, zone)

        assertTrue(sut.isToday)
    }

    @Test
    fun `is today returns false only date provided and date is tomorrow`() {
        val date = LocalDate.now().plusDays(1L)
        val zone = ZoneId.ofOffset("", ZoneOffset.UTC)

        val sut = tmg.flashback.formula1.model.Timestamp(date, null, zone)

        assertFalse(sut.isToday)
    }

    @Test
    fun `is today returns false when date is yesterday and original time is before`() {
        val date = LocalDate.now().minusDays(1L)
        val time = LocalTime.now().plusMinutes(1L)
        val zone = ZoneId.ofOffset("", ZoneOffset.UTC)

        val sut = tmg.flashback.formula1.model.Timestamp(date, time, zone)

        assertFalse(sut.isToday)
    }

    @Test
    fun `is today returns true when date is today and original time is today`() {
        val date = LocalDate.now()
        val time = LocalTime.now().minusMinutes(1L)
        val zone = ZoneId.ofOffset("", ZoneOffset.UTC)

        val sut = tmg.flashback.formula1.model.Timestamp(date, time, zone)

        assertTrue(sut.isToday)
    }

    @Test
    fun `is today returns true when date is today and original time is now`() {
        val date = LocalDate.now()
        val time = LocalTime.now().plusMinutes(1L)
        val zone = ZoneId.ofOffset("", ZoneOffset.UTC)

        val sut = tmg.flashback.formula1.model.Timestamp(date, time, zone)

        assertTrue(sut.isToday)
    }

    @Test
    fun `is today returns true when date is today and original time is in future`() {
        val date = LocalDate.now()
        val time = LocalTime.now().plusMinutes(1L)
        val zone = ZoneId.ofOffset("", ZoneOffset.UTC)

        val sut = tmg.flashback.formula1.model.Timestamp(date, time, zone)

        assertTrue(sut.isToday)
    }

    @Test
    fun `is today returns false when date is tomorrow and original time is later`() {
        val date = LocalDate.now().plusDays(1L)
        val time = LocalTime.now().minusMinutes(1L)
        val zone = ZoneId.ofOffset("", ZoneOffset.UTC)

        val sut = tmg.flashback.formula1.model.Timestamp(date, time, zone)

        assertFalse(sut.isToday)
    }

    //endregion
}