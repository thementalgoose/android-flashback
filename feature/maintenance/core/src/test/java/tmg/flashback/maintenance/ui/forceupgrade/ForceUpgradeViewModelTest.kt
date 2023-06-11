package tmg.flashback.maintenance.ui.forceupgrade

import app.cash.turbine.test
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import tmg.flashback.configuration.usecases.ResetConfigUseCase
import tmg.flashback.maintenance.repository.MaintenanceRepository
import tmg.flashback.maintenance.repository.model.ForceUpgrade
import tmg.testutils.BaseTest

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
    fun `force upgrade null error message displayed prompting app restart`() = runTest {

        every { mockMaintenanceRepository.forceUpgrade } returns null
        initSUT()

        sut.outputs.title.test {
            assertEquals("Error :(", expectMostRecentItem())
        }
        sut.outputs.message.test {
            assertEquals("Please restart the app", expectMostRecentItem())
        }
        sut.outputs.showLink.test {
            assertEquals(null, expectMostRecentItem())
        }
    }

    @Test
    fun `force upgrade shows message from configuration with link`() = runTest {

        every { mockMaintenanceRepository.forceUpgrade } returns ForceUpgrade(
            title = "title",
            message = "message",
            link = Pair("text", "https://www.google.com")
        )
        initSUT()

        sut.outputs.title.test {
            assertEquals("title", awaitItem())
        }
        sut.outputs.message.test {
            assertEquals("message", awaitItem())
        }
        sut.outputs.showLink.test {
            assertEquals(Pair("text", "https://www.google.com"), awaitItem())
        }

        verify {
            mockMaintenanceRepository.forceUpgrade
        }
        coVerify {
            mockResetConfigUseCase.reset()
        }
    }

    @Test
    fun `force upgrade shows message from configuration without link`() = runTest {

        every { mockMaintenanceRepository.forceUpgrade } returns ForceUpgrade(
            title = "title",
            message = "message",
            link = null
        )
        initSUT()

        sut.outputs.title.test {
            assertEquals("title", awaitItem())
        }
        sut.outputs.message.test {
            assertEquals("message", awaitItem())
        }
        sut.outputs.showLink.test {
            assertEquals(null, awaitItem())
        }

        verify {
            mockMaintenanceRepository.forceUpgrade
        }
        coVerify {
            mockResetConfigUseCase.reset()
        }
    }
}