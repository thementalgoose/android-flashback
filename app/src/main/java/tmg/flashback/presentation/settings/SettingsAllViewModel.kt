package tmg.flashback.presentation.settings

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.cancel
import tmg.flashback.BuildConfig
import tmg.flashback.ads.ads.repository.AdsRepository
import tmg.flashback.device.managers.BuildConfigManager
import tmg.flashback.navigation.ApplicationNavigationComponent
import tmg.flashback.navigation.Navigator
import tmg.flashback.navigation.Screen
import tmg.flashback.rss.repo.RssRepository
import tmg.flashback.device.AppPermissions
import tmg.flashback.ui.managers.PermissionManager
import tmg.flashback.device.repository.PermissionRepository
import tmg.flashback.ui.repository.ThemeRepository
import tmg.flashback.ui.settings.Setting
import javax.inject.Inject

interface SettingsAllViewModelInputs {
    fun itemClicked(pref: Setting)
    fun back()
}

interface SettingsAllViewModelOutputs {
    val uiState: StateFlow<SettingsAllViewModel.UiState>
}

@HiltViewModel
class SettingsAllViewModel @Inject constructor(
    private val themeRepository: ThemeRepository,
    private val buildConfig: BuildConfigManager,
    private val adsRepository: AdsRepository,
    private val rssRepository: RssRepository,
    private val permissionRepository: PermissionRepository,
    private val permissionManager: PermissionManager,
    private val applicationNavigationComponent: ApplicationNavigationComponent,
): ViewModel(), SettingsAllViewModelInputs, SettingsAllViewModelOutputs {

    val inputs: SettingsAllViewModelInputs = this
    val outputs: SettingsAllViewModelOutputs = this

    override val uiState: MutableStateFlow<UiState> = MutableStateFlow(UiState(
        adsEnabled = adsRepository.allowUserConfig,
        themeEnabled = isThemeEnabled,
        rssEnabled = rssRepository.enabled,
        notificationRuntimePermission = permissionRepository.isRuntimeNotificationsEnabled,
        notificationExactAlarmPermission = permissionRepository.isExactAlarmEnabled,
    ))

    override fun itemClicked(pref: Setting) {
        when (pref.key) {
            Settings.Theme.darkMode.key -> {
                uiState.value = uiState.value.copy(selectedSubScreen = SettingsScreen.DARK_MODE)
            }
            Settings.Theme.theme.key -> {
                uiState.value = uiState.value.copy(selectedSubScreen = SettingsScreen.THEME)
            }
            Settings.Data.layout.key -> {
                uiState.value = uiState.value.copy(selectedSubScreen = SettingsScreen.LAYOUT)
            }
            Settings.Data.weather.key -> {
                uiState.value = uiState.value.copy(selectedSubScreen = SettingsScreen.WEATHER)
            }
            Settings.RSS.rss.key -> {
                uiState.value = uiState.value.copy(selectedSubScreen = SettingsScreen.RSS_CONFIGURE)
            }
            Settings.Web.inAppBrowser.key -> {
                uiState.value = uiState.value.copy(selectedSubScreen = SettingsScreen.WEB_BROWSER)
            }
            Settings.Notifications.notificationResults.key -> {
                permissionManager
                    .requestPermission(AppPermissions.RuntimeNotifications)
                    .invokeOnCompletion {
                        applicationNavigationComponent.appSettingsNotifications()
                    }
            }
            Settings.Notifications.notificationUpcoming.key -> {
                permissionManager
                    .requestPermission(AppPermissions.RuntimeNotifications)
                    .invokeOnCompletion {
                        applicationNavigationComponent.appSettingsNotifications()
                    }
            }
            Settings.Notifications.notificationUpcomingNotice.key -> {
                uiState.value = uiState.value.copy(selectedSubScreen = SettingsScreen.NOTIFICATIONS_TIMER)
            }
            Settings.Widgets.widgets.key -> {
                uiState.value = uiState.value.copy(selectedSubScreen = SettingsScreen.WIDGETS)
            }
            Settings.Ads.ads.key -> {
                uiState.value = uiState.value.copy(selectedSubScreen = SettingsScreen.ADS)
            }
            Settings.Other.privacy.key -> {
                uiState.value = uiState.value.copy(selectedSubScreen = SettingsScreen.PRIVACY)
            }
            Settings.Other.about.key -> {
                uiState.value = uiState.value.copy(selectedSubScreen = SettingsScreen.ABOUT)
            }
            else -> if (BuildConfig.DEBUG) {
                throw UnsupportedOperationException("Preference with key ${pref.key} is not handled")
            }
        }
    }

    override fun back() {
        uiState.value = uiState.value.copy(selectedSubScreen = null)
    }

    private val isThemeEnabled: Boolean
        get() = buildConfig.isMonetThemeSupported

    fun refresh() {
        uiState.value = uiState.value.copy(
            adsEnabled = adsRepository.allowUserConfig,
            themeEnabled = isThemeEnabled,
            rssEnabled = rssRepository.enabled,
            notificationRuntimePermission = permissionRepository.isRuntimeNotificationsEnabled,
            notificationExactAlarmPermission = permissionRepository.isExactAlarmEnabled,
        )
    }

    data class UiState(
        val selectedSubScreen: SettingsScreen? = null,
        val notificationRuntimePermission: Boolean,
        val notificationExactAlarmPermission: Boolean,
        val adsEnabled: Boolean,
        val rssEnabled: Boolean,
        val themeEnabled: Boolean
    )

    enum class SettingsScreen {
        DARK_MODE,
        THEME,
        LAYOUT,
        WEATHER,
        RSS_CONFIGURE,
        WEB_BROWSER,
        NOTIFICATIONS_TIMER,
        WIDGETS,
        ADS,
        PRIVACY,
        ABOUT
    }
}