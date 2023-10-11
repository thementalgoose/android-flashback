package tmg.flashback.season.presentation.dashboard

import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.runtime.Composable
import tmg.flashback.season.presentation.messaging.Banner

@Composable
fun LazyItemScope.DashboardQuickLinks(
    season: Int? = null
) {
    Banner(
        season = season
    )
}