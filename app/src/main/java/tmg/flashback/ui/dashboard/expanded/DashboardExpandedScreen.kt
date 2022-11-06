package tmg.flashback.ui.dashboard.expanded

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun DashboardExpandedScreenVM() {
    val viewModel = hiltViewModel<DashboardExpandedViewModel>()
}

@Composable
fun DashboardExpandedScreen() {
}