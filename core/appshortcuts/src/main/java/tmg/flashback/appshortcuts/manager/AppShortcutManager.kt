package tmg.flashback.appshortcuts.manager

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.content.pm.ShortcutInfoCompat
import androidx.core.content.pm.ShortcutManagerCompat
import androidx.core.graphics.drawable.IconCompat
import dagger.hilt.android.qualifiers.ApplicationContext
import tmg.flashback.appshortcuts.models.ShortcutInfo
import tmg.flashback.appshortcuts.provider.HomeClassProvider
import javax.inject.Inject

class AppShortcutManager @Inject constructor(
    @ApplicationContext
    private val applicationContext: Context,
    private val homeClassProvider: HomeClassProvider
) {

    fun setDynamicShortcuts(shortcuts: List<ShortcutInfo>) {
        ShortcutManagerCompat.setDynamicShortcuts(applicationContext, shortcuts.mapNotNull { buildShortcutModel(it) })
    }

    fun addDynamicShortcut(shortcut: ShortcutInfo) {
        val shortcutModel = buildShortcutModel(shortcut) ?: return
        ShortcutManagerCompat.pushDynamicShortcut(applicationContext, shortcutModel)
    }

    fun removeDynamicShortcut(shortcutId: String) {
        ShortcutManagerCompat.removeDynamicShortcuts(applicationContext, listOf(shortcutId))
    }

    fun clearDynamicShortcuts() {
        ShortcutManagerCompat.removeAllDynamicShortcuts(applicationContext)
    }

    private fun buildShortcutModel(model: ShortcutInfo): ShortcutInfoCompat? {
        return try {
            ShortcutInfoCompat.Builder(applicationContext, model.id)
                .setShortLabel(applicationContext.getString(model.shortLabel))
                .setLongLabel(applicationContext.getString(model.longLabel))
                .setIcon(IconCompat.createWithResource(applicationContext, model.icon))
                .setDisabledMessage(applicationContext.getString(model.unavailableMessage))
                .setIntent(model.intentResolver(applicationContext.applicationContext, homeClassProvider.getHomeActivity()).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                })
                .build()
        } catch (e: IncompatibleClassChangeError) {
            // If it fails to generate the icon for the app shortcut
            null
        } catch (e: RuntimeException) {
            // If some other error happens, not critical functionality
            null
        }
    }
}