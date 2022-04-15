package tmg.flashback.stats.repository.converters

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import tmg.flashback.stats.repository.converters.convert
import tmg.flashback.stats.repository.json.AllSeasonJson
import tmg.flashback.stats.repository.json.AllSeasonsJson

internal class SupportedSeasonsConverterTest {

    @Test
    fun `null seasons means empty set is returned`() {
        val json = AllSeasonsJson(seasons = null)
        assertEquals(emptySet<Int>(), json.convert())
    }

    @Test
    fun `null seasons are ignored`() {
        val json = AllSeasonsJson(seasons = listOf(
                AllSeasonJson(null),
                AllSeasonJson(2018)
        ))
        assertEquals(setOf(2018), json.convert())
    }

    @Test
    fun `valid season converted`() {
        val json = AllSeasonsJson(seasons = listOf(
                AllSeasonJson(2019),
                AllSeasonJson(2018)
        ))
        assertEquals(setOf(2019, 2018), json.convert())
    }
}