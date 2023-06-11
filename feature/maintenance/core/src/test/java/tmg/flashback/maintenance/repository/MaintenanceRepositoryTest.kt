package tmg.flashback.maintenance.repository

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import tmg.flashback.configuration.manager.ConfigManager
import tmg.flashback.maintenance.repository.json.ForceUpgradeJson

internal class MaintenanceRepositoryTest {

    private val mockConfigManager: ConfigManager = mockk()

    private lateinit var sut: MaintenanceRepository

    private fun initSUT() {
        sut = MaintenanceRepository(mockConfigManager)
    }

    //region Force upgrade model

    @Test
    fun `force upgrade is null if config returns null`() {
        every { mockConfigManager.getJson(keyForceUpgrade, ForceUpgradeJson.serializer()) } returns null
        initSUT()
        assertNull(sut.forceUpgrade)
        verify {
            mockConfigManager.getJson(keyForceUpgrade, ForceUpgradeJson.serializer())
        }
    }

    @Test
    fun `force upgrade is successful if config returns model`() {
        every { mockConfigManager.getJson(keyForceUpgrade, ForceUpgradeJson.serializer()) } returns ForceUpgradeJson(title = "hey", message = "hey")
        initSUT()
        val model = sut.forceUpgrade!!
        assertEquals("hey", model.title)
        assertEquals("hey", model.message)
        verify {
            mockConfigManager.getJson(keyForceUpgrade, ForceUpgradeJson.serializer())
        }
    }

    //endregion

    companion object {
        private const val keyForceUpgrade: String = "force_upgrade"
    }
}