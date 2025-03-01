package tmg.flashback.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import tmg.flashback.BuildConfig
import tmg.flashback.configuration.repository.ConfigRepository
import tmg.flashback.configuration.usecases.ApplyConfigUseCase
import tmg.flashback.crashlytics.manager.CrashlyticsManager
import tmg.flashback.data.repo.repository.CacheRepository
import tmg.flashback.maintenance.contract.usecases.ShouldForceUpgradeUseCase
import tmg.flashback.reviews.usecases.StartReviewUseCase
import tmg.flashback.season.usecases.ScheduleNotificationsUseCase
import tmg.flashback.usecases.SetupAppShortcutUseCase
import javax.inject.Inject

//region Inputs

interface HomeViewModelInputs {
    fun initialise()
    fun loadAppReview()
    fun cancelAppReview()
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
    private val crashlyticsManager: CrashlyticsManager,
    private val shouldForceUpgradeUseCase: ShouldForceUpgradeUseCase,
    private val cacheRepository: CacheRepository,
    private val setupAppShortcutUseCase: SetupAppShortcutUseCase,
    private val scheduleNotificationsUseCase: ScheduleNotificationsUseCase,
    private val startReviewUseCase: StartReviewUseCase,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
): ViewModel(), HomeViewModelInputs, HomeViewModelOutputs {

    private var reviewJob: Job? = null

    override var requiresSync: Boolean = false
    override var forceUpgrade: Boolean = false
    override var appliedChanges: Boolean = true

    var outputs: HomeViewModelOutputs = this

    override fun initialise() {
        when {
            configRepository.requireSynchronisation || !cacheRepository.initialSync -> {
                requiresSync = true
            }
            shouldForceUpgradeUseCase.shouldForceUpgrade() -> {
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
                        crashlyticsManager.logException(e)
                    }
                    appliedChanges = false
                }
            }
        }
    }

    override fun loadAppReview() {
        reviewJob = viewModelScope.launch(ioDispatcher) {
            delay(4000L)
            startReviewUseCase.start()
        }
    }

    override fun cancelAppReview() {
        reviewJob?.cancel()
        reviewJob = null
    }

    private fun performConfigUpdates() {
        setupAppShortcutUseCase.setup()
        // Schedule notifications
        scheduleNotificationsUseCase.schedule()
    }
}
