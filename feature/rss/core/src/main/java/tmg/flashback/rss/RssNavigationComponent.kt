package tmg.flashback.rss

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import tmg.flashback.rss.contract.RSSNavigationComponent
import tmg.flashback.rss.presentation.configure.ConfigureRSSScreenVM
import javax.inject.Inject

class RssNavigationComponentImpl @Inject constructor(): RSSNavigationComponent {
    @Composable
    override fun Configure(
        actionUpClicked: () -> Unit,
        windowSizeClass: WindowSizeClass
    ) {
        ConfigureRSSScreenVM(
            actionUpClicked = actionUpClicked,
            windowSizeClass = windowSizeClass,
        )
    }
}