package tmg.flashback.ui.dashboard.compact.menu

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import tmg.flashback.R
import tmg.flashback.eastereggs.model.MenuKeys
import tmg.flashback.style.AppTheme
import tmg.flashback.style.text.TextHeadline2
import tmg.flashback.style.text.TextHeadline2WithIcon

private val MenuKeys.drawable: Int get() = when (this) {
    MenuKeys.VALENTINES_DAY -> R.drawable.ic_easteregg_valentines
    MenuKeys.EASTER -> R.drawable.ic_easteregg_valentines
    MenuKeys.HALLOWEEN -> R.drawable.ic_easteregg_halloween
    MenuKeys.CHRISTMAS -> R.drawable.ic_easteregg_christmas
}

@Composable
internal fun MenuScreenHero(
    menuKeys: MenuKeys?,
    modifier: Modifier = Modifier
) {
    if (menuKeys != null) {
        Box(Modifier.padding(
            vertical = AppTheme.dimens.medium,
            horizontal = AppTheme.dimens.nsmall
        )) {
            TextHeadline2WithIcon(
                text = stringResource(id = R.string.app_name),
                icon = painterResource(id = menuKeys.drawable),
                iconModifier = Modifier
                    .rotate(20f)
                    .size(18.dp)
            )
        }
    } else {
        TextHeadline2(
            text = stringResource(id = R.string.app_name),
            modifier = modifier.padding(
                vertical = AppTheme.dimens.medium,
                horizontal = AppTheme.dimens.nsmall
            )
        )
    }
}