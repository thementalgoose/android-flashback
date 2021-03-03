package tmg.flashback.statistics.ui.admin.forceupgrade

import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import tmg.flashback.core.controllers.ConfigurationController
import tmg.flashback.core.model.ForceUpgrade
import tmg.flashback.testutils.*
import tmg.flashback.testutils.BaseTest
import tmg.flashback.testutils.assertEventNotFired
import tmg.flashback.testutils.test
import tmg.flashback.ui.admin.forceupgrade.ForceUpgradeViewModel

internal class ForceUpgradeViewModelTest: BaseTest() {

    private val mockConfigurationController: ConfigurationController = mockk(relaxed = true)

    private lateinit var sut: ForceUpgradeViewModel

    private fun initSUT() {
        sut = ForceUpgradeViewModel(mockConfigurationController)
    }

    @Test
    fun `fetch is fired when screen first loaded`() {

        initSUT()
        coVerify {
            mockConfigurationController.fetch()
        }
    }

    @Test
    fun `force upgrade null error message displayed prompting app restart`() {

        every { mockConfigurationController.forceUpgrade } returns null
        initSUT()

        sut.outputs.data.test {
            assertValue(Pair("Error :(", "Please restart the app"))
        }
        sut.outputs.showLink.test {
            assertValueNull()
        }
    }

    @Test
    fun `force upgrade shows message from configuration with link`() {

        every { mockConfigurationController.forceUpgrade } returns ForceUpgrade(
            title = "title",
            message = "message",
            link = Pair("text", "https://www.google.com")
        )
        initSUT()

        sut.outputs.data.test {
            assertValue(Pair("title", "message"))
        }
        sut.outputs.showLink.test {
            assertValue(Pair("text", "https://www.google.com"))
        }

        verify {
            mockConfigurationController.forceUpgrade
        }
    }

    @Test
    fun `force upgrade shows message from configuration without link`() {

        every { mockConfigurationController.forceUpgrade } returns ForceUpgrade(
            title = "title",
            message = "message",
            link = null
        )
        initSUT()

        sut.outputs.data.test {
            assertValue(Pair("title", "message"))
        }
        sut.outputs.showLink.test {
            assertValueNull()
        }

        verify {
            mockConfigurationController.forceUpgrade
        }
    }

    @Test
    fun `click link does nothing if force upgrade doesnt exist`() {
        every { mockConfigurationController.forceUpgrade } returns ForceUpgrade(
            title = "title",
            message = "message",
            link = null
        )
        initSUT()
        sut.inputs.clickLink()

        sut.outputs.openLinkEvent.test {
            assertEventNotFired()
        }
    }

    @Test
    fun `click link fires open link event if force upgrade message populated`() {
        every { mockConfigurationController.forceUpgrade } returns ForceUpgrade(
            title = "title",
            message = "message",
            link = Pair("Title", "https://www.google.com")
        )
        initSUT()
        sut.inputs.clickLink()

        sut.outputs.openLinkEvent.test {
            assertDataEventValue("https://www.google.com")
        }
    }

}