package tmg.flashback.ui.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import org.koin.androidx.compose.viewModel
import tmg.flashback.statistics.ui.dashboard.calendar.CalendarScreen
import tmg.flashback.statistics.ui.dashboard.constructors.ConstructorsStandingScreen
import tmg.flashback.statistics.ui.dashboard.drivers.DriversStandingScreen
import tmg.flashback.style.utils.WindowSize
import tmg.flashback.ui.components.layouts.Dashboard
import tmg.flashback.ui.dashboard.menu.MenuScreen
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

    val tabState = viewModel.outputs.currentTab.observeAsState()
    Dashboard(
        windowSize = windowSize,
        menuItems = DashboardNavItem.toList(tabState.value?.tab),
        clickMenuItem = { menuItem ->
            menuItem.id.toEnum<DashboardNavItem>()?.let {
                viewModel.inputs.clickTab(it)
            }
        },
        menuContent = {
            MenuScreen()
        },
        content = { menuClicked ->
            if (tabState.value == null) {
                Box {
                    Text("Placeholder!")
                }
            } else {
                val item: DashboardScreenState = tabState.value!!
                when (item.tab) {
                    DashboardNavItem.CALENDAR -> {
                        CalendarScreen(
                            season = item.season,
                            menuClicked = menuClicked
                        )
                    }
                    DashboardNavItem.DRIVERS -> {
                        DriversStandingScreen(
                            season = item.season,
                            menuClicked = menuClicked
                        )
                    }
                    DashboardNavItem.CONSTRUCTORS -> {
                        ConstructorsStandingScreen(
                            season = item.season,
                            menuClicked = menuClicked
                        )
                    }
                }
            }
        },
        subContent = {

            Box(modifier = Modifier
                .fillMaxSize()
                .background(Color.Blue))
        }
    )
}