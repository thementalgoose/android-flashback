package tmg.flashback.weekend.presentation.shared

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import tmg.flashback.style.AppTheme
import tmg.flashback.style.annotations.PreviewTheme
import tmg.flashback.style.text.TextTitle
import tmg.flashback.weekend.R

@Composable
fun CollapsibleBox(
    title: String,
    modifier: Modifier = Modifier,
    showCheckmark: Boolean = false,
    isExpanded: MutableState<Boolean> = remember { mutableStateOf(false) }
) {
    val radius = animateDpAsState(
        targetValue = if (isExpanded.value) 0.dp else AppTheme.dimens.radiusMedium,
        label = "rotation"
    )
    Column(
        modifier = modifier
            .padding(
                top = AppTheme.dimens.medium,
                start = AppTheme.dimens.medium,
                end = AppTheme.dimens.medium
            )
            .clip(
                RoundedCornerShape(
                    topStart = AppTheme.dimens.radiusMedium,
                    topEnd = AppTheme.dimens.radiusMedium,
                    bottomStart = radius.value,
                    bottomEnd = radius.value,
                )
            )
            .background(AppTheme.colors.backgroundSecondary)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    isExpanded.value = !isExpanded.value
                }
                .padding(
                    horizontal = AppTheme.dimens.medium,
                    vertical = AppTheme.dimens.small
                )
        ) {
            TextTitle(
                text = title,
                modifier = Modifier.weight(1f)
            )
            val degrees = animateFloatAsState(
                targetValue = if (isExpanded.value) 0f else 90f,
                label = "rotation"
            )
            if (showCheckmark) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_tick),
                    contentDescription = null,
                    tint = AppTheme.colors.f1ResultsFull
                )
            }
            Icon(
                modifier = Modifier.rotate(degrees.value),
                painter = painterResource(id = tmg.flashback.ui.R.drawable.arrow_down),
                contentDescription = null,
                tint = AppTheme.colors.contentSecondary
            )
        }
    }
}

@PreviewTheme
@Composable
private fun Preview() {
    CollapsibleBox(
        title = "Qualifying"
    )
}