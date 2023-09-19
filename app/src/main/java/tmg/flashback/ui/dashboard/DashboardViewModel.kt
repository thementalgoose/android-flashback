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
import tmg.flashback.navigation.ApplicationNavigationComponent
import tmg.flashback.results.contract.repository.NotificationsRepository
import tmg.flashback.device.AppPermissions
import tmg.flashback.ui.managers.PermissionManager
import tmg.flashback.ui.managers.StyleManager
import tmg.flashback.ui.model.NightMode
import tmg.flashback.device.repository.PermissionRepository
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
    private val applicationNavigationComponent: ApplicationNavigationComponent,
    private val permissionManager: PermissionManager,
    private val notificationRepository: NotificationsRepository,
    private val permissionRepository: PermissionRepository,
    isSnowEnabledUseCase: IsSnowEnabledUseCase,
    isMenuIconEnabledUseCase: IsMenuIconEnabledUseCase,
    isUkraineEnabledUseCase: IsUkraineEnabledUseCase
): ViewModel(), DashboardViewModelInputs, DashboardViewModelOutputs {

    val inputs: DashboardViewModelInputs = this
    val outputs: DashboardViewModelOutputs = this

    override val isDarkMode: MutableStateFlow<Boolean> = MutableStateFlow(!styleManager.isDayMode)
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
            FeaturePrompt.RuntimeNotifications -> {
                viewModelScope.launch {
                    permissionManager
                        .requestPermission(AppPermissions.RuntimeNotifications)
                        .invokeOnCompletion {
                            notificationRepository.seenRuntimeNotifications = true
                            if (permissionRepository.isRuntimeNotificationsEnabled) {
                                applicationNavigationComponent.appSettingsNotifications()
                            }
                            initialiseFeatureList()
                        }
                }
            }
        }
    }
}