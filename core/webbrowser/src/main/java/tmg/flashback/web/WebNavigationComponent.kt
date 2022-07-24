package tmg.flashback.web

import tmg.flashback.ui.navigation.ActivityProvider
import tmg.flashback.ui.navigation.NavigationDestination
import tmg.flashback.ui.navigation.Navigator
import tmg.flashback.ui.navigation.Screen
import tmg.flashback.web.usecases.PickBrowserUseCase

val Screen.Settings.WebBrowser: NavigationDestination
    get() = object : NavigationDestination {
        override val route: String = "settings/web_browser"
    }

class WebNavigationComponent(
    private val navigator: Navigator,
    private val activityProvider: ActivityProvider,
    private val pickBrowserUseCase: PickBrowserUseCase
) {
    fun web(url: String, title: String = "") = activityProvider.launch {
        pickBrowserUseCase.open(it, url, title)
    }

    fun webSettings() {
        navigator.navigate(Screen.Settings.WebBrowser)
    }
}