package tmg.flashback.maintenance.usecases

import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.jupiter.api.Test
import tmg.flashback.maintenance.repository.MaintenanceRepository

internal class ShouldForceUpgradeUseCaseTest {

    private val mockMaintenanceRepository: MaintenanceRepository = mockk(relaxed = true)

    private lateinit var underTest: ShouldForceUpgradeUseCase

    private fun initUnderTest() {
        underTest = ShouldForceUpgradeUseCase(
            repository = mockMaintenanceRepository
        )
    }

    @Test
    fun `should force upgrade returns true when force upgrade model is not null`() {
        every { mockMaintenanceRepository.forceUpgrade } returns mockk()
        initUnderTest()

        assertTrue(underTest.shouldForceUpgrade())
    }

    @Test
    fun `should force upgrade returns false when force upgrade model is null`() {
        every { mockMaintenanceRepository.forceUpgrade } returns null
        initUnderTest()

        assertFalse(underTest.shouldForceUpgrade())
    }
}