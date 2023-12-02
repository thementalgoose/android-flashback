package tmg.flashback.ui.components.header

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.annotations.PreviewTheme
import tmg.flashback.style.text.TextHeadline1
import tmg.flashback.ui.R
import tmg.flashback.strings.R.string

@Composable
fun Header(
    actionUpClicked: () -> Unit,
    modifier: Modifier = Modifier,
    action: HeaderAction? = null,
    overrideIcons: @Composable () -> Unit = { },
    content: @Composable RowScope.() -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = AppTheme.dimens.xsmall)
    ) {
        if (action != null) {
            Row {
                IconButton(onClick = actionUpClicked) {
                    Icon(
                        painter = painterResource(id = action.icon),
                        contentDescription = stringResource(id = action.contentDescription),
                        tint = AppTheme.colors.contentPrimary
                    )
                }
                Spacer(Modifier.weight(1f))
                overrideIcons()
            }
            Spacer(Modifier.height(AppTheme.dimens.large))
        }
        Row(
            verticalAlignment = Alignment.Top
        ) {
            content()
            if (action == null) {
                Row(
                    modifier = Modifier.align(Alignment.CenterVertically)
                ) {
                    overrideIcons()
                }
            }
        }
    }
}

@Composable
fun Header(
    text: String,
    actionUpClicked: () -> Unit,
    modifier: Modifier = Modifier,
    action: HeaderAction? = null,
    overrideIcons: @Composable () -> Unit = { },
) {
    Header(
        actionUpClicked = actionUpClicked,
        modifier = modifier,
        action = action,
        overrideIcons = overrideIcons,
        content = {
            TextHeadline1(
                text = text,
                modifier = Modifier
                    .weight(1f)
                    .padding(
                        start = AppTheme.dimens.medium,
                        end = AppTheme.dimens.medium,
                        top = AppTheme.dimens.medium,
                        bottom = AppTheme.dimens.medium
                    )
            )
        }
    )
}

enum class HeaderAction(
    @DrawableRes
    val icon: Int,
    @StringRes
    val contentDescription: Int
) {
    MENU(
        icon = R.drawable.ic_menu,
        contentDescription = string.ab_menu
    ),
    BACK(
        icon = R.drawable.ic_back,
        contentDescription = string.ab_back
    ),
    CLOSE(
        icon = R.drawable.ic_close,
        contentDescription = string.ab_close
    )
}

@PreviewTheme
@Composable
private fun Preview() {
    AppThemePreview {
        Header(
            text = "2022",
            action = HeaderAction.MENU,
            actionUpClicked = { }
        )
    }
}

@PreviewTheme
@Composable
private fun PreviewWithOverride() {
    AppThemePreview {
        Header(
            text = "2022",
            action = HeaderAction.MENU,
            actionUpClicked = { },
            overrideIcons = {
                IconButton(onClick = { }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_close),
                        contentDescription = stringResource(id = string.tyres_label),
                        tint = AppTheme.colors.contentSecondary
                    )
                }
            }
        )
    }
}

@PreviewTheme
@Composable
private fun PreviewNoIcon() {
    AppThemePreview {
        Header(
            text = "2022",
            action = null,
            actionUpClicked = { }
        )
    }
}

@PreviewTheme
@Composable
private fun PreviewNoIconWithOverride() {
    AppThemePreview {
        Header(
            text = "2022",
            action = null,
            actionUpClicked = { },
            overrideIcons = {
                IconButton(onClick = { }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_close),
                        contentDescription = stringResource(id = string.tyres_label),
                        tint = AppTheme.colors.contentSecondary
                    )
                }
            }
        )
    }
}