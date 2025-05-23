package tmg.flashback.maintenance.data

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import tmg.flashback.maintenance.data.models.ForceUpgradeDto
import tmg.flashback.maintenance.repository.model.ForceUpgrade

internal class ForceUpgradeJsonConverterTest {

    @Test
    fun `force upgrade is null if title not specified`() {
        val json = ForceUpgradeDto(
            title = null,
            message = "message",
            link = null,
            linkText = null
        )
        Assertions.assertNull(json.convert())
    }

    @Test
    fun `force upgrade is null if message is not specified`() {
        val json = ForceUpgradeDto(
            title = "title",
            message = null,
            link = null,
            linkText = null
        )
        Assertions.assertNull(json.convert())
    }

    @Test
    fun `force upgrade link is null if original link is null`() {
        val json = ForceUpgradeDto(
            title = "title",
            message = "message",
            link = null,
            linkText = "linkText"
        )
        val expected = ForceUpgrade(
            title = "title",
            message = "message",
            link = null
        )
        Assertions.assertEquals(expected, json.convert())
    }

    @Test
    fun `force upgrade link text is null if original link is null`() {
        val json = ForceUpgradeDto(
            title = "title",
            message = "message",
            link = "link",
            linkText = null
        )
        val expected = ForceUpgrade(
            title = "title",
            message = "message",
            link = null
        )
        Assertions.assertEquals(expected, json.convert())
    }

    @Test
    fun `force upgrade links are defined generates model`() {
        val json = ForceUpgradeDto(
            title = "title",
            message = "message",
            link = "link",
            linkText = "linkText"
        )
        val expected = ForceUpgrade(
            title = "title",
            message = "message",
            link = Pair("linkText", "link")
        )
        Assertions.assertEquals(expected, json.convert())
    }

    @Test
    fun `force upgrade links null generates model`() {
        val json = ForceUpgradeDto(
            title = "title",
            message = "message",
            link = null,
            linkText = null
        )
        val expected = ForceUpgrade(
            title = "title",
            message = "message",
            link = null
        )
        Assertions.assertEquals(expected, json.convert())
    }
}