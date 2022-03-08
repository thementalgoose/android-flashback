package tmg.flashback.statistics.usecases

import android.content.Intent
import android.net.Uri
import tmg.flashback.appshortcuts.manager.AppShortcutManager
import tmg.flashback.appshortcuts.models.ShortcutInfo
import tmg.flashback.statistics.R
import tmg.flashback.statistics.repository.HomeRepository
import tmg.flashback.statistics.ui.search.SearchActivity

class SearchAppShortcutUseCase(
    private val homeRepository: HomeRepository,
    private val appShortcutManager: AppShortcutManager
) {

    private val enabled: Boolean by lazy {
        homeRepository.searchEnabled
    }

    fun setup() {
        if (enabled) {
            appShortcutManager.addDynamicShortcut(searchShortcutInfo)
        } else {
            appShortcutManager.removeDynamicShortcut(searchShortcutInfo.id)
        }
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