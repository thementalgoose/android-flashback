package tmg.flashback.ui.components.header

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.annotations.PreviewTheme
import tmg.flashback.style.text.TextHeadline1
import tmg.flashback.ui.R

@Composable
fun Header(
    text: String,
    icon: Painter?,
    iconContentDescription: String?,
    actionUpClicked: () -> Unit,
    modifier: Modifier = Modifier,
    overrideIcons: @Composable () -> Unit = { },
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = AppTheme.dimens.xsmall)
    ) {
        Row {
            if (icon != null) {
                IconButton(
                    onClick = actionUpClicked
                ) {
                    Icon(
                        painter = icon,
                        contentDescription = iconContentDescription,
                        tint = AppTheme.colors.contentPrimary
                    )
                }
                Spacer(Modifier.weight(1f))
                overrideIcons()

            } else {
                Spacer(modifier = Modifier.height(AppTheme.dimens.large + 16.dp))
            }
        }
        Spacer(Modifier.height(AppTheme.dimens.large))
        TextHeadline1(
            text = text,
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = AppTheme.dimens.medium,
                    end = AppTheme.dimens.medium,
                    top = AppTheme.dimens.medium,
                    bottom = AppTheme.dimens.medium
                )
        )
    }
}

@PreviewTheme
@Composable
private fun Preview() {
    AppThemePreview {
        Header(
            text = "2022",
            icon = painterResource(id = R.drawable.ic_menu),
            iconContentDescription = "Menu",
            actionUpClicked = { }
        )
    }
}

@PreviewTheme
@Composable
private fun PreviewNoIcon() {
    AppThemePreview {
        Header(
            text = "2022",
            icon = null,
            iconContentDescription = "Menu",
            actionUpClicked = { }
        )
    }
}