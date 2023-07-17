package tmg.flashback.weekend.ui.shared

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.annotations.PreviewTheme
import tmg.flashback.style.text.TextBody2
import tmg.flashback.weekend.R

@Composable
fun RaceHeader(
    modifier: Modifier = Modifier,
    showPoints: Boolean,
    showStatus: Boolean
) {
    Row(modifier) {

        Box(Modifier.weight(1f))

        if (showStatus) {
            Box(
                modifier = Modifier.width(timeWidth),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_race_finishes),
                    contentDescription = stringResource(id = R.string.ab_status),
                    tint = AppTheme.colors.contentSecondary
                )
            }
        }

        if (showPoints) {
            Box(
                modifier = Modifier.width(pointsWidth),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_race_points),
                    contentDescription = stringResource(id = R.string.ab_points),
                    tint = AppTheme.colors.contentSecondary
                )
            }
        }
    }
}

@Composable
@PreviewTheme
private fun Preview() {
    AppThemePreview {
        RaceHeader(showPoints = true, showStatus = true)
    }
}