package tmg.flashback.forceupgrade.repository

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import tmg.flashback.configuration.manager.ConfigManager
import tmg.flashback.forceupgrade.repository.json.ForceUpgradeJson
import kotlin.test.assertFalse
import kotlin.test.assertTrue

internal class ForceUpgradeRepositoryTest {

    private val mockConfigManager: ConfigManager = mockk()

    private lateinit var sut: ForceUpgradeRepository

    private fun initSUT() {
        sut = ForceUpgradeRepository(mockConfigManager)
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
        assertEquals(true, sut.shouldForceUpgrade)
        verify {
            mockConfigManager.getJson(keyForceUpgrade, ForceUpgradeJson.serializer())
        }
    }

    //endregion

    //region Should force upgrade

    @Test
    fun `should force upgrade is null if config returns null`() {
        every { mockConfigManager.getJson(keyForceUpgrade, ForceUpgradeJson.serializer()) } returns null
        initSUT()
        assertFalse(sut.shouldForceUpgrade)
    }

    @Test
    fun `should force upgrade is successful if config returns model`() {
        every { mockConfigManager.getJson(keyForceUpgrade, ForceUpgradeJson.serializer()) } returns ForceUpgradeJson(title = "hey", message = "hey")
        initSUT()
        assertTrue(sut.shouldForceUpgrade)
    }

    //endregion

    companion object {
        private const val keyForceUpgrade: String = "force_upgrade"
    }
}