package tmg.flashback.search.usecases

import android.content.Intent
import android.net.Uri
import tmg.flashback.appshortcuts.manager.AppShortcutManager
import tmg.flashback.appshortcuts.models.ShortcutInfo
import tmg.flashback.navigation.constants.Deeplink
import tmg.flashback.navigation.constants.SCREEN_PARAM
import tmg.flashback.search.R
import tmg.flashback.strings.R.string
import javax.inject.Inject

internal class SearchAppShortcutUseCase @Inject constructor(
    private val appShortcutManager: AppShortcutManager
) {
    fun setup() {
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
                intent.putExtra(SCREEN_PARAM, Deeplink.Search.route)
                return@ShortcutInfo intent
            }
        )
    }
}