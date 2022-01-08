package tmg.flashback.rss.ui.settings.configure.layouts

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.text.TextBody1
import tmg.flashback.style.text.TextTitle

@Composable
internal fun RssSectionLayout(
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier
) {
    return Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        TextTitle(
            text = title,
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = AppTheme.dimensions.paddingMedium,
                    end = AppTheme.dimensions.paddingMedium,
                    top = AppTheme.dimensions.paddingMedLarge,
                    bottom = AppTheme.dimensions.paddingSmall
                )
        )
        TextBody1(
            text = subtitle,
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = AppTheme.dimensions.paddingMedium,
                    end = AppTheme.dimensions.paddingMedium,
                    top = 0.dp,
                    bottom = AppTheme.dimensions.paddingMedium
                )
        )
    }
}

@Preview
@Composable
private fun PreviewLight() {
    AppThemePreview(isLight = true) {
        RssSectionLayout(
            title = "RSS List",
            subtitle = "Configure your personalised RSS feed here. Lorem Ipsum a couple of thousand times"
        )
    }
}

@Preview
@Composable
private fun PreviewDark() {
    AppThemePreview(isLight = false) {
        RssSectionLayout(
            title = "RSS List",
            subtitle = "Configure your personalised RSS feed here. Lorem Ipsum a couple of thousand times"
        )
    }
}