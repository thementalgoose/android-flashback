package tmg.flashback.ui.dashboard

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import tmg.common.controllers.ForceUpgradeController
import tmg.configuration.controllers.ConfigController
import tmg.flashback.BuildConfig
import tmg.flashback.managers.appshortcuts.AppShortcutManager
import tmg.flashback.rss.controllers.RSSController
import tmg.flashback.upnext.controllers.UpNextController

//region Inputs

interface HomeViewModelInputs {
    fun initialise()
}

//endregion

//region Outputs

interface HomeViewModelOutputs {
    val requiresSync: Boolean
    val forceUpgrade: Boolean

    val appliedChanges: Boolean
}

//endregion


class HomeViewModel(
    private val configurationController: ConfigController,
    private val rssController: RSSController,
    private val forceUpgradeController: ForceUpgradeController,
    private val shortcutManager: AppShortcutManager,
    private val upNextController: UpNextController
): ViewModel(), HomeViewModelInputs, HomeViewModelOutputs {

    override var requiresSync: Boolean = false
    override var forceUpgrade: Boolean = false
    override var appliedChanges: Boolean = true

    var outputs: HomeViewModelOutputs = this

    override fun initialise() {
        when {
            configurationController.requireSynchronisation -> {
                requiresSync = true
            }
            forceUpgradeController.shouldForceUpgrade -> {
                forceUpgrade = true
            }
            else -> {
                viewModelScope.launch {
                    val result = configurationController.applyPending()
                    if (BuildConfig.DEBUG) {
                        Log.i("Flashback", "Pending configuration applied $result")
                    }
                    performConfigUpdates()
                    appliedChanges = false
                }
            }
        }
    }

    private fun performConfigUpdates() {

        // Shortcuts for RSS
        when (rssController.enabled) {
            true -> shortcutManager.enable()
            false -> shortcutManager.disable()
        }

        // Schedule notifications
        upNextController.scheduleNotifications()
    }
}
