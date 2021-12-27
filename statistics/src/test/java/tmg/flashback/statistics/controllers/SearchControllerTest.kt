package tmg.flashback.statistics.controllers

import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import tmg.flashback.appshortcuts.manager.AppShortcutManager
import tmg.flashback.appshortcuts.models.ShortcutInfo
import tmg.flashback.statistics.repository.HomeRepository
import tmg.testutils.BaseTest

internal class SearchControllerTest: BaseTest() {

    private val mockHomeRepository: HomeRepository = mockk(relaxed = true)
    private val mockAppShortcutManager: AppShortcutManager = mockk(relaxed = true)

    private lateinit var sut: SearchController

    private fun initSUT() {
        sut = SearchController(mockHomeRepository, mockAppShortcutManager)
    }

    @Test
    fun `enabled returns value from repository`() {
        every { mockHomeRepository.searchEnabled } returns true
        initSUT()
        assertTrue(sut.enabled)
        verify {
            mockHomeRepository.searchEnabled
        }
    }

    @Test
    fun `add app shortcut adds shortcut to manager`() {
        val slot = slot<ShortcutInfo>()

        initSUT()
        sut.addAppShortcut()
        verify {
            mockAppShortcutManager.addDynamicShortcut(capture(slot))
        }
        assertEquals("search", slot.captured.id)
    }

    @Test
    fun `remove app shortcut removes shortcut to manager`() {
        initSUT()
        sut.removeAppShortcut()
        verify {
            mockAppShortcutManager.removeDynamicShortcut("search")
        }
    }
}