package tmg.flashback.rss.contract

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import dagger.hilt.EntryPoint
import dagger.hilt.EntryPoints
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
internal interface RSSUiComponent {
    fun rssNav(): RSSNavigationComponent
}

private lateinit var uiComponent: RSSUiComponent
@Composable
fun requireRSSNavigationComponent(): RSSNavigationComponent {
    if (!::uiComponent.isInitialized) {
        uiComponent = EntryPoints.get(LocalContext.current.applicationContext, RSSUiComponent::class.java)
    }
    return uiComponent.rssNav()
}