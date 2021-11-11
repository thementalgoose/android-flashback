package tmg.flashback.upnext.repository.converters

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalTime
import tmg.flashback.formula1.model.Timestamp
import tmg.flashback.upnext.repository.json.UpNextItemJson
import tmg.flashback.upnext.repository.json.UpNextJson
import tmg.flashback.upnext.repository.json.UpNextScheduleJson
import tmg.flashback.upnext.repository.model.UpNextSchedule
import tmg.flashback.upnext.repository.model.UpNextScheduleTimestamp

internal class UpNextJsonConverterTest {

    @Test
    fun `if schedule is null then empty list is returned`() {
        val json = UpNextJson(schedule = null)
        assertEquals(emptyList<UpNextSchedule>(), json.convert())
    }

    @Test
    fun `if schedule is empty list then empty list is returned`() {
        val json = UpNextJson(schedule = emptyList())
        assertEquals(emptyList<UpNextSchedule>(), json.convert())
    }

    @Test
    fun `if schedule item doesnt have season then item is returned as null`() {
        val json = UpNextScheduleJson(
                s = null,
                r = 1,
                title = "title",
                subtitle = "subtitle",
                dates = listOf(
                        UpNextItemJson(
                                type = "FP1",
                                d = "2020-01-01",
                                t = "13:00:00"
                        )
                )
        )
        assertNull(json.convert())
    }

    @Test
    fun `if schedule item doesnt have title then item is returned as null`() {
        val json = UpNextScheduleJson(
                s = 2020,
                r = 1,
                title = null,
                subtitle = "subtitle",
                dates = listOf(
                        UpNextItemJson(
                                type = "FP1",
                                d = "2020-01-01",
                                t = "13:00:00"
                        )
                )
        )
        assertNull(json.convert())
    }

    @Test
    fun `if schedule item doesnt have dates then item is returned as null`() {
        val json = UpNextScheduleJson(
                s = 2020,
                r = 1,
                title = "title",
                subtitle = "subtitle",
                dates = emptyList()
        )
        assertNull(json.convert())
    }

    @Test
    fun `if schedule item date item doesnt have type then values is empty`() {
        val json = UpNextScheduleJson(
                s = 2020,
                r = 1,
                title = "title",
                subtitle = "subtitle",
                dates = listOf(
                        UpNextItemJson(
                                type = null,
                                d = "2020-01-01",
                                t = "13:00:00"
                        )
                )
        )
        assertNull(json.convert())
    }

    @Test
    fun `if schedule item date item doesnt have date then values is empty`() {
        val json = UpNextScheduleJson(
                s = 2020,
                r = 1,
                title = "title",
                subtitle = "subtitle",
                dates = listOf(
                        UpNextItemJson(
                                type = "FP1",
                                d = null,
                                t = "13:00:00"
                        )
                )
        )
        assertNull(json.convert())
    }

    @Test
    fun `if schedule item is valid then it's converted as expected`() {
        val json = UpNextScheduleJson(
                s = 2020,
                r = 1,
                title = "title",
                subtitle = "subtitle",
                dates = listOf(
                        UpNextItemJson(
                                type = "FP1",
                                d = "2020-01-01",
                                t = "13:00:00"
                        )
                )
        )
        val expected = UpNextSchedule(
                season = 2020,
                round = 1,
                title = "title",
                subtitle = "subtitle",
                values = listOf(UpNextScheduleTimestamp(
                        label = "FP1",
                        timestamp = Timestamp(
                                originalDate = LocalDate.of(2020, 1, 1),
                                originalTime = LocalTime.of(13, 0, 0)
                        )
                )),
                flag = null,
                circuitId = null
        )
        assertEquals(expected, json.convert())
    }

}