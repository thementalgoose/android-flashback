package tmg.flashback.ui.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import org.koin.androidx.compose.viewModel
import tmg.flashback.style.utils.WindowSize
import tmg.flashback.ui.components.layouts.Dashboard
import tmg.utilities.extensions.toEnum

data class DashboardScreenState(
    val tab: DashboardNavItem,
    val season: Int
)

@Composable
fun DashboardScreen(
    windowSize: WindowSize
) {
    val viewModel by viewModel<DashboardViewModel>()

    val state = viewModel.outputs.currentTab.observeAsState()
    Dashboard(
        windowSize = windowSize,
        menuItems = DashboardNavItem.toList(state.value?.tab),
        clickMenuItem = { menuItem ->
            menuItem.id.toEnum<DashboardNavItem>()?.let {
                viewModel.inputs.clickTab(it)
            }
        },
        menuContent = {
            Box(modifier = Modifier.fillMaxSize().background(Color.Red))
        },
        content = {
            Box(modifier = Modifier.fillMaxSize().background(Color.Green))
        },
        subContent = {
            Box(modifier = Modifier.fillMaxSize().background(Color.Blue))
        }
    )
}