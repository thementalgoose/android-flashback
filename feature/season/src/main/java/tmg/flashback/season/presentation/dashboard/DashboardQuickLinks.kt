package tmg.flashback.season.presentation.dashboard

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import tmg.flashback.season.presentation.messaging.Banner
import tmg.flashback.season.presentation.messaging.News

@Composable
fun LazyItemScope.DashboardQuickLinks(
    season: Int? = null
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        News()
        Banner(
            season = season
        )
    }
}