package tmg.flashback.style.buttons

import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview

@Composable
fun ButtonTertiary(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Button(
        modifier = modifier
            .focusable(true)
            .wrapContentHeight(Alignment.CenterVertically)
            .padding(0.dp)
            .defaultMinSize(1.dp, 1.dp),
        colors = ButtonDefaults.textButtonColors(
            backgroundColor = AppTheme.colors.backgroundTertiary,
            contentColor = AppTheme.colors.contentPrimary
        ),
        contentPadding = PaddingValues(),
        shape = RoundedCornerShape(6.dp),
        onClick = onClick
    ) {
        Text(text,
            style = AppTheme.typography.caption,
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
            onClick = { }
        )
    }
}

@Preview
@Composable
private fun PreviewDark() {
    AppThemePreview(isLight = false) {
        ButtonTertiary(
            text = "Tertiary Button",
            onClick = { }
        )
    }
}