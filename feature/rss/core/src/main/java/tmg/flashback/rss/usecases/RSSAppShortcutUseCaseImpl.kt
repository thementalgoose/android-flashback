package tmg.flashback.rss.usecases

import android.content.Intent
import android.net.Uri
import tmg.flashback.appshortcuts.manager.AppShortcutManager
import tmg.flashback.appshortcuts.models.ShortcutInfo
import tmg.flashback.rss.R
import tmg.flashback.rss.contract.usecases.RSSAppShortcutUseCase
import tmg.flashback.rss.repo.RssRepository
import javax.inject.Inject

internal class RSSAppShortcutUseCaseImpl @Inject constructor(
    private val rssRepository: RssRepository,
    private val appShortcutManager: AppShortcutManager
): RSSAppShortcutUseCase {

    override fun setup() {
        if (rssRepository.enabled) {
            appShortcutManager.addDynamicShortcut(rssShortcutInfo)
        } else {
            appShortcutManager.removeDynamicShortcut(rssShortcutInfo.id)
        }
    }

    companion object {
        private val rssShortcutInfo: ShortcutInfo = ShortcutInfo(
            id = "rss",
            shortLabel = R.string.app_shortcut_rss_shorttitle,
            longLabel = R.string.app_shortcut_rss_longtitle,
            icon = R.drawable.app_shortcut_rss,
            unavailableMessage = R.string.app_shortcut_rss_disabled,
            intentResolver = { context, homeClass ->
                val intent = Intent(Intent.ACTION_MAIN, Uri.EMPTY, context, homeClass)
                intent.putExtra("screen", "rss")
                return@ShortcutInfo intent
            }
        )
    }
}