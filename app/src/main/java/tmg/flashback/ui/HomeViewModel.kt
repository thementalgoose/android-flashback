package tmg.flashback.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import tmg.flashback.BuildConfig
import tmg.flashback.configuration.repository.ConfigRepository
import tmg.flashback.configuration.usecases.ApplyConfigUseCase
import tmg.flashback.crash_reporting.manager.CrashManager
import tmg.flashback.maintenance.repository.MaintenanceRepository
import tmg.flashback.statistics.repo.repository.CacheRepository
import tmg.flashback.results.usecases.ScheduleNotificationsUseCase
import tmg.flashback.rss.contract.usecases.RSSAppShortcutUseCase
import tmg.flashback.search.contract.usecases.SearchAppShortcutUseCase
import javax.inject.Inject

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

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val configRepository: ConfigRepository,
    private val applyConfigUseCase: ApplyConfigUseCase,
    private val rssShortcutUseCase: RSSAppShortcutUseCase,
    private val crashManager: CrashManager,
    private val maintenanceRepository: MaintenanceRepository,
    private val cacheRepository: CacheRepository,
    private val searchAppShortcutUseCase: SearchAppShortcutUseCase,
    private val scheduleNotificationsUseCase: ScheduleNotificationsUseCase
): ViewModel(), HomeViewModelInputs, HomeViewModelOutputs {

    override var requiresSync: Boolean = false
    override var forceUpgrade: Boolean = false
    override var appliedChanges: Boolean = true

    var outputs: HomeViewModelOutputs = this

    override fun initialise() {
        when {
            configRepository.requireSynchronisation || !cacheRepository.initialSync -> {
                requiresSync = true
            }
            maintenanceRepository.shouldForceUpgrade -> {
                forceUpgrade = true
            }
            else -> {
                viewModelScope.launch {
                    try {
                        val result = applyConfigUseCase.apply()
                        if (BuildConfig.DEBUG) {
                            Log.i("Home", "Pending configuration applied $result")
                        }
                        performConfigUpdates()
                    } catch (e: Exception) {
                        crashManager.logException(e)
                    }
                    appliedChanges = false
                }
            }
        }
    }

    private fun performConfigUpdates() {

        // Shortcuts for RSS
        rssShortcutUseCase.setup()

        // Shortcuts for Search
        searchAppShortcutUseCase.setup()

        // Schedule notifications
        scheduleNotificationsUseCase.schedule()
    }
}
