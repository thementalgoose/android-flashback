package tmg.flashback.managers.configuration.models

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import tmg.flashback.testutils.BaseTest

internal class RemoteConfigAllSeasonsTest: BaseTest() {

    @Test
    fun `convert ignores null s values`() {
        val model = RemoteConfigAllSeasons(
            seasons = listOf(
                RemoteConfigAllSeason(null),
                RemoteConfigAllSeason(2019)
            )
        )
        assertEquals(setOf(2019), model.convert())
    }

    @Test
    fun `convert maps all items`() {
        val model = RemoteConfigAllSeasons(
            seasons = listOf(
                RemoteConfigAllSeason(2017),
                RemoteConfigAllSeason(2019)
            )
        )
        assertEquals(setOf(2019, 2017), model.convert())
    }
}