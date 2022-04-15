package tmg.flashback.common.ui.forceupgrade

import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import tmg.flashback.common.repository.ForceUpgradeRepository
import tmg.flashback.common.repository.model.ForceUpgrade
import tmg.flashback.configuration.usecases.ResetConfigUseCase
import tmg.testutils.BaseTest
import tmg.testutils.livedata.test

internal class ForceUpgradeViewModelTest: BaseTest() {

    private val mockResetConfigUseCase: ResetConfigUseCase = mockk(relaxed = true)
    private val mockForceUpgradeRepository: ForceUpgradeRepository = mockk(relaxed = true)

    private lateinit var sut: ForceUpgradeViewModel

    private fun initSUT() {
        sut = ForceUpgradeViewModel(mockForceUpgradeRepository, mockResetConfigUseCase)
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

        every { mockForceUpgradeRepository.forceUpgrade } returns null
        initSUT()

        sut.outputs.title.test {
            assertValue("Error :(")
        }
        sut.outputs.message.test {
            assertValue("Please restart the app")
        }
        sut.outputs.showLink.test {
            assertValue(null)
        }
    }

    @Test
    fun `force upgrade shows message from configuration with link`() {

        every { mockForceUpgradeRepository.forceUpgrade } returns ForceUpgrade(
            title = "title",
            message = "message",
            link = Pair("text", "https://www.google.com")
        )
        initSUT()

        sut.outputs.title.test {
            assertValue("title")
        }
        sut.outputs.message.test {
            assertValue("message")
        }
        sut.outputs.showLink.test {
            assertValue(Pair("text", "https://www.google.com"))
        }

        verify {
            mockForceUpgradeRepository.forceUpgrade
        }
        coVerify {
            mockResetConfigUseCase.reset()
        }
    }

    @Test
    fun `force upgrade shows message from configuration without link`() {

        every { mockForceUpgradeRepository.forceUpgrade } returns ForceUpgrade(
            title = "title",
            message = "message",
            link = null
        )
        initSUT()

        sut.outputs.title.test {
            assertValue("title")
        }
        sut.outputs.message.test {
            assertValue("message")
        }
        sut.outputs.showLink.test {
            assertValue(null)
        }

        verify {
            mockForceUpgradeRepository.forceUpgrade
        }
        coVerify {
            mockResetConfigUseCase.reset()
        }
    }
}