package tmg.common.ui.forceupgrade

import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import tmg.configuration.controllers.ConfigController
import tmg.configuration.repository.models.ForceUpgrade
import tmg.testutils.BaseTest
import tmg.testutils.livedata.assertDataEventValue
import tmg.testutils.livedata.assertEventNotFired
import tmg.testutils.livedata.test

internal class ForceUpgradeViewModelTest: BaseTest() {

    private val mockConfigurationController: ConfigController = mockk(relaxed = true)

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
            // TODO: Replace this once https://github.com/thementalgoose/android-test-utilities/pull/2 is merged
            assertEmittedCount(1)
            assertValueAt(null, 0)
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
            // TODO: Replace this once https://github.com/thementalgoose/android-test-utilities/pull/2 is merged
            assertEmittedCount(1)
            assertValueAt(null, 0)
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