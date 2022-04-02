package tmg.flashback.statistics.ui.weekend.race

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.text.TextTitle

@Composable
fun RaceScreen(
    season: Int,
    round: Int
) {
}

@Composable
private fun RaceScreenImpl(
    showLoading: Boolean,
    list: List<RaceModel>
) {
    TextTitle(text = "Race")
}

@Preview
@Composable
private fun PreviewLight() {
    AppThemePreview(isLight = true) {
//        RaceScreenImpl()
    }
}