package tmg.flashback.stats.ui.dashboard

import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import tmg.flashback.stats.ui.messaging.Banner
import tmg.flashback.stats.ui.messaging.ProvidedBy

@Composable
fun LazyItemScope.DashboardQuickLinks(
    modifier: Modifier = Modifier
) {
    Banner()
    ProvidedBy()
}