package tmg.flashback.ui.components.loading

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.placeholder
import com.google.accompanist.placeholder.shimmer
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.annotations.PreviewTheme

@Composable
fun SkeletonViewList(
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        SkeletonView()
        SkeletonView()
        SkeletonView()
        SkeletonView()
        SkeletonView()
        SkeletonView()
    }
}

@Composable
fun SkeletonView(
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier
        .fillMaxWidth()
        .padding(
            vertical = AppTheme.dimens.small,
            horizontal = AppTheme.dimens.medium
        )
    ) {
        Box(modifier = Modifier
            .size(48.dp)
            .placeholder(
                highlight = PlaceholderHighlight.shimmer(
                    highlightColor = AppTheme.colors.backgroundSecondary
                ),
                visible = true,
                color = AppTheme.colors.backgroundTertiary,
                shape = RoundedCornerShape(4.dp)
            ))
        Spacer(Modifier.width(AppTheme.dimens.medium))
        Box(modifier = Modifier
            .height(48.dp)
            .weight(1f)
            .placeholder(
                highlight = PlaceholderHighlight.shimmer(
                    highlightColor = AppTheme.colors.backgroundSecondary
                ),
                visible = true,
                color = AppTheme.colors.backgroundTertiary,
                shape = RoundedCornerShape(4.dp)
            )
        )
    }
}

@PreviewTheme
@Composable
private fun Preview() {
    AppThemePreview {
        SkeletonView()
    }
}