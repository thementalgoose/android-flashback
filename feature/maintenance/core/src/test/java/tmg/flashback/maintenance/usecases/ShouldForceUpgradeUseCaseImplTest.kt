package tmg.flashback.maintenance.usecases

import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import tmg.flashback.maintenance.repository.MaintenanceRepository

internal class ShouldForceUpgradeUseCaseImplTest {

    private val mockMaintenanceRepository: MaintenanceRepository = mockk(relaxed = true)

    private lateinit var underTest: ShouldForceUpgradeUseCaseImpl

    private fun initUnderTest() {
        underTest = ShouldForceUpgradeUseCaseImpl(
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