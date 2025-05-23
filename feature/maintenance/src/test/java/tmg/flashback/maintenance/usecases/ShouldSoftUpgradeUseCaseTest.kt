package tmg.flashback.maintenance.usecases

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test
import org.junit.jupiter.api.Assertions.*
import tmg.flashback.maintenance.repository.MaintenanceRepository

internal class ShouldSoftUpgradeUseCaseTest {

    private val mockMaintenanceRepository: MaintenanceRepository = mockk(relaxed = true)

    private lateinit var underTest: ShouldSoftUpgradeUseCase

    private fun initUnderTest() {
        underTest = ShouldSoftUpgradeUseCase(
            repository = mockMaintenanceRepository
        )
    }

    @Test
    fun `should soft upgrade calls repository`() {
        every { mockMaintenanceRepository.softUpgrade } returns true
        initUnderTest()

        assertEquals(true, underTest.shouldSoftUpgrade())

        verify {
            mockMaintenanceRepository.softUpgrade
        }
    }
}