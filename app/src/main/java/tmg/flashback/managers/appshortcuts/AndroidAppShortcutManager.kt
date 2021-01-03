package tmg.flashback.managers.appshortcuts

import android.content.Context
import android.content.Intent
import android.content.pm.ShortcutInfo
import android.content.pm.ShortcutManager
import android.graphics.drawable.Icon
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import tmg.flashback.R
import tmg.flashback.rss.ui.RSSActivity

class AndroidAppShortcutManager(
        private val context: Context
): AppShortcutManager {
    private val shortcutIdRss: String = "rss"

    @RequiresApi(Build.VERSION_CODES.N_MR1)
    private val shortcutManager: ShortcutManager? = context.getSystemService(Context.SHORTCUT_SERVICE) as? ShortcutManager

    @RequiresApi(Build.VERSION_CODES.N_MR1)
    private fun buildRssShortcut(): ShortcutInfo {
        return ShortcutInfo.Builder(context, shortcutIdRss)
                .setShortLabel(context.getString(R.string.app_shortcut_rss_shorttitle))
                .setLongLabel(context.getString(R.string.app_shortcut_rss_longtitle))
                .setIcon(Icon.createWithResource(context, R.drawable.ic_rss))
                .setDisabledMessage(context.getString(R.string.app_shortcut_rss_disabled))
                .setIntent(Intent(Intent.ACTION_MAIN, Uri.EMPTY, context, RSSActivity::class.java))
                .build()
    }

    /**
     * Enable all app shortcuts
     */
    override fun enable(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            val shortcut = buildRssShortcut()
            if (shortcutManager?.dynamicShortcuts?.size != 1) {
                shortcutManager?.dynamicShortcuts = listOf(shortcut)
            }
        }
        return true
    }

    /**
     * Disable any and all app shortcuts
     */
    override fun disable(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            shortcutManager?.removeAllDynamicShortcuts()
            shortcutManager?.disableShortcuts(listOf(shortcutIdRss))
        }
        return true
    }
}