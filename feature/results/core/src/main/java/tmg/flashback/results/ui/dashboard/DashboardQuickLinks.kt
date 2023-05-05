package tmg.flashback.results.ui.dashboard

import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.runtime.Composable
import tmg.flashback.results.ui.messaging.Banner
import tmg.flashback.results.ui.messaging.ProvidedBy

@Composable
fun LazyItemScope.DashboardQuickLinks(
    season: Int? = null
) {
    Banner(
        season = season
    )
}