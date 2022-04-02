package tmg.flashback.statistics.ui.weekend.sprint

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.text.TextTitle

@Composable
fun SprintScreen(
    season: Int,
    round: Int
) {
}

@Composable
private fun SprintScreenImpl() {
    TextTitle(text = "Sprint")
}

@Preview
@Composable
private fun PreviewLight() {
    AppThemePreview(isLight = true) {
        SprintScreenImpl()
    }
}