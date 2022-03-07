package tmg.flashback.common.ui.forceupgrade

import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import tmg.flashback.common.controllers.ForceUpgradeController
import tmg.flashback.common.repository.model.ForceUpgrade
import tmg.flashback.configuration.usecases.ResetConfigUseCase
import tmg.testutils.BaseTest
import tmg.testutils.livedata.test

internal class ForceUpgradeViewModelTest: BaseTest() {

    private val mockResetConfigUseCase: ResetConfigUseCase = mockk(relaxed = true)
    private val mockForceUpgradeController: ForceUpgradeController = mockk(relaxed = true)

    private lateinit var sut: ForceUpgradeViewModel

    private fun initSUT() {
        sut = ForceUpgradeViewModel(mockForceUpgradeController, mockResetConfigUseCase)
    }

    @Test
    fun `fetch is fired when screen first loaded`() {

        initSUT()
        coVerify {
            mockResetConfigUseCase.reset()
        }
    }

    @Test
    fun `force upgrade null error message displayed prompting app restart`() {

        every { mockForceUpgradeController.forceUpgrade } returns null
        initSUT()

        sut.outputs.data.test {
            assertValue(Pair("Error :(", "Please restart the app"))
        }
        sut.outputs.showLink.test {
            assertValue(null)
        }
    }

    @Test
    fun `force upgrade shows message from configuration with link`() {

        every { mockForceUpgradeController.forceUpgrade } returns ForceUpgrade(
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
            mockForceUpgradeController.forceUpgrade
        }
        coVerify {
            mockResetConfigUseCase.reset()
        }
    }

    @Test
    fun `force upgrade shows message from configuration without link`() {

        every { mockForceUpgradeController.forceUpgrade } returns ForceUpgrade(
            title = "title",
            message = "message",
            link = null
        )
        initSUT()

        sut.outputs.data.test {
            assertValue(Pair("title", "message"))
        }
        sut.outputs.showLink.test {
            assertValue(null)
        }

        verify {
            mockForceUpgradeController.forceUpgrade
        }
        coVerify {
            mockResetConfigUseCase.reset()
        }
    }
}