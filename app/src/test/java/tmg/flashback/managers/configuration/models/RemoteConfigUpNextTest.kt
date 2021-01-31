package tmg.flashback.managers.configuration.models

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.threeten.bp.LocalDate
import tmg.flashback.core.model.Timestamp
import tmg.flashback.core.model.UpNextSchedule
import tmg.flashback.firebase.base.ConverterUtils
import tmg.flashback.testutils.BaseTest

internal class RemoteConfigUpNextTest: BaseTest() {

    //region Root

    @Test
    fun `convert returns empty list if schedule is null`() {
        val model = RemoteConfigUpNext(null)
        assertEquals(emptyList<UpNextSchedule>(), model.convert())
    }

    @Test
    fun `convert returns only valid items`() {
        val model = RemoteConfigUpNext(
            schedule = listOf(
                RemoteConfigUpNextSchedule(
                    s = 2020,
                    r = 1,
                    name = "name",
                    date = "2020-01-21",
                    time = null,
                    flag = "GBR",
                    circuit = null,
                    circuitName = null
                )
            )
        )
        val expected = UpNextSchedule(
            season = 2020,
            round = 1,
            name = "name",
            timestamp = Timestamp(LocalDate.of(2020, 1, 21)),
            flag = "GBR",
            circuitId = null,
            circuitName = null
        )

        assertEquals(listOf(expected), model.convert())
    }

    //endregion

    //region Item

    @Test
    fun `convert item is null is s (season) is null`() {
        val model = RemoteConfigUpNextSchedule(
            s = null,
            r = null,
            name = "name",
            date = "21-01-2020",
            time = null,
            flag = null,
            circuit = null,
            circuitName = null
        )
        assertNull(model.convert())
    }

    @Test
    fun `convert item is null is name is null`() {
        val model = RemoteConfigUpNextSchedule(
            s = 2020,
            r = null,
            name = null,
            date = "21-01-2020",
            time = null,
            flag = null,
            circuit = null,
            circuitName = null
        )
        assertNull(model.convert())
    }

    @Test
    fun `convert item is null is date is null`() {
        val model = RemoteConfigUpNextSchedule(
            s = 2020,
            r = null,
            name = "name",
            date = null,
            time = null,
            flag = null,
            circuit = null,
            circuitName = null
        )
        assertNull(model.convert())
    }

    @Test
    fun `convert item is null is date is invalid`() {
        val model = RemoteConfigUpNextSchedule(
            s = 2020,
            r = null,
            name = "name",
            date = "something-random",
            time = null,
            flag = null,
            circuit = null,
            circuitName = null
        )
        assertNull(model.convert())
    }

    //endregion
}