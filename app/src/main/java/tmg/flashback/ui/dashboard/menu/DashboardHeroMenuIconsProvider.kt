package tmg.flashback.ui.dashboard.menu

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import tmg.flashback.eastereggs.model.MenuIcons

/**
 * Testing for hero
 */
class DashboardHeroMenuIconsProvider: PreviewParameterProvider<MenuIcons> {
    override val values: Sequence<MenuIcons> = sequenceOf(*MenuIcons.values())
}