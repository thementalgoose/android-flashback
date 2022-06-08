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

    private lateinit var underTest: SearchAppShortcutUseCase

    private val searchId: String = "search"

    private fun initUnderTest() {
        underTest = SearchAppShortcutUseCase(
            mockAppShortcutManager
        )
    }

    @Test
    fun `search app shortcut setup adds shortcut if toggle is enabled`() {
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