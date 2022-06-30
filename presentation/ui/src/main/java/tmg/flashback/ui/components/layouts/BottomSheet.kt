package tmg.flashback.ui.components.layouts

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.annotations.PreviewTheme
import tmg.flashback.style.text.TextBody2
import tmg.flashback.style.text.TextHeadline2

@Composable
fun BottomSheet(
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
){
    Column(modifier = modifier
        .background(AppTheme.colors.backgroundContainer)
    ) {
        TextHeadline2(
            text = title,
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = AppTheme.dimensions.paddingMedium,
                    end = AppTheme.dimensions.paddingMedium,
                    top = AppTheme.dimensions.paddingNSmall
                )
        )
        TextBody2(
            text = subtitle,
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    vertical = AppTheme.dimensions.paddingSmall,
                    horizontal = AppTheme.dimensions.paddingMedium
                )
        )
        content()
    }
}

@PreviewTheme
@Composable
private fun Preview() {
    AppThemePreview {
        BottomSheet(
            title = "Bottom Sheet",
            subtitle = "See your bottom sheet content here",
            content = {
                Box(
                    Modifier
                        .size(100.dp)
                        .background(Color.Green))
            }
        )
    }
}