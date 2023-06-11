package tmg.flashback.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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

    val isDarkMode: StateFlow<Boolean>
    val featurePromptsList: StateFlow<List<FeaturePrompt>>
    val appVersion: StateFlow<String>

    // Easter eggs
    val snow: StateFlow<Boolean>
    val titleIcon: StateFlow<MenuIcons?>
    val ukraine: StateFlow<Boolean>
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

    override val isDarkMode: MutableStateFlow<Boolean> = MutableStateFlow(false)
    override val appVersion: MutableStateFlow<String> = MutableStateFlow(buildConfigManager.versionName)

    override val featurePromptsList: MutableStateFlow<List<FeaturePrompt>> = MutableStateFlow(emptyList())

    override val snow: MutableStateFlow<Boolean> = MutableStateFlow(isSnowEnabledUseCase())
    override val titleIcon: MutableStateFlow<MenuIcons?> = MutableStateFlow(isMenuIconEnabledUseCase())
    override val ukraine: MutableStateFlow<Boolean> = MutableStateFlow(isUkraineEnabledUseCase())

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
            if (!buildConfigManager.isRuntimeNotificationsSupported &&
                !notificationRepository.seenNotificationOnboarding) {
                add(FeaturePrompt.Notifications)
            }
        }
        featurePromptsList.value = list
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