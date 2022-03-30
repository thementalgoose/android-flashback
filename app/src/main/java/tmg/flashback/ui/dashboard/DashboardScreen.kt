package tmg.flashback.ui.dashboard

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat.startActivity
import org.koin.androidx.compose.viewModel
import tmg.flashback.formula1.model.OverviewRace
import tmg.flashback.statistics.ui.dashboard.calendar.CalendarScreen
import tmg.flashback.statistics.ui.dashboard.constructors.ConstructorsStandingScreen
import tmg.flashback.statistics.ui.dashboard.drivers.DriversStandingScreen
import tmg.flashback.statistics.ui.weekend.WeekendActivity
import tmg.flashback.statistics.ui.weekend.WeekendScreen
import tmg.flashback.style.text.TextHeadline2
import tmg.flashback.style.utils.WindowSize
import tmg.flashback.ui.components.layouts.Dashboard
import tmg.flashback.ui.dashboard.menu.MenuScreen
import tmg.utilities.extensions.toEnum

data class DashboardScreenState(
    val tab: DashboardNavItem,
    val season: Int
)

sealed class SideContentView {
    object Race : SideContentView()
    object Driver : SideContentView()
    object Constructor : SideContentView()
}

@Composable
fun DashboardScreen(
    windowSize: WindowSize,
    contextResolver: () -> Context,
) {
    val viewModel by viewModel<DashboardViewModel>()

    val tabState = viewModel.outputs.currentTab.observeAsState()
    val subContentState = viewModel.outputs.subContent.observeAsState(null)
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
                    // TODO
                    Text("Placeholder!")
                }
            } else {
                val item: DashboardScreenState = tabState.value!!
                when (item.tab) {
                    DashboardNavItem.CALENDAR -> {
                        CalendarScreen(
                            season = item.season,
                            menuClicked = menuClicked,
                            itemClicked = {
                                when (windowSize) {
                                    WindowSize.Compact,
                                    WindowSize.Medium -> {
                                        launchRaceActivity(contextResolver(), it)
                                    }
                                    WindowSize.Expanded -> {
                                        viewModel.inputs.clickRace(it)
                                    }
                                }
                            }
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
            when (subContentState.value) {
                SideContentView.Constructor -> {}
                SideContentView.Driver -> {}
                SideContentView.Race -> {
                    WeekendScreen()
                }
                null -> {
                    Box(contentAlignment = Alignment.Center) {
                        TextHeadline2("Welcome to Flashback")
                    }
                }
            }
        }
    )
}

private fun launchRaceActivity(context: Context, overview: OverviewRace) {
    val intent = WeekendActivity.intent(context)
    startActivity(context, intent, null)
}