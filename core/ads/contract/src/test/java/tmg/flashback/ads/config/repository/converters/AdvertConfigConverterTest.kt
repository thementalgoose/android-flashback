package tmg.flashback.ads.config.repository.converters

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import tmg.flashback.ads.core.repository.json.AdvertConfigJson
import tmg.flashback.ads.core.repository.json.AdvertLocationJson
import tmg.flashback.ads.core.repository.model.AdvertConfig
import tmg.flashback.ads.core.repository.converters.convert
import tmg.testutils.BaseTest

internal class AdvertConfigConverterTest: BaseTest() {

    private val allFalseModel = tmg.flashback.ads.core.repository.model.AdvertConfig(
        onHomeScreen = false,
        onRaceScreen = false,
        onSearch = false,
        onRss = false,
        allowUserConfig = false
    )

    @Test
    fun `converts to model`() {
        val input = tmg.flashback.ads.core.repository.json.AdvertConfigJson(
            locations = tmg.flashback.ads.core.repository.json.AdvertLocationJson(
                home = true,
                race = true,
                search = true,
                rss = true
            ),
            allowUserConfig = true
        )
        val expected = tmg.flashback.ads.core.repository.model.AdvertConfig(
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
        val input: tmg.flashback.ads.core.repository.json.AdvertConfigJson? = null
        assertEquals(allFalseModel, input.convert())
    }

    @Test
    fun `null locations maps to false location values`() {
        val input: tmg.flashback.ads.core.repository.json.AdvertConfigJson =
            tmg.flashback.ads.core.repository.json.AdvertConfigJson(
                locations = null,
                allowUserConfig = true
            )
        assertEquals(allFalseModel.copy(allowUserConfig = true), input.convert())
    }
}