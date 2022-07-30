package tmg.flashback.appshortcuts.manager

import android.content.Context
import android.os.Build
import android.os.PersistableBundle
import androidx.core.content.pm.ShortcutInfoCompat
import androidx.core.content.pm.ShortcutManagerCompat
import androidx.core.graphics.drawable.IconCompat
import tmg.flashback.appshortcuts.models.ShortcutInfo
import tmg.flashback.appshortcuts.provider.HomeClassProvider
import javax.inject.Inject

class AppShortcutManager @Inject constructor(
    private val applicationContext: Context,
    private val homeClassProvider: HomeClassProvider
) {

    fun setDynamicShortcuts(shortcuts: List<ShortcutInfo>) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            ShortcutManagerCompat.setDynamicShortcuts(applicationContext, shortcuts.mapNotNull { buildShortcutModel(it) })
        }
    }

    fun addDynamicShortcut(shortcut: ShortcutInfo) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            val shortcutModel = buildShortcutModel(shortcut) ?: return
            ShortcutManagerCompat.pushDynamicShortcut(applicationContext, shortcutModel)
        }
    }

    fun removeDynamicShortcut(shortcutId: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            ShortcutManagerCompat.removeDynamicShortcuts(applicationContext, listOf(shortcutId))
        }
    }

    fun clearDynamicShortcuts() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            ShortcutManagerCompat.removeAllDynamicShortcuts(applicationContext)
        }
    }

    private fun buildShortcutModel(model: ShortcutInfo): ShortcutInfoCompat? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            try {
                ShortcutInfoCompat.Builder(applicationContext, model.id)
                    .setShortLabel(applicationContext.getString(model.shortLabel))
                    .setLongLabel(applicationContext.getString(model.longLabel))
                    .setIcon(IconCompat.createWithResource(applicationContext, model.icon))
                    .setDisabledMessage(applicationContext.getString(model.unavailableMessage))
                    .setIntent(model.intentResolver(applicationContext.applicationContext, homeClassProvider.getHomeActivity()))
                    .build()
            } catch (e: IncompatibleClassChangeError) {
                // If it fails to generate the icon for the app shortcut
                null
            } catch (e: RuntimeException) {
                // If some other error happens, not critical functionality
                null
            }
        } else {
            null
        }
    }
}