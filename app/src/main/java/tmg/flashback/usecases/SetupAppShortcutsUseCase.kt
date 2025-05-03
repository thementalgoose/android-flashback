package tmg.flashback.usecases

import tmg.flashback.rss.usecases.RSSAppShortcutUseCase
import tmg.flashback.search.usecases.SearchAppShortcutUseCase
import javax.inject.Inject

class SetupAppShortcutUseCase @Inject constructor(
    private val searchAppShortcutUseCase: SearchAppShortcutUseCase,
    private val rssAppShortcutUseCase: RSSAppShortcutUseCase
) {
    fun setup() {
        searchAppShortcutUseCase.setup()
        rssAppShortcutUseCase.setup()
    }
}