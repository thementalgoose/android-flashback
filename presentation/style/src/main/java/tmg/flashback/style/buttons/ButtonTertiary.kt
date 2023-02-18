package tmg.flashback.style.buttons

import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.R
import tmg.flashback.style.annotations.PreviewTheme
import tmg.flashback.style.text.TextBody2

@Composable
fun ButtonTertiary(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    narrow: Boolean = true,
    @DrawableRes
    icon: Int? = null,
    highlighted: Boolean = false,
) {
    Button(
        modifier = modifier
            .focusable(true)
            .wrapContentHeight(Alignment.CenterVertically)
            .padding(0.dp)
            .defaultMinSize(1.dp, 1.dp),
        border = BorderStroke(0.dp, when (highlighted) {
            true -> AppTheme.colors.primaryLight
            false -> AppTheme.colors.backgroundTertiary
        }),
        colors = ButtonDefaults.outlinedButtonColors(
            backgroundColor = when (highlighted) {
                true -> AppTheme.colors.primaryLight
                false -> AppTheme.colors.backgroundTertiary
            },
            contentColor = AppTheme.colors.contentSecondary
        ),
        contentPadding = PaddingValues(),
        shape = RoundedCornerShape(6.dp),
        onClick = onClick
    ) {
        TextBody2(
            text,
            bold = true,
            modifier = Modifier
                .padding(
                    start = AppTheme.dimens.nsmall,
                    top = if (narrow) AppTheme.dimens.small else AppTheme.dimens.medium,
                    end = AppTheme.dimens.nsmall,
                    bottom = if (narrow) AppTheme.dimens.small else AppTheme.dimens.medium
                )
        )
        if (icon != null) {
            Icon(
                modifier = Modifier.size(16.dp),
                painter = painterResource(id = icon),
                contentDescription = null,
                tint = AppTheme.colors.contentSecondary
            )
            Spacer(Modifier.width(AppTheme.dimens.nsmall))
        }
    }
}

@PreviewTheme
@Composable
private fun Preview() {
    AppThemePreview {
        ButtonTertiary(
            text = "Tertiary Button",
            onClick = { }
        )
    }
}

@PreviewTheme
@Composable
private fun PreviewEnabled() {
    AppThemePreview {
        ButtonTertiary(
            highlighted = true,
            text = "Tertiary Button",
            onClick = { }
        )
    }
}

@PreviewTheme
@Composable
private fun PreviewWithIcon() {
    AppThemePreview {
        ButtonTertiary(
            highlighted = true,
            icon = R.drawable.lb_ic_fast_forward,
            text = "Tertiary Button",
            onClick = { }
        )
    }
}