package tmg.flashback.ui.sync

import androidx.lifecycle.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import tmg.flashback.common.controllers.ForceUpgradeController
import tmg.flashback.configuration.controllers.ConfigController
import tmg.flashback.managers.appshortcuts.AppShortcutManager
import tmg.flashback.rss.controllers.RSSController
import tmg.flashback.statistics.repo.*
import tmg.flashback.statistics.repo.repository.CacheRepository
import tmg.flashback.ui.sync.SyncState.*
import tmg.flashback.statistics.controllers.UpNextController
import tmg.utilities.lifecycle.DataEvent

//region Inputs

interface SyncViewModelInputs {
    fun startLoading()
}

//endregion

//region Outputs

interface SyncViewModelOutputs {

    val loadingState: LiveData<SyncState>
    val showRetry: LiveData<Boolean>

    val navigate: LiveData<DataEvent<SyncNavTarget>>
}

//endregion

class SyncViewModel(
    private val shortcutManager: AppShortcutManager,
    private val rssController: RSSController,
    private val circuitRepository: CircuitRepository,
    private val constructorRepository: ConstructorRepository,
    private val driverRepository: DriverRepository,
    private val overviewRepository: OverviewRepository,
    private val configurationController: ConfigController,
    private val forceUpgradeController: ForceUpgradeController,
    private val cacheRepository: CacheRepository,
    private val upNextController: UpNextController,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
): ViewModel(), SyncViewModelInputs, SyncViewModelOutputs {

    var inputs: SyncViewModelInputs = this
    var outputs: SyncViewModelOutputs = this

    private val circuitsState: MutableLiveData<SyncState> = MutableLiveData(LOADING)
    private val constructorsState: MutableLiveData<SyncState> = MutableLiveData(LOADING)
    private val driversState: MutableLiveData<SyncState> = MutableLiveData(LOADING)
    private val racesState: MutableLiveData<SyncState> = MutableLiveData(LOADING)
    private val configState: MutableLiveData<SyncState> = MutableLiveData(LOADING)

    override val showRetry: MutableLiveData<Boolean> = MutableLiveData(false)

    override val loadingState: LiveData<SyncState> = combine(
        circuitsState.asFlow(),
        constructorsState.asFlow(),
        driversState.asFlow(),
        racesState.asFlow(),
        configState.asFlow()
    ) { circuit, constructor, driver, races, config ->
        val all = listOf(circuit, constructor, driver, races, config)
        if (all.all { it == DONE }) {
            return@combine DONE
        }
        if (all.all { it != LOADING }) {
            if (all.any { it == FAILED }) {
                return@combine FAILED
            }
        }
        return@combine LOADING
    }
        .map {
            if (it == FAILED) {
                showRetry.postValue(true)
            }
            return@map it
        }
        .asLiveData(viewModelScope.coroutineContext)

    override val navigate: LiveData<DataEvent<SyncNavTarget>> = loadingState
        .asFlow()
        .filter { it == DONE }
        .map {
            cacheRepository.initialSync = true
            if (forceUpgradeController.shouldForceUpgrade) {
                SyncNavTarget.FORCE_UPGRADE
            } else {
                SyncNavTarget.DASHBOARD
            }
        }
        .map { DataEvent(it) }
        .asLiveData(viewModelScope.coroutineContext)

    override fun startLoading() {
        startRemoteConfig()
        startSyncDrivers()
        startSyncConstructors()
        startSyncCircuits()
        startSyncRaces()
    }

    private fun startRemoteConfig() {

        if (!configurationController.requireSynchronisation) {
            configState.value = DONE
            return
        }

        configState.value = LOADING
        viewModelScope.launch(ioDispatcher) {
            configurationController.ensureCacheReset()

            val result = configurationController.fetchAndApply()

            performConfigUpdates()
            if (result) {
                goToNextScreen()
            } else {
                configState.postValue(FAILED)
            }
        }
    }

    private fun startSyncDrivers() {
        driversState.value = LOADING
        viewModelScope.launch(ioDispatcher) {
            when (driverRepository.fetchDrivers()) {
                true -> driversState.postValue(DONE)
                false -> driversState.postValue(FAILED)
            }
        }
    }

    private fun startSyncConstructors() {
        constructorsState.value = LOADING
        viewModelScope.launch(ioDispatcher) {
            when (constructorRepository.fetchConstructors()) {
                true -> constructorsState.postValue(DONE)
                false -> constructorsState.postValue(FAILED)
            }
        }
    }

    private fun startSyncCircuits() {
        circuitsState.value = LOADING
        viewModelScope.launch(ioDispatcher) {
            when (circuitRepository.fetchCircuits()) {
                true -> circuitsState.postValue(DONE)
                false -> circuitsState.postValue(FAILED)
            }
        }
    }

    private fun startSyncRaces() {
        racesState.value = LOADING
        viewModelScope.launch(ioDispatcher) {
            when (overviewRepository.fetchOverview()) {
                true -> racesState.postValue(DONE)
                false -> racesState.postValue(FAILED)
            }
        }
    }

    private fun goToNextScreen() {
        configState.postValue(DONE)
    }

    private suspend fun performConfigUpdates() {

        // Shortcuts for RSS
        when (rssController.enabled) {
            true -> shortcutManager.enable()
            false -> shortcutManager.disable()
        }

        // Schedule notifications
        upNextController.scheduleNotifications()
    }
}
