package tmg.flashback.stats.usecases

import android.content.Intent
import android.net.Uri
import androidx.core.net.toUri
import tmg.flashback.appshortcuts.manager.AppShortcutManager
import tmg.flashback.appshortcuts.models.ShortcutInfo
import tmg.flashback.stats.R

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
            intentResolver = { context, homeClass ->
                val intent = Intent(Intent.ACTION_MAIN, Uri.EMPTY, context, homeClass)
                intent.putExtra("screen", "search")
                return@ShortcutInfo intent
            }
        )
    }
}