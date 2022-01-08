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

@Composable
fun ButtonTertiary(
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = { }
) {
    return Button(
        modifier = modifier
            .focusable(true),
        colors = ButtonDefaults.textButtonColors(
            backgroundColor = FlashbackTheme.colors.backgroundTertiary,
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

@Preview
@Composable
private fun PreviewLight() {
    AppThemePreview(isLight = true) {
        ButtonTertiary(
            text = "Tertiary Button",
            modifier = Modifier
        )
    }
}

@Preview
@Composable
private fun PreviewDark() {
    AppThemePreview(isLight = false) {
        ButtonTertiary(
            text = "Tertiary Button",
            modifier = Modifier
        )
    }
}