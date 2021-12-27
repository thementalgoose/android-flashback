package tmg.flashback.appshortcuts.manager

import android.content.Context
import android.content.Intent
import android.content.pm.ShortcutManager
import android.graphics.drawable.Icon
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import tmg.flashback.appshortcuts.models.ShortcutInfo

class AppShortcutManager(
    private val applicationContext: Context
) {

    private val shortcutManager: ShortcutManager? by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            applicationContext.getSystemService(Context.SHORTCUT_SERVICE) as? ShortcutManager
        } else {
            null
        }
    }

    fun setDynamicShortcuts(shortcuts: List<ShortcutInfo<*>>) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            shortcutManager?.dynamicShortcuts = shortcuts.mapNotNull { buildShortcutModel(it) }
        }
    }

    fun <T: AppCompatActivity> addDynamicShortcut(shortcut: ShortcutInfo<T>) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            val shortcutModel = buildShortcutModel(shortcut) ?: return
            val list = shortcutManager?.dynamicShortcuts?.toMutableList() ?: mutableListOf()
            list.add(shortcutModel)
            shortcutManager?.dynamicShortcuts = list
        }
    }

    fun removeDynamicShortcut(shortcutId: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            shortcutManager?.removeDynamicShortcuts(listOf(shortcutId))
        }
    }

    fun clearDynamicShortcuts() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            shortcutManager?.dynamicShortcuts = emptyList()
        }
    }

    private fun <T: AppCompatActivity> buildShortcutModel(model: ShortcutInfo<T>): android.content.pm.ShortcutInfo? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            android.content.pm.ShortcutInfo.Builder(applicationContext, model.id)
                .setShortLabel(applicationContext.getString(model.shortLabel))
                .setLongLabel(applicationContext.getString(model.longLabel))
                .setIcon(Icon.createWithResource(applicationContext, model.icon))
                .setDisabledMessage(applicationContext.getString(model.unavailableMessage))
                .setIntent(Intent(Intent.ACTION_MAIN, Uri.EMPTY, applicationContext, model.activity::class.java))
                .build()
        } else {
            null
        }
    }
}