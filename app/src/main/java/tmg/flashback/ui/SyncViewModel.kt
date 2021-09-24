package tmg.flashback.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import tmg.common.controllers.ForceUpgradeController
import tmg.configuration.controllers.ConfigController
import tmg.flashback.managers.appshortcuts.AppShortcutManager
import tmg.flashback.rss.controllers.RSSController
import tmg.flashback.upnext.controllers.UpNextController
import tmg.utilities.lifecycle.Event

//region Inputs

interface SyncViewModelInputs {
    fun start()
}

//endregion

//region Outputs

interface SyncViewModelOutputs {
    val showLoading: LiveData<Boolean>
    val showResync: LiveData<Boolean>
    val goToDashboard: LiveData<Event>
    val goToForceUpgrade: LiveData<Event>
}

//endregion

class SyncViewModel(
    private val shortcutManager: AppShortcutManager,
    private val rssController: RSSController,
    private val configurationController: ConfigController,
    private val forceUpgradeController: ForceUpgradeController,
    private val upNextController: UpNextController
): ViewModel(), SyncViewModelInputs, SyncViewModelOutputs {

    var inputs: SyncViewModelInputs = this
    var outputs: SyncViewModelOutputs = this

    override val showLoading: MutableLiveData<Boolean> = MutableLiveData(true)
    override val showResync: MutableLiveData<Boolean> = MutableLiveData(false)
    override val goToDashboard: MutableLiveData<Event> = MutableLiveData()
    override val goToForceUpgrade: MutableLiveData<Event> = MutableLiveData()

    override fun start() {
        showLoading.value = true
        showResync.value = false
        viewModelScope.launch {
            configurationController.ensureCacheReset()

            val result = configurationController.fetchAndApply()

            performConfigUpdates()
            if (result) {
                goToNextScreen()
            }
            else {
                showResync.value = true
                showLoading.value = false
            }
        }
    }

    private fun goToNextScreen() {
        if (forceUpgradeController.shouldForceUpgrade) {
            goToForceUpgrade.value = Event()
        }
        else {
            goToDashboard.value = Event()
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
