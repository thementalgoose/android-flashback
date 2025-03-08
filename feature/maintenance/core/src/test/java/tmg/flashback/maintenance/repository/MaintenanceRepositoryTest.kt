package tmg.flashback.maintenance.repository

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import tmg.flashback.configuration.manager.ConfigManager
import tmg.flashback.maintenance.data.models.ForceUpgradeDto

internal class MaintenanceRepositoryTest {

    private val mockConfigManager: ConfigManager = mockk()

    private lateinit var sut: MaintenanceRepository

    private fun initUnderTest() {
        sut = MaintenanceRepository(mockConfigManager)
    }

    //region Force upgrade model

    @Test
    fun `force upgrade is null if config returns null`() {
        every { mockConfigManager.getJson(keyForceUpgrade, ForceUpgradeDto.serializer()) } returns null
        initUnderTest()
        assertNull(sut.forceUpgrade)
        verify {
            mockConfigManager.getJson(keyForceUpgrade, ForceUpgradeDto.serializer())
        }
    }

    @Test
    fun `force upgrade is successful if config returns model`() {
        every { mockConfigManager.getJson(keyForceUpgrade, ForceUpgradeDto.serializer()) } returns ForceUpgradeDto(title = "hey", message = "hey")
        initUnderTest()
        val model = sut.forceUpgrade!!
        assertEquals("hey", model.title)
        assertEquals("hey", model.message)
        verify {
            mockConfigManager.getJson(keyForceUpgrade, ForceUpgradeDto.serializer())
        }
    }

    //endregion

    //region Soft upgrade model

    @Test
    fun `soft upgrade returns value from config manager`() {
        every { mockConfigManager.getBoolean(keySoftUpgrade) } returns true
        initUnderTest()
        assertEquals(true, sut.softUpgrade)
        verify {
            mockConfigManager.getBoolean(keySoftUpgrade)
        }
    }

    //endregion

    companion object {
        private const val keyForceUpgrade: String = "force_upgrade"
        private const val keySoftUpgrade: String = "soft_upgrade"
    }
}