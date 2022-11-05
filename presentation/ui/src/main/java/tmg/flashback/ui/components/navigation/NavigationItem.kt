package tmg.flashback.ui.components.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.ui.graphics.Color
import tmg.flashback.ui.R

data class NavigationItem(
    val id: String,
    @StringRes
    val label: Int?,
    @DrawableRes
    val icon: Int,
    val isSelected: Boolean? = false,
)

data class NavigationTimelineItem(
    val id: String,
    val pipeType: PipeType,
    val label: String,
    val color: Color,
    val isSelected: Boolean = false
)

internal val fakeNavigationTimelineItems: List<NavigationTimelineItem> = listOf(
    NavigationTimelineItem(
        id = "2022",
        pipeType = PipeType.START,
        label = "2022",
        color = Color.Magenta,
        isSelected = false
    ),
    NavigationTimelineItem(
        id = "2021",
        pipeType = PipeType.START_END,
        label = "2021",
        color = Color.Magenta,
        isSelected = true
    ),
    NavigationTimelineItem(
        id = "2020",
        pipeType = PipeType.END,
        label = "2020",
        color = Color.Magenta,
        isSelected = false
    )
)

internal val fakeNavigationItems: List<NavigationItem> = listOf(
    NavigationItem(
        id = "menu",
        label = R.string.ab_menu,
        icon = R.drawable.ic_nightmode_dark,
        isSelected = true
    ),
    NavigationItem(
        id = "back",
        label = R.string.ab_back,
        icon = R.drawable.ic_theme_material_you
    ),
    NavigationItem(
        id = "settings",
        label = R.string.settings_theme_title,
        icon = R.drawable.ic_nightmode_auto
    ),
    NavigationItem(
        id = "light",
        label = R.string.settings_theme_nightmode_light,
        icon = R.drawable.ic_nightmode_light
    ),
    NavigationItem(
        id = "experiment",
        label = R.string.settings_experimental,
        icon = R.drawable.ic_theme_default
    )
)