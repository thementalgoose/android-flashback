package tmg.flashback.forceupgrade.ui.forceupgrade

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.buttons.ButtonPrimary
import tmg.flashback.style.text.TextBody1
import tmg.flashback.style.text.TextHeadline1

@Composable
internal fun ForceUpgradeScreen(
    title: String,
    description: String,
    link: Pair<String, String>?, // linkText, link
    openLink: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f)
        ) {
            TextHeadline1(
                text = title,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = AppTheme.dimensions.paddingMedium,
                        end = AppTheme.dimensions.paddingMedium,
                        top = AppTheme.dimensions.paddingMedium,
                        bottom = AppTheme.dimensions.paddingSmall,
                    )
            )
            TextBody1(
                text = description,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = AppTheme.dimensions.paddingMedium,
                        end = AppTheme.dimensions.paddingMedium,
                        top = AppTheme.dimensions.paddingSmall,
                        bottom = AppTheme.dimensions.paddingMedium,
                    )
            )
        }
        if (link != null) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = AppTheme.dimensions.paddingMedium,
                        end = AppTheme.dimensions.paddingMedium,
                        bottom = AppTheme.dimensions.paddingNSmall,
                        top = AppTheme.dimensions.paddingNSmall
                    )
            ) {
                ButtonPrimary(
                    text = link.first,
                    onClick = {
                        openLink(link.second)
                    }
                )
            }
        }
    }
}

@Preview
@Composable
private fun PreviewLight() {
    AppThemePreview(isLight = true) {
        ForceUpgradeScreen(
            title = "Force Upgrade",
            description = "Please update the app!",
            link = Pair("Google Play", "this is a url"),
            openLink = {}
        )
    }
}

@Preview
@Composable
private fun PreviewDark() {
    AppThemePreview(isLight = false) {
        ForceUpgradeScreen(
            title = "Force Upgrade",
            description = "Please update the app!",
            link = null,
            openLink = {}
        )
    }
}