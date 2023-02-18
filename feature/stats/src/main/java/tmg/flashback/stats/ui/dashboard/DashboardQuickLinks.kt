package tmg.flashback.stats.ui.dashboard

import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.runtime.Composable
import tmg.flashback.stats.ui.messaging.Banner
import tmg.flashback.stats.ui.messaging.ProvidedBy

@Composable
fun LazyItemScope.DashboardQuickLinks(
    season: Int? = null
) {
    Banner(
        season = season
    )
    ProvidedBy()
}