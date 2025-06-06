package tmg.flashback.ui.components.now

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.annotations.PreviewTheme
import tmg.flashback.ui.R

@Composable
fun Now(
    modifier: Modifier = Modifier
) {
    Icon(
        modifier = modifier
            .size(12.dp)
            .alpha(0.4f),
        painter = painterResource(id = R.drawable.ic_current_indicator),
        contentDescription = null,
        tint = AppTheme.colors.contentPrimary
    )
}

@PreviewTheme
@Composable
private fun Preview() {
    AppThemePreview {
        Now()
    }
}