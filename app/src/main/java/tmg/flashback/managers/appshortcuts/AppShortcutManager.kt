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

interface AppShortcutManager {
    fun enable(): Boolean
    fun disable(): Boolean
}
