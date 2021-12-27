package tmg.flashback.statistics.controllers

import tmg.flashback.appshortcuts.manager.AppShortcutManager
import tmg.flashback.appshortcuts.models.ShortcutInfo
import tmg.flashback.statistics.R
import tmg.flashback.statistics.repository.HomeRepository
import tmg.flashback.statistics.ui.search.SearchActivity

class SearchController(
    private val homeRepository: HomeRepository,
    private val appShortcutManager: AppShortcutManager
) {

    val enabled: Boolean by lazy {
        homeRepository.searchEnabled
    }

    fun addAppShortcut() {
        appShortcutManager.addDynamicShortcut(searchShortcutInfo)
    }

    fun removeAppShortcut() {
        appShortcutManager.removeDynamicShortcut(searchShortcutInfo.id)
    }

    companion object {
        private val searchShortcutInfo: ShortcutInfo<SearchActivity> = ShortcutInfo(
            id = "search",
            shortLabel = R.string.app_shortcut_search_shorttitle,
            longLabel = R.string.app_shortcut_search_longtitle,
            icon = R.drawable.app_shortcut_search,
            unavailableMessage = R.string.app_shortcut_search_disabled,
            activity = SearchActivity::class,
        )
    }
}