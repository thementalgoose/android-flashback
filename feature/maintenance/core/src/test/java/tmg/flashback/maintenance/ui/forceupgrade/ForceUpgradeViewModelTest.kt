package tmg.flashback.maintenance.ui.forceupgrade

import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import tmg.flashback.configuration.usecases.ResetConfigUseCase
import tmg.flashback.maintenance.repository.MaintenanceRepository
import tmg.flashback.maintenance.repository.model.ForceUpgrade
import tmg.testutils.BaseTest
import tmg.testutils.livedata.test

internal class ForceUpgradeViewModelTest: BaseTest() {

    private val mockResetConfigUseCase: ResetConfigUseCase = mockk(relaxed = true)
    private val mockMaintenanceRepository: MaintenanceRepository = mockk(relaxed = true)

    private lateinit var sut: ForceUpgradeViewModel

    private fun initSUT() {
        sut = ForceUpgradeViewModel(mockMaintenanceRepository, mockResetConfigUseCase)
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

        every { mockMaintenanceRepository.forceUpgrade } returns null
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

        every { mockMaintenanceRepository.forceUpgrade } returns ForceUpgrade(
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
            mockMaintenanceRepository.forceUpgrade
        }
        coVerify {
            mockResetConfigUseCase.reset()
        }
    }

    @Test
    fun `force upgrade shows message from configuration without link`() {

        every { mockMaintenanceRepository.forceUpgrade } returns ForceUpgrade(
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
            mockMaintenanceRepository.forceUpgrade
        }
        coVerify {
            mockResetConfigUseCase.reset()
        }
    }
}