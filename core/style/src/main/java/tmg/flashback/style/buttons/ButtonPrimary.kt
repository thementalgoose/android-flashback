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
import tmg.flashback.style.*

@Composable
fun ButtonPrimary(
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = { }
) {
    return Button(
        modifier = modifier
            .focusable(true)
            .padding(
                start = AppTheme.dimensions.paddingMedium,
                top = AppTheme.dimensions.paddingSmall,
                end = AppTheme.dimensions.paddingMedium,
                bottom = AppTheme.dimensions.paddingSmall
            ),
        colors = ButtonDefaults.textButtonColors(
            backgroundColor = FlashbackTheme.colors.primary,
            contentColor = FlashbackTheme.colors.contentPrimaryInverse
        ),
        shape = RoundedCornerShape(6.dp),
        onClick = onClick
    ) {
        Text(
            text
        )
    }
}

@Preview
@Composable
private fun PreviewLight() {
    AppThemePreview(isLight = true) {
        ButtonPrimary(
            text = "Primary Button",
            modifier = Modifier
        )
    }
}

@Preview
@Composable
private fun PreviewDark() {
    AppThemePreview(isLight = false) {
        ButtonPrimary(
            text = "Primary Button",
            modifier = Modifier
        )
    }
}