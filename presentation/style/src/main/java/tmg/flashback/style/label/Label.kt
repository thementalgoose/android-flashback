package tmg.flashback.style.label

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.R
import tmg.flashback.style.annotations.PreviewTheme
import tmg.flashback.style.text.TextBody2
import tmg.flashback.style.text.TextCaption

@Composable
fun Label(
    @StringRes
    label: Int,
    modifier: Modifier = Modifier,
    @DrawableRes
    icon: Int? = null,
    iconSize: Dp = 16.dp
) {
    Label(
        label = stringResource(id = label),
        modifier = modifier,
        icon = icon,
        iconSize = iconSize
    )
}

@Composable
fun Label(
    label: String,
    modifier: Modifier = Modifier,
    @DrawableRes
    icon: Int? = null,
    iconSize: Dp = 16.dp
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(AppTheme.dimens.radiusLarge))
            .background(AppTheme.colors.backgroundSecondary)
            .padding(
                start = AppTheme.dimens.nsmall,
                end = AppTheme.dimens.nsmall,
                top = AppTheme.dimens.xsmall,
                bottom = AppTheme.dimens.xsmall
            )
    ) {
        TextBody2(
            text = label,
            bold = true
        )
        if (icon != null) {
            Spacer(modifier = Modifier.width(AppTheme.dimens.xxsmall))
            Icon(
                modifier = Modifier.size(iconSize),
                painter = painterResource(id = icon),
                contentDescription = null,
                tint = AppTheme.colors.contentTertiary
            )
        }
    }
}

@PreviewTheme
@Composable
private fun Preview() {
    AppThemePreview {
        Label(
            label = R.string.lb_playback_controls_play,
        )
    }
}

@PreviewTheme
@Composable
private fun PreviewWithIcon() {
    AppThemePreview {
        Label(
            label = R.string.lb_playback_controls_play,
            icon = R.drawable.lb_ic_play
        )
    }
}