package tmg.flashback.web

import tmg.flashback.ui.navigation.ActivityProvider
import tmg.flashback.ui.navigation.NavigationDestination
import tmg.flashback.ui.navigation.Navigator
import tmg.flashback.ui.navigation.Screen
import tmg.flashback.web.usecases.PickBrowserUseCase
import javax.inject.Inject

class WebNavigationComponent @Inject constructor(
    private val activityProvider: ActivityProvider,
    private val pickBrowserUseCase: PickBrowserUseCase
) {
    fun web(url: String, title: String = "") = activityProvider.launch {
        pickBrowserUseCase.open(it, url, title)
    }
}