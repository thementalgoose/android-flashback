package tmg.flashback.ui.settings

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import tmg.flashback.BuildConfig
import tmg.flashback.ads.ads.repository.AdsRepository
import tmg.flashback.device.managers.BuildConfigManager
import tmg.flashback.navigation.Navigator
import tmg.flashback.navigation.Screen
import tmg.flashback.rss.contract.RSSConfigure
import tmg.flashback.rss.repo.RssRepository
import tmg.flashback.ui.repository.PermissionRepository
import tmg.flashback.ui.repository.ThemeRepository
import javax.inject.Inject

interface SettingsAllViewModelInputs {
    fun itemClicked(pref: Setting)
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
    private val navigator: Navigator,
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
                navigator.navigate(Screen.Settings.NightMode)
            }
            Settings.Theme.theme.key -> {
                navigator.navigate(Screen.Settings.Theme)
            }
            Settings.Data.layout.key -> {
                navigator.navigate(Screen.Settings.Home)
            }
            Settings.Data.weather.key -> {
                navigator.navigate(Screen.Settings.Weather)
            }
            Settings.RSS.rss.key -> {
                navigator.navigate(Screen.Settings.RSSConfigure)
            }
            Settings.Web.inAppBrowser.key -> {
                navigator.navigate(Screen.Settings.Web)
            }
            Settings.Notifications.notificationResults.key -> {
                navigator.navigate(Screen.Settings.NotificationsResults)
            }
            Settings.Notifications.notificationUpcoming.key -> {
                navigator.navigate(Screen.Settings.NotificationsUpcoming)
            }
            Settings.Notifications.notificationUpcomingNotice -> {
                navigator.navigate(Screen.Settings.NotificationsUpcomingNotice)
            }
            Settings.Ads.ads.key -> {
                navigator.navigate(Screen.Settings.Ads)
            }
            Settings.Other.privacy.key -> {
                navigator.navigate(Screen.Settings.Privacy)
            }
            Settings.Other.about.key -> {
                navigator.navigate(Screen.Settings.About)
            }
            else -> if (BuildConfig.DEBUG) {
                throw UnsupportedOperationException("Preference with key ${pref.key} is not handled")
            }
        }
    }

    private val isThemeEnabled: Boolean
        get() = themeRepository.enableThemePicker && buildConfig.isMonetThemeSupported

    fun refresh() {
        uiState.value = uiState.value.copy(
            adsEnabled = adsRepository.allowUserConfig,
            themeEnabled = isThemeEnabled,
            rssEnabled = rssRepository.enabled
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
        NOTIFICATIONS_UPCOMING,
        NOTIFICATIONS_RESULTS,
        ADS,
        PRIVACY,
        ABOUT
    }
}