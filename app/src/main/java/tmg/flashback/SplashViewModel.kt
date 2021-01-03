package tmg.flashback

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import tmg.flashback.base.BaseViewModel
import tmg.flashback.managers.AppShortcutManager
import tmg.flashback.repo.config.RemoteConfigRepository
import tmg.flashback.repo.pref.DeviceRepository
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
        private val prefsDeviceDB: DeviceRepository,
        private val remoteConfigRepository: RemoteConfigRepository
): BaseViewModel(), SplashViewModelInputs, SplashViewModelOutputs {

    var inputs: SplashViewModelInputs = this
    var outputs: SplashViewModelOutputs = this

    override val showLoading: MutableLiveData<Boolean> = MutableLiveData(false)
    override val showResync: MutableLiveData<Boolean> = MutableLiveData(false)
    override val goToNextScreen: MutableLiveData<Event> = MutableLiveData()

    //region Inputs

    override fun start() {
        if (!prefsDeviceDB.remoteConfigInitialSync) {
            showLoading.value = true
            showResync.value = false
            viewModelScope.launch {
                val result = remoteConfigRepository.update(true)
                performConfigUpdates()
                if (result) {
                    prefsDeviceDB.remoteConfigInitialSync = true
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
                val result = remoteConfigRepository.activate()
                performConfigUpdates()
                goToNextScreen.value = Event()
            }
        }
    }

    //endregion

    /**
     * Perform any configuration updates off the back of a fresh activate or synchronisation
     */
    private fun performConfigUpdates() {

        // Shortcuts for RSS
        when (remoteConfigRepository.rss) {
            true -> shortcutManager.enable()
            false -> shortcutManager.disable()
        }
    }
}
