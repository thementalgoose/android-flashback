package tmg.flashback.style.buttons

import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
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