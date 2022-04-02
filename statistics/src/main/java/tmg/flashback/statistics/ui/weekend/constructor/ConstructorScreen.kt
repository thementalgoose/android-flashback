package tmg.flashback.statistics.ui.weekend.constructor

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.text.TextTitle

@Composable
fun ConstructorScreen(
    season: Int,
    round: Int
) {
}

@Composable
private fun ConstructorScreenImpl(

) {
    TextTitle(text = "Constructors")
}

@Preview
@Composable
private fun PreviewLight() {
    AppThemePreview(isLight = true) {
        ConstructorScreenImpl()
    }
}