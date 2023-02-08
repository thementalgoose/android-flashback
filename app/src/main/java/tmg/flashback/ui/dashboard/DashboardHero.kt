package tmg.flashback.ui.dashboard

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import tmg.flashback.R
import tmg.flashback.eastereggs.model.MenuIcons
import tmg.flashback.style.AppTheme
import tmg.flashback.style.annotations.PreviewTheme
import tmg.flashback.style.text.TextHeadline2
import tmg.flashback.style.text.TextHeadline2WithIcon

private val MenuIcons.drawable: Int get() = when (this) {
    MenuIcons.VALENTINES_DAY -> R.drawable.ic_easteregg_valentines
    MenuIcons.EASTER -> R.drawable.ic_easteregg_easter
    MenuIcons.HALLOWEEN -> R.drawable.ic_easteregg_halloween
    MenuIcons.BONFIRE -> R.drawable.ic_easteregg_bonfire
    MenuIcons.CHRISTMAS -> R.drawable.ic_easteregg_christmas
    MenuIcons.NEW_YEARS -> R.drawable.ic_easteregg_newyears
    MenuIcons.CHINESE_NEW_YEAR -> R.drawable.ic_easteregg_newyears
    MenuIcons.NEW_YEARS_EVE -> R.drawable.ic_easteregg_newyears
}

@Composable
internal fun DashboardHero(
    menuIcons: MenuIcons?,
    modifier: Modifier = Modifier
) {
    if (menuIcons != null) {
        Box(
            Modifier.padding(
            vertical = AppTheme.dimens.medium,
            horizontal = AppTheme.dimens.nsmall
        )) {
            TextHeadline2WithIcon(
                text = stringResource(id = R.string.app_name),
                icon = painterResource(id = menuIcons.drawable),
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

@Composable
@PreviewTheme
private fun Preview(
    @PreviewParameter(MenuIconsProvider::class) menuKey: MenuIcons
) {
    DashboardHero(menuIcons = menuKey)
}

@Composable
@PreviewTheme
private fun PreviewTheme() {
    DashboardHero(null)
}

class MenuIconsProvider: PreviewParameterProvider<MenuIcons> {
    override val values: Sequence<MenuIcons> = MenuIcons.values().asSequence()
}