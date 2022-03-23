package tmg.flashback.statistics.ui.weekend

import androidx.compose.runtime.Composable
import org.koin.androidx.compose.viewModel

@Composable
fun WeekendScreen() {
    val viewModel by viewModel<WeekendViewModel>()

    WeekendScreenImpl()
}

@Composable
private fun WeekendScreenImpl() {

}