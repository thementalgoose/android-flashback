package tmg.flashback.ui.components.layouts

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
        .background(AppTheme.colors.backgroundPrimary)
        .navigationBarsPadding()
    ) {
        TextHeadline2(
            text = title,
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = AppTheme.dimens.medium,
                    end = AppTheme.dimens.medium,
                    top = AppTheme.dimens.nsmall
                )
        )
        TextBody2(
            text = subtitle,
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    vertical = AppTheme.dimens.small,
                    horizontal = AppTheme.dimens.medium
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