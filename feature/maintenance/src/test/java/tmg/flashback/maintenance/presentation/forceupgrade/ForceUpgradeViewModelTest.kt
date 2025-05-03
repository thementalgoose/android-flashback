package tmg.flashback.maintenance.presentation.forceupgrade

import app.cash.turbine.test
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tmg.flashback.configuration.usecases.ResetConfigUseCase
import tmg.flashback.device.managers.BuildConfigManager
import tmg.flashback.maintenance.repository.MaintenanceRepository
import tmg.flashback.maintenance.repository.model.ForceUpgrade
import tmg.testutils.BaseTest

internal class ForceUpgradeViewModelTest: BaseTest() {

    private val mockResetConfigUseCase: ResetConfigUseCase = mockk(relaxed = true)
    private val mockMaintenanceRepository: MaintenanceRepository = mockk(relaxed = true)
    private val mockBuildConfigManager: BuildConfigManager = mockk(relaxed = true)

    companion object {
        private const val FAKE_APP_ID = "APP_ID"
    }

    private lateinit var sut: ForceUpgradeViewModel

    private fun initSUT() {
        sut = ForceUpgradeViewModel(
            maintenanceRepository = mockMaintenanceRepository,
            resetConfigUseCase = mockResetConfigUseCase,
            buildConfigManager = mockBuildConfigManager
        )
    }

    @BeforeEach
    fun setUp() {
        every { mockBuildConfigManager.applicationId } returns FAKE_APP_ID
    }

    @Test
    fun `fetch is fired when screen first loaded`() {

        initSUT()
        coVerify {
            mockResetConfigUseCase.reset()
        }
    }

    @Test
    fun `force upgrade null error message displayed prompting app restart`() = runTest(testDispatcher) {

        every { mockMaintenanceRepository.forceUpgrade } returns null
        initSUT()

        sut.outputs.title.test {
            assertEquals("Error :(", expectMostRecentItem())
        }
        sut.outputs.message.test {
            assertEquals("Please restart the app", expectMostRecentItem())
        }
        sut.outputs.showLink.test {
            assertEquals("Go to the Play Store" to "https://play.google.com/store/apps/details?id=${FAKE_APP_ID}", expectMostRecentItem())
        }
    }

    @Test
    fun `force upgrade shows message from configuration with link`() = runTest(testDispatcher) {

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
    fun `force upgrade shows message from configuration without link`() = runTest(testDispatcher) {

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