package tmg.flashback.managers.configuration.models

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import tmg.configuration.repository.json.AllSeasonJson
import tmg.configuration.repository.json.AllSeasonsJson
import tmg.flashback.testutils.BaseTest

internal class JsonAllSeasonsTest: BaseTest() {

    @Test
    fun `convert ignores null s values`() {
        val model = AllSeasonsJson(
            seasons = listOf(
                AllSeasonJson(null),
                AllSeasonJson(2019)
            )
        )
        assertEquals(setOf(2019), model.convert())
    }

    @Test
    fun `convert maps all items`() {
        val model = AllSeasonsJson(
            seasons = listOf(
                AllSeasonJson(2017),
                AllSeasonJson(2019)
            )
        )
        assertEquals(setOf(2019, 2017), model.convert())
    }
}