package tmg.flashback.common.controllers

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import tmg.flashback.common.repository.ForceUpgradeRepository
import tmg.flashback.common.repository.model.ForceUpgrade

internal class ForceUpgradeControllerTest {

    private val mockForceUpgradeRepository: ForceUpgradeRepository = mockk()

    private lateinit var sut: ForceUpgradeController

    private fun initSUT() {
        sut = ForceUpgradeController(mockForceUpgradeRepository)
    }

    @Test
    fun `should force upgrade is true if repository is not null`() {
        every { mockForceUpgradeRepository.forceUpgrade } returns ForceUpgrade(title = "hey", message = "hey", link = null)
        initSUT()
        assertTrue(sut.shouldForceUpgrade)
        verify {
            mockForceUpgradeRepository.forceUpgrade
        }
    }

    @Test
    fun `should force upgrade is false if repository is null`() {
        every { mockForceUpgradeRepository.forceUpgrade } returns null
        initSUT()
        assertFalse(sut.shouldForceUpgrade)
        verify {
            mockForceUpgradeRepository.forceUpgrade
        }
    }

    @Test
    fun `force upgrade model returned from repository`() {
        val model = ForceUpgrade(title = "hey", message = "hey", link = null)
        every { mockForceUpgradeRepository.forceUpgrade } returns model
        initSUT()
        assertEquals(model, sut.forceUpgrade)
        verify {
            mockForceUpgradeRepository.forceUpgrade
        }
    }
}