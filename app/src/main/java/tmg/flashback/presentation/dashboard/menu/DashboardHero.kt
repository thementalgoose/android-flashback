package tmg.flashback.presentation.dashboard.menu

import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import tmg.flashback.R
import tmg.flashback.eastereggs.model.MenuIcons
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.annotations.PreviewTheme
import tmg.flashback.style.text.ColourType
import tmg.flashback.style.text.TextCaption
import tmg.flashback.style.text.TextHeadline2WithIcon

private val MenuIcons.drawable: Int get() = when (this) {
    MenuIcons.VALENTINES_DAY -> R.drawable.ic_easteregg_valentines
    MenuIcons.EASTER -> R.drawable.ic_easteregg_easter
    MenuIcons.HALLOWEEN -> R.drawable.ic_easteregg_halloween
    MenuIcons.BONFIRE -> R.drawable.ic_easteregg_bonfire
    MenuIcons.CHRISTMAS -> R.drawable.ic_easteregg_christmas
    MenuIcons.NEW_YEARS -> R.drawable.ic_easteregg_newyears
    MenuIcons.DIWALI -> R.drawable.ic_easteregg_diwali
    MenuIcons.CHINESE_NEW_YEAR -> R.drawable.ic_easteregg_newyears
    MenuIcons.NEW_YEARS_EVE -> R.drawable.ic_easteregg_newyears
}

@Composable
internal fun DashboardHero(
    menuIcons: MenuIcons?,
    showUkraine: Boolean,
    pride: Boolean = false,
    modifier: Modifier = Modifier
) {
    Column(modifier.padding(
        top = AppTheme.dimens.xsmall,
        bottom = AppTheme.dimens.xsmall,
        end = AppTheme.dimens.nsmall
    )) {
        TextHeadline2WithIcon(
            text = stringResource(id = R.string.app_name),
            icon = menuIcons?.let { painterResource(id = it.drawable) },
            iconModifier = Modifier
                .rotate(20f)
                .size(18.dp),
            colourType = when (pride) {
                true -> ColourType.RAINBOW
                false -> ColourType.DEFAULT
            }
        )
        if (showUkraine) {
            TextCaption(
                fontStyle = FontStyle.Italic,
                text = stringResource(id = R.string.easter_egg_slava_ukraine),
                maxLines = 1
            )
        }
    }
}

@Composable
@PreviewTheme
private fun Preview(
    @PreviewParameter(DashboardHeroMenuIconsProvider::class) menuKey: MenuIcons
) {
    AppThemePreview {
        DashboardHero(
            menuIcons = menuKey,
            showUkraine = false
        )
    }
}

@Composable
@PreviewTheme
private fun PreviewUkraine() {
    AppThemePreview {
        DashboardHero(
            menuIcons = null,
            showUkraine = true
        )
    }
}

@Composable
@PreviewTheme
private fun PreviewPride() {
    AppThemePreview {
        DashboardHero(
            menuIcons = null,
            showUkraine = false,
            pride = true
        )
    }
}