@file:OptIn(ExperimentalTime::class)

package tmg.flashback.ui.sync

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
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
import javax.inject.Inject
import kotlin.time.ExperimentalTime

//region Inputs

interface SyncViewModelInputs {
    fun startLoading()
}

//endregion

//region Outputs

interface SyncViewModelOutputs {

    val circuitsState: StateFlow<SyncState>
    val constructorsState: StateFlow<SyncState>
    val driversState: StateFlow<SyncState>
    val racesState: StateFlow<SyncState>
    val configState: StateFlow<SyncState>

    val loadingState: StateFlow<SyncState>
    val showRetry: StateFlow<Boolean>

    val navigate: StateFlow<SyncNavTarget?>
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

    override val circuitsState: MutableStateFlow<SyncState> = MutableStateFlow(LOADING)
    override val constructorsState: MutableStateFlow<SyncState> = MutableStateFlow(LOADING)
    override val driversState: MutableStateFlow<SyncState> = MutableStateFlow(LOADING)
    override val racesState: MutableStateFlow<SyncState> = MutableStateFlow(LOADING)
    override val configState: MutableStateFlow<SyncState> = MutableStateFlow(LOADING)

    override val showRetry: MutableStateFlow<Boolean> = MutableStateFlow(false)

    override val loadingState: StateFlow<SyncState> = combine(
        circuitsState.asStateFlow(),
        constructorsState.asStateFlow(),
        driversState.asStateFlow(),
        racesState.asStateFlow(),
        configState.asStateFlow()
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
                showRetry.value = true
            }
            return@map it
        }
        .stateIn(viewModelScope, SharingStarted.Lazily, SyncState.LOADING)

    override val navigate: StateFlow<SyncNavTarget?> = loadingState
        .filter { it == DONE }
        .map {
            cacheRepository.initialSync = true
            if (shouldForceUpgradeUseCase.shouldForceUpgrade()) {
                SyncNavTarget.FORCE_UPGRADE
            } else {
                SyncNavTarget.DASHBOARD
            }
        }
        .onEach { delay(500) }
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)

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
                configState.value = FAILED
            }
        }
    }

    private fun startSyncDrivers() {
        if (driversState.value != DONE) {
            driversState.value = LOADING
            viewModelScope.launch(ioDispatcher) {
                when (driverRepository.fetchDrivers()) {
                    true -> driversState.value = DONE
                    false -> driversState.value = FAILED
                }
            }
        }
    }

    private fun startSyncConstructors() {
        if (constructorsState.value != DONE) {
            constructorsState.value = LOADING
            viewModelScope.launch(ioDispatcher) {
                when (constructorRepository.fetchConstructors()) {
                    true -> constructorsState.value = DONE
                    false -> constructorsState.value = FAILED
                }
            }
        }
    }

    private fun startSyncCircuits() {
        if (circuitsState.value != DONE) {
            circuitsState.value = LOADING
            viewModelScope.launch(ioDispatcher) {
                when (circuitRepository.fetchCircuits()) {
                    true -> circuitsState.value = DONE
                    false -> circuitsState.value = FAILED
                }
            }
        }
    }

    private fun startSyncRaces() {
        if (racesState.value != DONE) {
            racesState.value = LOADING
            viewModelScope.launch(ioDispatcher) {
                when (overviewRepository.fetchOverview()) {
                    true -> racesState.value = DONE
                    false -> racesState.value = FAILED
                }
            }
        }
    }

    private fun goToNextScreen() {
        configState.value = DONE
    }

    private fun performConfigUpdates() {
        // App Shortcuts
        setupAppShortcutUseCase.setup()
        // Schedule notifications
        scheduleNotificationsUseCase.schedule()
    }
}
