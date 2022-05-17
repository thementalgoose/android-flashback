package tmg.flashback.stats.ui.weekend.sprint

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.koin.androidx.compose.viewModel
import tmg.flashback.stats.ui.weekend.WeekendInfo
import tmg.flashback.stats.ui.weekend.info.RaceInfoHeader
import tmg.flashback.style.text.TextBody2


@Composable
fun SprintScreenVM(
    info: WeekendInfo,
    actionUpClicked: () -> Unit
) {
    val viewModel by viewModel<SprintViewModel>()
    SprintScreen(
        info = info,
        actionUpClicked = actionUpClicked
    )
}

@Composable
fun SprintScreen(
    info: WeekendInfo,
    actionUpClicked: () -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        content = {
            item("header") {
                RaceInfoHeader(
                    model = info,
                    actionUpClicked = actionUpClicked
                )
            }
            item("content") {
                TextBody2(text = "Sprint")
            }
        }
    )
}