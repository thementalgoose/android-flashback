package tmg.flashback.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import tmg.flashback.device.managers.BuildConfigManager
import tmg.flashback.eastereggs.model.MenuIcons
import tmg.flashback.eastereggs.usecases.IsMenuIconEnabledUseCase
import tmg.flashback.eastereggs.usecases.IsSnowEnabledUseCase
import tmg.flashback.eastereggs.usecases.IsUkraineEnabledUseCase
import tmg.flashback.results.contract.ResultsNavigationComponent
import tmg.flashback.results.repository.NotificationsRepositoryImpl
import tmg.flashback.ui.managers.PermissionManager
import tmg.flashback.ui.managers.StyleManager
import tmg.flashback.ui.model.NightMode
import tmg.flashback.ui.permissions.RationaleType
import tmg.flashback.ui.repository.PermissionRepository
import tmg.flashback.ui.usecases.ChangeNightModeUseCase
import javax.inject.Inject

interface DashboardViewModelInputs {
    fun clickDarkMode(toState: Boolean)
    fun clickFeaturePrompt(prompt: FeaturePrompt)
}

interface DashboardViewModelOutputs {

    val isDarkMode: LiveData<Boolean>
    val featurePromptsList: LiveData<List<FeaturePrompt>>
    val appVersion: LiveData<String>

    // Easter eggs
    val snow: LiveData<Boolean>
    val titleIcon: LiveData<MenuIcons?>
    val ukraine: LiveData<Boolean>
}

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val styleManager: StyleManager,
    private val changeNightModeUseCase: ChangeNightModeUseCase,
    private val buildConfigManager: BuildConfigManager,
    private val resultsNavigationComponent: ResultsNavigationComponent,
    private val permissionManager: PermissionManager,
    private val notificationRepository: NotificationsRepositoryImpl,
    private val permissionRepository: PermissionRepository,
    isSnowEnabledUseCase: IsSnowEnabledUseCase,
    isMenuIconEnabledUseCase: IsMenuIconEnabledUseCase,
    isUkraineEnabledUseCase: IsUkraineEnabledUseCase
): ViewModel(), DashboardViewModelInputs, DashboardViewModelOutputs {

    val inputs: DashboardViewModelInputs = this
    val outputs: DashboardViewModelOutputs = this

    override val isDarkMode: MutableLiveData<Boolean> = MutableLiveData()
    override val appVersion: MutableLiveData<String> = MutableLiveData(buildConfigManager.versionName)

    override val featurePromptsList: MutableLiveData<List<FeaturePrompt>> = MutableLiveData()

    override val snow: MutableLiveData<Boolean> = MutableLiveData(isSnowEnabledUseCase())
    override val titleIcon: LiveData<MenuIcons?> = MutableLiveData(isMenuIconEnabledUseCase())
    override val ukraine: LiveData<Boolean> = MutableLiveData(isUkraineEnabledUseCase())

    init {
        initialiseFeatureList()
        initialiseDarkMode()
    }

    private fun initialiseFeatureList() {
        val list = mutableListOf<FeaturePrompt>().apply {
            if (buildConfigManager.isRuntimeNotificationsSupported &&
                !notificationRepository.seenRuntimeNotifications &&
                !permissionRepository.isRuntimeNotificationsEnabled
            ) {
                add(FeaturePrompt.RuntimeNotifications)
            }
            if (!buildConfigManager.isRuntimeNotificationsSupported && !notificationRepository.seenNotificationOnboarding) {
                add(FeaturePrompt.Notifications)
            }
        }
        featurePromptsList.postValue(list)
    }

    override fun clickDarkMode(toState: Boolean) {
        changeNightModeUseCase.setNightMode(when (toState) {
            true -> NightMode.NIGHT
            false -> NightMode.DAY
        })
        initialiseDarkMode()
    }

    private fun initialiseDarkMode() {
        isDarkMode.value = !styleManager.isDayMode
    }

    override fun clickFeaturePrompt(prompt: FeaturePrompt) {
        when (prompt) {
            FeaturePrompt.Notifications -> {
                resultsNavigationComponent.featureNotificationOnboarding()
                notificationRepository.seenNotificationOnboarding = true
                initialiseFeatureList()
            }
            FeaturePrompt.RuntimeNotifications -> {
                viewModelScope.launch {
                    permissionManager
                        .requestPermission(RationaleType.RuntimeNotifications)
                        .invokeOnCompletion {
                            notificationRepository.seenRuntimeNotifications = true
                            if (permissionRepository.isRuntimeNotificationsEnabled) {
                                resultsNavigationComponent.featureNotificationOnboarding()
                            }
                            initialiseFeatureList()
                        }
                }
            }
        }
    }
}