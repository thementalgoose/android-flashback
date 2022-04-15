package tmg.flashback.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import tmg.flashback.BuildConfig
import tmg.flashback.common.repository.ForceUpgradeRepository
import tmg.flashback.configuration.repository.ConfigRepository
import tmg.flashback.configuration.usecases.ApplyConfigUseCase
import tmg.flashback.crash_reporting.controllers.CrashController
import tmg.flashback.rss.controllers.RSSController
import tmg.flashback.statistics.controllers.ScheduleController
import tmg.flashback.statistics.repo.repository.CacheRepository
import tmg.flashback.statistics.usecases.SearchAppShortcutUseCase

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
    private val configRepository: ConfigRepository,
    private val applyConfigUseCase: ApplyConfigUseCase,
    private val rssController: RSSController,
    private val crashController: CrashController,
    private val forceUpgradeRepository: ForceUpgradeRepository,
    private val cacheRepository: CacheRepository,
    private val searchAppShortcutUseCase: SearchAppShortcutUseCase,
    private val scheduleController: ScheduleController
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
            forceUpgradeRepository.shouldForceUpgrade -> {
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
                        crashController.logException(e)
                    }
                    appliedChanges = false
                }
            }
        }
    }

    private fun performConfigUpdates() {

        // Shortcuts for RSS
        when (rssController.enabled) {
            true -> rssController.addAppShortcut()
            false -> rssController.removeAppShortcut()
        }

        // Shortcuts for Search
        searchAppShortcutUseCase.setup()

        // Schedule notifications
        scheduleController.scheduleNotifications()
    }
}
