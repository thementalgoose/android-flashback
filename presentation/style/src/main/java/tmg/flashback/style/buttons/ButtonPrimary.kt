package tmg.flashback.style.buttons

import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.fillMaxWidth
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
fun ButtonPrimary(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    return Button(
        modifier = modifier
            .focusable(true)
            .fillMaxWidth(),
        colors = ButtonDefaults.textButtonColors(
            backgroundColor = FlashbackTheme.colors.primary,
            contentColor = FlashbackTheme.colors.contentPrimaryInverse
        ),
        shape = RoundedCornerShape(6.dp),
        onClick = onClick
    ) {
        Text(
            text,
            modifier = Modifier
                .padding(
                    start = AppTheme.dimensions.paddingMedium,
                    top = AppTheme.dimensions.paddingSmall,
                    end = AppTheme.dimensions.paddingMedium,
                    bottom = AppTheme.dimensions.paddingSmall
                )
        )
    }
}

@PreviewTheme
@Composable
private fun Preview() {
    AppThemePreview {
        ButtonPrimary(
            text = "Primary Button",
            onClick = { }
        )
    }
}