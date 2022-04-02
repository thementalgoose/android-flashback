package tmg.flashback.statistics.ui.weekend.qualifying

import androidx.compose.runtime.Composable
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.text.TextTitle

@Composable
fun QualifyingScreen(
    season: Int,
    round: Int
) {
    QualifyingScreenImpl()
}

@Composable
private fun QualifyingScreenImpl(

) {
    TextTitle(text = "Qualifying")
}

@Composable
private fun PreviewLight() {
    AppThemePreview(isLight = true) {
        QualifyingScreenImpl()
    }
}