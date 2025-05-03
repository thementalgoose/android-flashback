package tmg.flashback.usecases

import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import tmg.flashback.rss.usecases.RSSAppShortcutUseCase
import tmg.flashback.search.usecases.SearchAppShortcutUseCase

internal class SetupAppShortcutUseCaseTest {

    private val mockSearchAppShortcutUseCase: SearchAppShortcutUseCase = mockk(relaxed = true)
    private val mockRSSAppShortcutUseCase: RSSAppShortcutUseCase = mockk(relaxed = true)

    private lateinit var underTest: SetupAppShortcutUseCase

    private fun initUnderTest() {
        underTest = SetupAppShortcutUseCase(
            searchAppShortcutUseCase = mockSearchAppShortcutUseCase,
            rssAppShortcutUseCase = mockRSSAppShortcutUseCase
        )
    }

    @Test
    internal fun `setup calls use cases`() {
        initUnderTest()
        underTest.setup()

        verify {
            mockSearchAppShortcutUseCase.setup()
            mockRSSAppShortcutUseCase.setup()
        }
    }
}