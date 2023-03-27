package tmg.flashback.style.buttons

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.FlashbackTheme
import tmg.flashback.style.annotations.PreviewTheme
import tmg.flashback.style.text.TextBody1
import tmg.flashback.style.text.TextBody2

@Composable
fun ButtonSecondary(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    return Button(
        modifier = modifier
            .focusable(true)
            .wrapContentHeight(Alignment.CenterVertically)
            .padding(0.dp)
            .defaultMinSize(1.dp, 1.dp),
        border = BorderStroke(1.dp, when (enabled) {
            true -> AppTheme.colors.primary
            false -> AppTheme.colors.primary.copy(alpha = 0.4f)
        }),
        colors = ButtonDefaults.textButtonColors(
            backgroundColor = AppTheme.colors.backgroundPrimary,
            contentColor = FlashbackTheme.colors.contentPrimary
        ),
        contentPadding = PaddingValues(),
        enabled = enabled,
        shape = CircleShape,
        onClick = onClick
    ) {
        TextBody2(
            text,
            bold = true,
            textColor = when (enabled) {
                true -> AppTheme.colors.primary
                false -> AppTheme.colors.primary.copy(alpha = 0.4f)
            },
            modifier = Modifier
                .padding(
                    start = AppTheme.dimens.medium,
                    top = AppTheme.dimens.small,
                    end = AppTheme.dimens.medium,
                    bottom = AppTheme.dimens.small
                )
        )
    }
}

@PreviewTheme
@Composable
private fun Preview() {
    AppThemePreview {
        Box(Modifier.padding(16.dp)) { 
            ButtonSecondary(
                text = "Secondary Button",
                onClick = { }
            )
        }
    }
}

@PreviewTheme
@Composable
private fun PreviewDisabled() {
    AppThemePreview {
        Box(Modifier.padding(16.dp)) {
            ButtonSecondary(
                enabled = false,
                text = "Secondary Button",
                onClick = { }
            )
        }
    }
}