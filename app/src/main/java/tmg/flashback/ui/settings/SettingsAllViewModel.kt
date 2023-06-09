package tmg.flashback.ui.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import tmg.flashback.BuildConfig
import tmg.flashback.ads.ads.repository.AdsRepository
import tmg.flashback.device.managers.BuildConfigManager
import tmg.flashback.rss.repo.RssRepository
import tmg.flashback.navigation.Navigator
import tmg.flashback.navigation.Screen
import tmg.flashback.rss.contract.RSSConfigure
import tmg.flashback.ui.repository.ThemeRepository
import tmg.flashback.ui.settings.appearance.AppearanceNavigationComponent
import javax.inject.Inject

interface SettingsAllViewModelInputs {
    fun itemClicked(pref: Setting)
}

interface SettingsAllViewModelOutputs {
    val isThemeEnabled: LiveData<Boolean>
    val isAdsEnabled: LiveData<Boolean>
    val isRSSEnabled: LiveData<Boolean>
}

@HiltViewModel
class SettingsAllViewModel @Inject constructor(
    private val themeRepository: ThemeRepository,
    private val buildConfig: BuildConfigManager,
    private val adsRepository: AdsRepository,
    private val rssRepository: RssRepository,
    private val navigator: Navigator,
    private val appearanceNavigationComponent: AppearanceNavigationComponent
): ViewModel(), SettingsAllViewModelInputs, SettingsAllViewModelOutputs {

    val inputs: SettingsAllViewModelInputs = this
    val outputs: SettingsAllViewModelOutputs = this

    override val isThemeEnabled: MutableLiveData<Boolean> = MutableLiveData()
    override val isAdsEnabled: MutableLiveData<Boolean> = MutableLiveData()
    override val isRSSEnabled: MutableLiveData<Boolean> = MutableLiveData()

    init {
        refresh()
    }

    override fun itemClicked(pref: Setting) {
        when (pref.key) {
            Settings.Theme.darkMode.key -> {
                appearanceNavigationComponent.nightModeDialog()
            }
            Settings.Theme.theme.key -> {
                appearanceNavigationComponent.themeDialog()
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

    fun refresh() {
        isThemeEnabled.value = themeRepository.enableThemePicker && buildConfig.isMonetThemeSupported
        isAdsEnabled.value = adsRepository.allowUserConfig
        isRSSEnabled.value = rssRepository.enabled
    }
}