package tmg.flashback.ads.repository.converters

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import tmg.flashback.ads.repository.json.AdvertConfigJson
import tmg.flashback.ads.repository.json.AdvertLocationJson
import tmg.flashback.ads.repository.model.AdvertConfig
import tmg.testutils.BaseTest

internal class AdvertConfigConverterTest: BaseTest() {

    private val allFalseModel = AdvertConfig(
        onHomeScreen = false,
        onRaceScreen = false,
        onSearch = false,
        onRss = false,
        allowUserConfig = false
    )

    @Test
    fun `converts to model`() {
        val input = AdvertConfigJson(
            locations = AdvertLocationJson(
                home = true,
                race = true,
                search = true,
                rss = true
            ),
            allowUserConfig = true
        )
        val expected = AdvertConfig(
            onHomeScreen = true,
            onRaceScreen = true,
            onSearch = true,
            onRss = true,
            allowUserConfig = true
        )

        assertEquals(expected, input.convert())
    }

    @Test
    fun `null converts to all false model`() {
        val input: AdvertConfigJson? = null
        assertEquals(allFalseModel, input.convert())
    }

    @Test
    fun `null locations maps to false location values`() {
        val input: AdvertConfigJson = AdvertConfigJson(
            locations = null,
            allowUserConfig = true
        )
        assertEquals(allFalseModel.copy(allowUserConfig = true), input.convert())
    }
}