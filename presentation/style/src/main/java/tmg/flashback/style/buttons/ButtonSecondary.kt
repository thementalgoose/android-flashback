package tmg.flashback.style.buttons

import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.FlashbackTheme
import tmg.flashback.style.annotations.PreviewTheme

@Composable
fun ButtonSecondary(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    return Button(
        modifier = modifier
            .focusable(true),
        colors = ButtonDefaults.textButtonColors(
            backgroundColor = when (enabled) {
                true -> FlashbackTheme.colors.accent
                false -> FlashbackTheme.colors.accent.copy(alpha = 0.4f)
            },
            contentColor = FlashbackTheme.colors.contentPrimary
        ),
        enabled = enabled,
        shape = RoundedCornerShape(6.dp),
        onClick = onClick
    ) {
        Text(
            text,
            modifier = Modifier
                .padding(
                    start = AppTheme.dimens.small,
                    top = AppTheme.dimens.xsmall,
                    end = AppTheme.dimens.small,
                    bottom = AppTheme.dimens.xsmall
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