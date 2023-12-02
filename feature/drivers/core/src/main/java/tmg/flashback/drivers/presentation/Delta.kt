package tmg.flashback.drivers.presentation

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import tmg.flashback.drivers.R
import tmg.flashback.strings.R.string
import tmg.flashback.style.AppTheme
import tmg.flashback.style.text.TextBody2
import kotlin.math.abs

@Composable
internal fun Delta(
    grid: Int?,
    finish: Int,
    modifier: Modifier = Modifier
) {
    val diff = (grid ?: 0) - finish
    when {
        diff == 0 || grid == null -> { // Equal
            Delta(
                modifier = modifier,
                diff = diff,
                color = AppTheme.colors.f1DeltaNeutral,
                icon = R.drawable.ic_pos_neutral,
                contentDescription = stringResource(id = string.ab_positions_neutral, grid ?: "unknown", finish)
            )
        }
        diff > 0 -> { // Gained
            Delta(
                modifier = modifier,
                diff = diff,
                color = AppTheme.colors.f1DeltaNegative,
                icon = R.drawable.ic_pos_up,
                contentDescription = stringResource(id = string.ab_positions_gained, grid, finish, abs(diff))
            )
        }
        else -> { // Lost
            Delta(
                modifier = modifier,
                diff = diff,
                color = AppTheme.colors.f1DeltaPositive,
                icon = R.drawable.ic_pos_down,
                contentDescription = stringResource(id = string.ab_positions_lost, grid, finish, abs(diff))
            )
        }
    }
}

@Composable
private fun Delta(
    diff: Int,
    color: Color,
    @DrawableRes
    icon: Int,
    contentDescription: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center
    ) {
        Icon(
            painter = painterResource(id = icon),
            modifier = Modifier.align(Alignment.CenterVertically),
            contentDescription = contentDescription,
            tint = color,
        )
        TextBody2(
            text = abs(diff).toString(),
            textColor = color,
            bold = true,
            modifier = Modifier.padding(
                start = AppTheme.dimens.xxsmall,
                end = AppTheme.dimens.xxsmall
            )
        )
    }
}