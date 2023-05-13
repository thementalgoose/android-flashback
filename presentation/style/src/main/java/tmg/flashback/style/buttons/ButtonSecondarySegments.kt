package tmg.flashback.style.buttons

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.FlashbackTheme
import tmg.flashback.style.R
import tmg.flashback.style.annotations.PreviewTheme
import tmg.flashback.style.text.TextBody2

private val colour: Color
    @Composable
    get() = AppTheme.colors.contentPrimary

@Composable
fun ButtonSecondarySegments(
    items: List<Int>,
    selected: Int?,
    onClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
    showTick: Boolean = false,
    enabled: Boolean = true
) {
    Row(modifier = modifier) {
        items.forEachIndexed { index, item ->
            val selected = item == selected
            Button(
                modifier = Modifier
                    .focusable(true)
                    .wrapContentHeight(Alignment.CenterVertically)
                    .padding(0.dp)
                    .weight(1f)
                    .defaultMinSize(1.dp, 1.dp),
                border = BorderStroke(1.dp, when (enabled) {
                    true -> colour
                    false -> colour.copy(alpha = 0.4f)
                }),
                colors = ButtonDefaults.textButtonColors(
                    backgroundColor = when (selected) {
                        true -> AppTheme.colors.backgroundTertiary
                        false -> AppTheme.colors.backgroundPrimary
                    },
                    contentColor = FlashbackTheme.colors.contentPrimary
                ),
                contentPadding = PaddingValues(0.dp),
                enabled = enabled,
                shape = when (index) {
                    0 -> RoundedCornerShape(topStart = 100.dp, bottomStart = 100.dp)
                    items.size - 1 -> RoundedCornerShape(topEnd = 100.dp, bottomEnd = 100.dp)
                    else -> RoundedCornerShape(0.dp)
                },
                onClick = {
                    onClick(item)
                }
            ) {
                Row(Modifier.padding(
                    start = AppTheme.dimens.nsmall,
                    end = AppTheme.dimens.nsmall,
                    top = AppTheme.dimens.small,
                    bottom = AppTheme.dimens.small
                )) {
                    TextBody2(
                        stringResource(id = item),
                        maxLines = 1,
                        bold = true,
                        textColor = when (enabled) {
                            true -> colour
                            false -> colour.copy(alpha = 0.4f)
                        },
                        modifier = Modifier
                    )
                    if (showTick) {
                        AnimatedVisibility(visible = selected) {
                            Row {
                                Spacer(Modifier.width(AppTheme.dimens.small))
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    tint = colour,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@PreviewTheme
@Composable
private fun Preview() {
    AppThemePreview {
        ButtonSecondarySegments(
            items = listOf(R.string.lb_playback_controls_play, R.string.lb_playback_controls_pause, R.string.lb_playback_controls_fast_forward),
            selected = R.string.lb_playback_controls_play,
            onClick = { }
        )
    }
}

@PreviewTheme
@Composable
private fun PreviewMany() {
    AppThemePreview {
        ButtonSecondarySegments(
            items = listOf(R.string.lb_playback_controls_play, R.string.lb_playback_controls_pause, R.string.lb_playback_controls_fast_forward),
            selected = R.string.lb_playback_controls_play,
            onClick = { }
        )
    }
}

@PreviewTheme
@Composable
private fun PreviewShowTick() {
    AppThemePreview {
        ButtonSecondarySegments(
            items = listOf(R.string.lb_playback_controls_play, R.string.lb_playback_controls_pause, R.string.lb_playback_controls_fast_forward),
            selected = R.string.lb_playback_controls_play,
            onClick = { },
            showTick = true
        )
    }
}