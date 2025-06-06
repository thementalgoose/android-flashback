package tmg.flashback.style.buttons

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.FlashbackTheme
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
            val background = when (selected) {
                true -> AppTheme.colors.backgroundSecondary
                false -> AppTheme.colors.backgroundPrimary
            }
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
                    containerColor = background,
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
                        bold = enabled,
                        textColor = when (enabled) {
                            true -> colour
                            false -> colour.copy(alpha = 0.4f)
                        },
                        modifier = Modifier
                    )
                    if (showTick) {
                        Box(Modifier.height(16.dp).width(0.dp))
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
            items = listOf(androidx.appcompat.R.string.abc_action_bar_up_description, androidx.appcompat.R.string.abc_action_bar_up_description, androidx.appcompat.R.string.abc_action_bar_up_description),
            selected = androidx.appcompat.R.string.abc_action_bar_up_description,
            onClick = { }
        )
    }
}

@PreviewTheme
@Composable
private fun PreviewMany() {
    AppThemePreview {
        ButtonSecondarySegments(
            items = listOf(androidx.appcompat.R.string.abc_action_bar_up_description, androidx.appcompat.R.string.abc_action_bar_up_description, androidx.appcompat.R.string.abc_action_bar_up_description),
            selected = androidx.appcompat.R.string.abc_action_bar_up_description,
            onClick = { }
        )
    }
}

@PreviewTheme
@Composable
private fun PreviewShowTick() {
    AppThemePreview {
        ButtonSecondarySegments(
            items = listOf(androidx.appcompat.R.string.abc_action_bar_up_description, androidx.appcompat.R.string.abc_action_bar_up_description, androidx.appcompat.R.string.abc_action_bar_up_description),
            selected = androidx.appcompat.R.string.abc_action_bar_up_description,
            onClick = { },
            showTick = true
        )
    }
}