package tmg.flashback.managers.configuration.models

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import tmg.flashback.testutils.BaseTest

internal class RemoteConfigForceUpgradeTest: BaseTest() {

    @Test
    fun `convert method returns null if title is null`() {
        val result = RemoteConfigForceUpgrade(
                title = null,
                message = "howdy",
                link = null,
                linkText = null
        )
        assertNull(result.convert())
    }

    @Test
    fun `convert method returns null if title is empty`() {
        val result = RemoteConfigForceUpgrade(
                title = "",
                message = "howdy",
                link = null,
                linkText = null
        )
        assertNull(result.convert())
    }

    @Test
    fun `convert method returns null if message is null`() {
        val result = RemoteConfigForceUpgrade(
                title = "title",
                message = null,
                link = null,
                linkText = null
        )
        assertNull(result.convert())
    }

    @Test
    fun `convert method returns null if message is empty`() {
        val result = RemoteConfigForceUpgrade(
                title = "title",
                message = "",
                link = null,
                linkText = null
        )
        assertNull(result.convert())
    }

    @Test
    fun `convert method converts item successfully with null link`() {
        val result = RemoteConfigForceUpgrade(
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
        assertEquals(expected, result.convert())
    }

    @Test
    fun `convert method converts item successfully real link`() {
        val result = RemoteConfigForceUpgrade(
                title = "title",
                message = "message",
                link = "https://www.google.com",
                linkText = "Google"
        )
        val expected = ForceUpgrade(
                title = "title",
                message = "message",
                link = Pair("Google", "https://www.google.com")
        )
        assertEquals(expected, result.convert())
    }

}