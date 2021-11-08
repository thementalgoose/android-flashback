package tmg.flashback.ui

import androidx.lifecycle.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import tmg.common.controllers.ForceUpgradeController
import tmg.configuration.controllers.ConfigController
import tmg.flashback.managers.appshortcuts.AppShortcutManager
import tmg.flashback.rss.controllers.RSSController
import tmg.flashback.statistics.repo.*
import tmg.flashback.ui.SyncState.*
import tmg.flashback.upnext.controllers.UpNextController
import tmg.utilities.lifecycle.DataEvent
import tmg.utilities.lifecycle.Event

//region Inputs

interface SyncViewModelInputs {
    fun startRemoteConfig()
    fun startSyncCircuits()
    fun startSyncConstructors()
    fun startSyncDrivers()
    fun startSyncRaces()
}

//endregion

//region Outputs

interface SyncViewModelOutputs {
    val configState: LiveData<SyncState>
    val driversState: LiveData<SyncState>
    val constructorsState: LiveData<SyncState>
    val circuitsState: LiveData<SyncState>
    val racesState: LiveData<SyncState>

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
    private val upNextController: UpNextController,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
): ViewModel(), SyncViewModelInputs, SyncViewModelOutputs {

    var inputs: SyncViewModelInputs = this
    var outputs: SyncViewModelOutputs = this

    override val circuitsState: MutableLiveData<SyncState> = MutableLiveData(LOADING)
    override val constructorsState: MutableLiveData<SyncState> = MutableLiveData(LOADING)
    override val driversState: MutableLiveData<SyncState> = MutableLiveData(LOADING)
    override val racesState: MutableLiveData<SyncState> = MutableLiveData(LOADING)
    override val configState: MutableLiveData<SyncState> = MutableLiveData(LOADING)

    override val navigate: LiveData<DataEvent<SyncNavTarget>> = combine(
        circuitsState.asFlow(),
        constructorsState.asFlow(),
        driversState.asFlow(),
        racesState.asFlow(),
        configState.asFlow()
    ) { circuit, constructor, driver, races, config ->
        circuit == DONE && constructor == DONE && driver == DONE && races == DONE && config == DONE
    }
        .filter { it }
        .debounce(250)
        .map {
            if (forceUpgradeController.shouldForceUpgrade) {
                SyncNavTarget.FORCE_UPGRADE
            } else {
                SyncNavTarget.DASHBOARD
            }
        }
        .map { DataEvent(it) }
        .asLiveData(viewModelScope.coroutineContext)

    override fun startRemoteConfig() {

        if (!configurationController.requireSynchronisation) {
            configState.value = DONE
            return
        }

        configState.value = LOADING
        viewModelScope.launch {
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

    override fun startSyncDrivers() {
        driversState.value = LOADING
        viewModelScope.launch(ioDispatcher) {
            delay(500)
            when (driverRepository.fetchDrivers()) {
                true -> driversState.postValue(DONE)
                false -> driversState.postValue(FAILED)
            }
        }
    }

    override fun startSyncConstructors() {
        constructorsState.value = LOADING
        viewModelScope.launch(ioDispatcher) {
            delay(500)
            when (constructorRepository.fetchConstructors()) {
                true -> constructorsState.postValue(DONE)
                false -> constructorsState.postValue(FAILED)
            }
        }
    }

    override fun startSyncCircuits() {
        circuitsState.value = LOADING
        viewModelScope.launch(ioDispatcher) {
            delay(500)
            when (circuitRepository.fetchCircuits()) {
                true -> circuitsState.postValue(DONE)
                false -> circuitsState.postValue(FAILED)
            }
        }
    }

    override fun startSyncRaces() {
        racesState.value = LOADING
        viewModelScope.launch(ioDispatcher) {
            delay(500)
            when (overviewRepository.fetchOverview()) {
                true -> racesState.postValue(DONE)
                false -> racesState.postValue(FAILED)
            }
        }
    }

    private fun goToNextScreen() {
        configState.postValue(DONE)
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
