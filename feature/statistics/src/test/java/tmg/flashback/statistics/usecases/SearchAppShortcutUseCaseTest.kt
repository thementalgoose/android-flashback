package tmg.flashback.statistics.usecases

import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import tmg.flashback.appshortcuts.manager.AppShortcutManager
import tmg.flashback.appshortcuts.models.ShortcutInfo
import tmg.flashback.statistics.repository.HomeRepository

internal class SearchAppShortcutUseCaseTest {

    private val mockAppShortcutManager: AppShortcutManager = mockk(relaxed = true)
    private val mockHomeRepository: HomeRepository = mockk(relaxed = true)

    private lateinit var underTest: SearchAppShortcutUseCase

    private val searchId: String = "search"

    private fun initUnderTest() {
        underTest = SearchAppShortcutUseCase(
            mockHomeRepository,
            mockAppShortcutManager
        )
    }

    @Test
    fun `search app shortcut setup removes shortcut if toggle is disabled`() {
        every { mockHomeRepository.searchEnabled } returns false

        initUnderTest()
        underTest.setup()

        verify(exactly = 0) {
            mockAppShortcutManager.addDynamicShortcut(any())
        }
        verify {
            mockAppShortcutManager.removeDynamicShortcut(searchId)
        }
    }

    @Test
    fun `search app shortcut setup adds shortcut if toggle is enabled`() {
        every { mockHomeRepository.searchEnabled } returns true

        initUnderTest()
        underTest.setup()

        val shortcutInfo = slot<ShortcutInfo>()
        verify {
            mockAppShortcutManager.addDynamicShortcut(capture(shortcutInfo))
        }
        verify(exactly = 0) {
            mockAppShortcutManager.removeDynamicShortcut(any())
        }
        assertEquals(searchId, shortcutInfo.captured.id)
    }
}