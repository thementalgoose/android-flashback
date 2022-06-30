package tmg.flashback.style.buttons

import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
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
    @DrawableRes
    icon: Int? = null,
    enabled: Boolean = false,
) {
    Button(
        modifier = modifier
            .focusable(true)
            .wrapContentHeight(Alignment.CenterVertically)
            .padding(0.dp)
            .defaultMinSize(1.dp, 1.dp),
        border = BorderStroke(0.dp, when (enabled) {
            true -> AppTheme.colors.primaryLight
            false -> AppTheme.colors.backgroundTertiary
        }),
        colors = ButtonDefaults.outlinedButtonColors(
            backgroundColor = when (enabled) {
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
                    start = AppTheme.dimensions.paddingNSmall,
                    top = AppTheme.dimensions.paddingSmall,
                    end = AppTheme.dimensions.paddingNSmall,
                    bottom = AppTheme.dimensions.paddingSmall
                )
        )
        if (icon != null) {
            Icon(
                modifier = Modifier.size(16.dp),
                painter = painterResource(id = icon),
                contentDescription = null,
                tint = AppTheme.colors.contentSecondary
            )
            Spacer(Modifier.width(AppTheme.dimensions.paddingNSmall))
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
            enabled = true,
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
            enabled = true,
            text = "Tertiary Button",
            onClick = { }
        )
    }
}