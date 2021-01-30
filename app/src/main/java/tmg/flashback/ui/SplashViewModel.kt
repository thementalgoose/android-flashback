package tmg.flashback.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import tmg.flashback.BuildConfig
import tmg.flashback.controllers.FeatureController
import tmg.flashback.ui.base.BaseViewModel
import tmg.flashback.managers.appshortcuts.AppShortcutManager
import tmg.flashback.managers.remoteconfig.RemoteConfigManager
import tmg.flashback.data.pref.DeviceRepository
import tmg.utilities.lifecycle.Event

//region Inputs

interface SplashViewModelInputs {
    fun start()
}

//endregion

//region Outputs

interface SplashViewModelOutputs {
    val showLoading: LiveData<Boolean>
    val showResync: LiveData<Boolean>
    val goToNextScreen: LiveData<Event>
}

//endregion

class SplashViewModel(
        private val shortcutManager: AppShortcutManager,
        private val featureController: FeatureController,
        private val remoteConfigManager: RemoteConfigManager
): BaseViewModel(), SplashViewModelInputs, SplashViewModelOutputs {

    var inputs: SplashViewModelInputs = this
    var outputs: SplashViewModelOutputs = this

    override val showLoading: MutableLiveData<Boolean> = MutableLiveData(false)
    override val showResync: MutableLiveData<Boolean> = MutableLiveData(false)
    override val goToNextScreen: MutableLiveData<Event> = MutableLiveData()

    //region Inputs

    override fun start() {
        if (remoteConfigManager.requiresRemoteSync) {
            showLoading.value = true
            showResync.value = false
            viewModelScope.launch {
                val result = remoteConfigManager.update(true)
                performConfigUpdates()
                if (result) {
                    goToNextScreen.value = Event()
                }
                else {
                    showResync.value = true
                    showLoading.value = false
                }
            }
        }
        else {
            viewModelScope.launch {
                val result = remoteConfigManager.activate()
                performConfigUpdates()
                goToNextScreen.value = Event()
            }
        }
    }

    //endregion

    private fun performConfigUpdates() {

        // Shortcuts for RSS
        when (featureController.rssEnabled) {
            true -> shortcutManager.enable()
            false -> shortcutManager.disable()
        }
    }
}
