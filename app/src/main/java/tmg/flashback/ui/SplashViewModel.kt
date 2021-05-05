package tmg.flashback.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import tmg.configuration.controllers.ConfigController
import tmg.flashback.BuildConfig
import tmg.flashback.core.controllers.FeatureController
import tmg.flashback.managers.appshortcuts.AppShortcutManager
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
    val goToDashboard: LiveData<Event>
    val goToForceUpgrade: LiveData<Event>
}

//endregion

class SplashViewModel(
    private val shortcutManager: AppShortcutManager,
    private val featureController: FeatureController,
    private val configurationController: ConfigController
): ViewModel(), SplashViewModelInputs, SplashViewModelOutputs {

    var inputs: SplashViewModelInputs = this
    var outputs: SplashViewModelOutputs = this

    override val showLoading: MutableLiveData<Boolean> = MutableLiveData(false)
    override val showResync: MutableLiveData<Boolean> = MutableLiveData(false)
    override val goToDashboard: MutableLiveData<Event> = MutableLiveData()
    override val goToForceUpgrade: MutableLiveData<Event> = MutableLiveData()

    //region Inputs

    override fun start() {
        if (configurationController.requireSynchronisation) {
            showLoading.value = true
            showResync.value = false
            viewModelScope.launch {
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
        else {
            viewModelScope.launch {
                val result = configurationController.applyPending()
                if (BuildConfig.DEBUG) {
                    Log.i("Flashback", "Pending configuration applied $result")
                }
                performConfigUpdates()
                goToNextScreen()
            }
        }
    }

    private fun goToNextScreen() {
        if (configurationController.forceUpgrade != null) {
            goToForceUpgrade.value = Event()
        }
        else {
            goToDashboard.value = Event()
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
