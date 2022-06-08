package tmg.flashback.statistics.usecases

import android.content.Intent
import android.net.Uri
import tmg.flashback.appshortcuts.manager.AppShortcutManager
import tmg.flashback.appshortcuts.models.ShortcutInfo
import tmg.flashback.statistics.R
import tmg.flashback.statistics.repository.HomeRepository
import tmg.flashback.statistics.ui.search.SearchActivity

class SearchAppShortcutUseCase(
    private val appShortcutManager: AppShortcutManager
) {

    fun setup() {
        appShortcutManager.addDynamicShortcut(searchShortcutInfo)
    }

    companion object {

        private val searchShortcutInfo: ShortcutInfo = ShortcutInfo(
            id = "search",
            shortLabel = R.string.app_shortcut_search_shorttitle,
            longLabel = R.string.app_shortcut_search_longtitle,
            icon = R.drawable.app_shortcut_search,
            unavailableMessage = R.string.app_shortcut_search_disabled,
            intentResolver = { context -> Intent(Intent.ACTION_MAIN, Uri.EMPTY, context, SearchActivity::class.java) }
        )
    }
}