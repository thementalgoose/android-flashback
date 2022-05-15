package tmg.flashback.stats.ui.weekend

import androidx.compose.foundation.layout.offset
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import org.koin.androidx.compose.viewModel
import tmg.flashback.style.AppTheme
import tmg.flashback.ui.components.navigation.NavigationBar
import tmg.flashback.ui.components.navigation.NavigationItem
import tmg.utilities.extensions.toEnum

data class WeekendScreenState(
    val tab: WeekendNavItem,
    val isSelected: Boolean
)

@Composable
fun WeekendScreenEmbedded(

) {

}

@Composable
fun WeekendScreen(
    
) {
    val viewModel by viewModel<WeekendViewModel>()

    val tabState = viewModel.outputs.tabs.observeAsState()
    Scaffold(
        bottomBar = {
            NavigationBar(
                list = tabState.value?.toNavigationItems() ?: emptyList(),
                itemClicked = {
                    val tab = it.id.toEnum() ?: WeekendNavItem.SCHEDULE
                    viewModel.inputs.clickTab(tab)
                }
            )
        },
        content = {

        }
    )
}

private fun List<WeekendScreenState>.toNavigationItems(): List<NavigationItem> {
    return this
        .map {
            NavigationItem(
                id = it.tab.name,
                label = it.tab.label,
                icon = it.tab.icon,
                isSelected = it.isSelected
            )
        }
}