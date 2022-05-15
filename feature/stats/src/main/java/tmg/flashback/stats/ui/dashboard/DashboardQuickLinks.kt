package tmg.flashback.stats.ui.dashboard

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import tmg.flashback.stats.ui.messaging.Banner
import tmg.flashback.stats.ui.messaging.ProvidedBy

@Composable
fun DashboardQuickLinks(
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier.fillMaxWidth()) {
        Banner()
        ProvidedBy()
//        Column(modifier = Modifier
//            .horizontalScroll(rememberScrollState())
//        ) {
//            Banner(
//                message = stringResource(id = R.string.tyres_list_title)
//            )
//        }
    }
}