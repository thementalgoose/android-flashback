package tmg.flashback.managers.configuration.models

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.threeten.bp.LocalDate
import tmg.configuration.firebase.models.RemoteConfigUpNext
import tmg.configuration.firebase.models.RemoteConfigUpNextItem
import tmg.configuration.firebase.models.RemoteConfigUpNextSchedule
import tmg.configuration.firebase.models.convert
import tmg.flashback.core.model.Timestamp
import tmg.flashback.core.model.UpNextSchedule
import tmg.flashback.core.model.UpNextScheduleTimestamp
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
                    title = "name",
                    dates = listOf(
                        RemoteConfigUpNextItem(
                            type = "Race",
                            d = "2020-01-21",
                            t = null
                        )
                    ),
                    flag = "GBR",
                    circuit = null,
                    subtitle = null
                )
            )
        )
        val expected = UpNextSchedule(
            season = 2020,
            round = 1,
            title = "name",
            values = listOf(
                UpNextScheduleTimestamp(
                    label = "Race",
                    timestamp = Timestamp(LocalDate.of(2020, 1, 21))
                )
            ),
            flag = "GBR",
            circuitId = null,
            subtitle = null
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
            title = "name",
            dates = listOf(
                RemoteConfigUpNextItem(
                    type = "Race",
                    d = "2020-01-21",
                    t = null
                )
            ),
            flag = null,
            circuit = null,
            subtitle = null
        )
        assertNull(model.convert())
    }

    @Test
    fun `convert item is null is name is null`() {
        val model = RemoteConfigUpNextSchedule(
            s = 2020,
            r = null,
            title = null,
            dates = listOf(
                RemoteConfigUpNextItem(
                    type = "Race",
                    d = "2020-01-21",
                    t = null
                )
            ),
            flag = null,
            circuit = null,
            subtitle = null
        )
        assertNull(model.convert())
    }

    @Test
    fun `convert item is null is date is null`() {
        val model = RemoteConfigUpNextSchedule(
            s = 2020,
            r = null,
            title = "name",
            dates = listOf(
                RemoteConfigUpNextItem(
                    type = "Race",
                    d = null,
                    t = null
                )
            ),
            flag = null,
            circuit = null,
            subtitle = null
        )
        assertNull(model.convert())
    }

    @Test
    fun `convert item is null is date is invalid`() {
        val model = RemoteConfigUpNextSchedule(
            s = 2020,
            r = null,
            title = "name",
            dates = listOf(
                RemoteConfigUpNextItem(
                    type = "Race",
                    d = "something-random",
                    t = null
                )
            ),
            flag = null,
            circuit = null,
            subtitle = null
        )
        assertNull(model.convert())
    }

    //endregion
}