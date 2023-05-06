package tmg.flashback.ui.sync

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import tmg.flashback.configuration.repository.ConfigRepository
import tmg.flashback.configuration.usecases.FetchConfigUseCase
import tmg.flashback.configuration.usecases.ResetConfigUseCase
import tmg.flashback.domain.repo.CircuitRepository
import tmg.flashback.domain.repo.ConstructorRepository
import tmg.flashback.domain.repo.DriverRepository
import tmg.flashback.domain.repo.OverviewRepository
import tmg.flashback.domain.repo.repository.CacheRepository
import tmg.flashback.maintenance.contract.usecases.ShouldForceUpgradeUseCase
import tmg.flashback.results.usecases.ScheduleNotificationsUseCase
import tmg.flashback.ui.sync.SyncState.DONE
import tmg.flashback.ui.sync.SyncState.FAILED
import tmg.flashback.ui.sync.SyncState.LOADING
import tmg.flashback.usecases.SetupAppShortcutUseCase
import tmg.utilities.lifecycle.DataEvent
import javax.inject.Inject

//region Inputs

interface SyncViewModelInputs {
    fun startLoading()
}

//endregion

//region Outputs

interface SyncViewModelOutputs {

    val circuitsState: LiveData<SyncState>
    val constructorsState: LiveData<SyncState>
    val driversState: LiveData<SyncState>
    val racesState: LiveData<SyncState>
    val configState: LiveData<SyncState>

    val loadingState: LiveData<SyncState>
    val showRetry: LiveData<Boolean>

    val navigate: LiveData<DataEvent<SyncNavTarget>>
}

//endregion

@HiltViewModel
class SyncViewModel @Inject constructor(
    private val circuitRepository: CircuitRepository,
    private val constructorRepository: ConstructorRepository,
    private val driverRepository: DriverRepository,
    private val overviewRepository: OverviewRepository,
    private val configRepository: ConfigRepository,
    private val resetConfigUseCase: ResetConfigUseCase,
    private val fetchConfigUseCase: FetchConfigUseCase,
    private val shouldForceUpgradeUseCase: ShouldForceUpgradeUseCase,
    private val cacheRepository: CacheRepository,
    private val scheduleNotificationsUseCase: ScheduleNotificationsUseCase,
    private val setupAppShortcutUseCase: SetupAppShortcutUseCase,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
): ViewModel(), SyncViewModelInputs, SyncViewModelOutputs {

    var inputs: SyncViewModelInputs = this
    var outputs: SyncViewModelOutputs = this

    override val circuitsState: MutableLiveData<SyncState> = MutableLiveData(LOADING)
    override val constructorsState: MutableLiveData<SyncState> = MutableLiveData(LOADING)
    override val driversState: MutableLiveData<SyncState> = MutableLiveData(LOADING)
    override val racesState: MutableLiveData<SyncState> = MutableLiveData(LOADING)
    override val configState: MutableLiveData<SyncState> = MutableLiveData(LOADING)

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
            if (shouldForceUpgradeUseCase.shouldForceUpgrade()) {
                SyncNavTarget.FORCE_UPGRADE
            } else {
                SyncNavTarget.DASHBOARD
            }
        }
        .map { DataEvent(it) }
        .asLiveData(viewModelScope.coroutineContext)

    override fun startLoading() {
        showRetry.value = false
        startRemoteConfig()
        startSyncDrivers()
        startSyncConstructors()
        startSyncCircuits()
        startSyncRaces()
    }

    private fun startRemoteConfig() {

        if (!configRepository.requireSynchronisation) {
            configState.value = DONE
            return
        }

        configState.value = LOADING
        viewModelScope.launch(ioDispatcher) {
            resetConfigUseCase.ensureReset()

            val result = fetchConfigUseCase.fetchAndApply()

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

    private fun performConfigUpdates() {
        // App Shortcuts
        setupAppShortcutUseCase.setup()
        // Schedule notifications
        scheduleNotificationsUseCase.schedule()
    }
}
