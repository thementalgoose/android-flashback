package tmg.common.repository

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import tmg.common.repository.json.ForceUpgradeJson
import tmg.configuration.controllers.ConfigController

internal class ForceUpgradeRepositoryTest {

    private val mockConfigController: ConfigController = mockk()

    private lateinit var sut: ForceUpgradeRepository

    private fun initSUT() {
        sut = ForceUpgradeRepository(mockConfigController)
    }

    //region Force upgrade model

    @Test
    fun `force upgrade is null if config returns null`() {
        every { mockConfigController.getJson<ForceUpgradeJson>(keyForceUpgrade) } returns null
        initSUT()
        assertNull(sut.forceUpgrade)
        verify {
            mockConfigController.getJson<ForceUpgradeJson>(keyForceUpgrade)
        }
    }

    @Test
    fun `force upgrade is successful if config returns model`() {
        every { mockConfigController.getJson<ForceUpgradeJson>(keyForceUpgrade) } returns ForceUpgradeJson(title = "hey", message = "hey")
        initSUT()
        val model = sut.forceUpgrade!!
        assertEquals("hey", model.title)
        assertEquals("hey", model.message)
        verify {
            mockConfigController.getJson<ForceUpgradeJson>(keyForceUpgrade)
        }
    }

    //endregion

    companion object {
        private const val keyForceUpgrade: String = "force_upgrade"
    }
}