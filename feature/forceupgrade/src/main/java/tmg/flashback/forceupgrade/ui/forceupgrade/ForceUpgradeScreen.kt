package tmg.flashback.forceupgrade.ui.forceupgrade

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.annotations.PreviewTheme
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
                        start = AppTheme.dimens.medium,
                        end = AppTheme.dimens.medium,
                        top = AppTheme.dimens.medium,
                        bottom = AppTheme.dimens.small,
                    )
            )
            TextBody1(
                text = description,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = AppTheme.dimens.medium,
                        end = AppTheme.dimens.medium,
                        top = AppTheme.dimens.small,
                        bottom = AppTheme.dimens.medium,
                    )
            )
        }
        if (link != null) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = AppTheme.dimens.medium,
                        end = AppTheme.dimens.medium,
                        bottom = AppTheme.dimens.nsmall,
                        top = AppTheme.dimens.nsmall
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

@PreviewTheme
@Composable
private fun Preview() {
    AppThemePreview {
        ForceUpgradeScreen(
            title = "Force Upgrade",
            description = "Please update the app!",
            link = Pair("Google Play", "this is a url"),
            openLink = {}
        )
    }
}