package tmg.flashback.style.buttons

import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
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
) {
    return Button(
        modifier = modifier
            .focusable(true),
        colors = ButtonDefaults.textButtonColors(
            backgroundColor = FlashbackTheme.colors.accent,
            contentColor = FlashbackTheme.colors.contentPrimary
        ),
        shape = RoundedCornerShape(6.dp),
        onClick = onClick
    ) {
        Text(
            text,
            modifier = Modifier
                .padding(
                    start = AppTheme.dimensions.paddingSmall,
                    top = AppTheme.dimensions.paddingXSmall,
                    end = AppTheme.dimensions.paddingSmall,
                    bottom = AppTheme.dimensions.paddingXSmall
                )
        )
    }
}

@PreviewTheme
@Composable
private fun Preview() {
    AppThemePreview {
        ButtonSecondary(
            text = "Secondary Button",
            onClick = { }
        )
    }
}