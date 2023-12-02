package tmg.flashback.search.usecases

import android.content.Intent
import android.net.Uri
import tmg.flashback.appshortcuts.manager.AppShortcutManager
import tmg.flashback.appshortcuts.models.ShortcutInfo
import tmg.flashback.navigation.Deeplink.SCREEN_PARAM
import tmg.flashback.navigation.Screen
import tmg.flashback.search.R
import tmg.flashback.strings.R.string
import tmg.flashback.search.contract.Search
import tmg.flashback.search.contract.usecases.SearchAppShortcutUseCase
import javax.inject.Inject

internal class SearchAppShortcutUseCaseImpl @Inject constructor(
    private val appShortcutManager: AppShortcutManager
) : SearchAppShortcutUseCase {
    override fun setup() {
        appShortcutManager.addDynamicShortcut(searchShortcutInfo)
    }

    companion object {

        private val searchShortcutInfo: ShortcutInfo = ShortcutInfo(
            id = "search",
            shortLabel = string.app_shortcut_search_shorttitle,
            longLabel = string.app_shortcut_search_longtitle,
            icon = R.drawable.app_shortcut_search,
            unavailableMessage = string.app_shortcut_search_disabled,
            intentResolver = { context, homeClass ->
                val intent = Intent(Intent.ACTION_MAIN, Uri.EMPTY, context, homeClass)
                intent.putExtra(SCREEN_PARAM, Screen.Search.route)
                return@ShortcutInfo intent
            }
        )
    }
}