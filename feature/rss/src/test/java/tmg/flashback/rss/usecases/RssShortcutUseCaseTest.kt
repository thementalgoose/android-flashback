package tmg.flashback.rss.usecases

import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import tmg.flashback.appshortcuts.manager.AppShortcutManager
import tmg.flashback.appshortcuts.models.ShortcutInfo
import tmg.flashback.rss.repo.RSSRepository
import tmg.testutils.BaseTest

internal class RssShortcutUseCaseTest: BaseTest() {

    private val mockRssRepository: RSSRepository = mockk(relaxed = true)
    private val mockAppShortcutManager: AppShortcutManager = mockk(relaxed = true)

    private lateinit var underTest: RssShortcutUseCase

    private val rssId: String = "rss"

    private fun initUnderTest() {
        underTest = RssShortcutUseCase(
            rssRepository = mockRssRepository,
            appShortcutManager = mockAppShortcutManager,
        )
    }

    @Test
    fun `setup enables shortcut if feature enabled`() {
        every { mockRssRepository.enabled } returns true

        initUnderTest()
        underTest.setup()

        val shortcutInfo = slot<ShortcutInfo>()
        verify {
            mockAppShortcutManager.addDynamicShortcut(capture(shortcutInfo))
        }
        verify(exactly = 0) {
            mockAppShortcutManager.removeDynamicShortcut(any())
        }
        assertEquals(rssId, shortcutInfo.captured.id)
    }

    @Test
    fun `setup disables shortcut if feature disabled`() {
        every { mockRssRepository.enabled } returns false

        initUnderTest()
        underTest.setup()

        verify {
            mockAppShortcutManager.removeDynamicShortcut(rssId)
        }
        verify(exactly = 0) {
            mockAppShortcutManager.addDynamicShortcut(any())
        }
    }
}