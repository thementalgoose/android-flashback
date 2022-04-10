package tmg.flashback.ui.dashboard

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.core.content.ContextCompat.startActivity
import org.koin.androidx.compose.viewModel
import tmg.flashback.formula1.model.OverviewRace
import tmg.flashback.statistics.ui.search.SearchActivity
import tmg.flashback.stats.ui.dashboard.calendar.CalendarScreen
import tmg.flashback.stats.ui.dashboard.constructors.ConstructorStandingsScreen
import tmg.flashback.stats.ui.dashboard.drivers.DriverStandingsScreen
import tmg.flashback.stats.ui.dashboard.drivers.DriverStandingsScreenVM
import tmg.flashback.style.text.TextHeadline2
import tmg.flashback.style.utils.WindowSize
import tmg.flashback.ui.components.layouts.Dashboard
import tmg.flashback.ui.dashboard.menu.MenuScreen
import tmg.flashback.ui.extensions.Observe
import tmg.utilities.extensions.toEnum

data class DashboardScreenState(
    val tab: DashboardNavItem,
    val season: Int
)

sealed class SideContentView {
    data class Race(
        val overviewRace: OverviewRace
    ) : SideContentView()
    object Driver : SideContentView()
    object Constructor : SideContentView()
}

@Composable
fun DashboardScreen(
    windowSize: WindowSize
) {
    val viewModel by viewModel<DashboardViewModel>()

    val context = LocalContext.current,
    Observe(viewModel.outputs.openSearch, callback = {
        startActivity(context, SearchActivity.intent(context), null)
    })

    val tabState = viewModel.outputs.currentTab.observeAsState()
//    Dashboard(
//        windowSize = windowSize,
//        menuItems = DashboardNavItem.toList(tabState.value?.tab),
//        clickMenuItem = { menuItem ->
//            menuItem.id.toEnum<DashboardNavItem>()?.let {
//                viewModel.inputs.clickTab(it)
//            }
//        },
//        menuContent = {
//            MenuScreen()
//        },
//        content = { menuClicked ->
//            if (tabState.value == null) {
//                Box {
//                    // TODO
//                    Text("Placeholder!")
//                }
//            } else {
//                val item: DashboardScreenState = tabState.value!!
//                when (item.tab) {
//                    DashboardNavItem.CALENDAR -> {
//                        CalendarScreen(
//                            season = item.season,
//                            menuClicked = menuClicked,
//                            itemClicked = {
//                                when (windowSize) {
//                                    WindowSize.Compact,
//                                    WindowSize.Medium -> {
//                                        launchRaceActivity(contextResolver(), it)
//                                    }
//                                    WindowSize.Expanded -> {
//                                        viewModel.inputs.clickRace(it)
//                                    }
//                                }
//                            }
//                        )
//                    }
//                    DashboardNavItem.DRIVERS -> {
//                        DriverStandingsScreenVM(
//                            season = item.season,
//                            menuClicked = menuClicked,
//                            showMenu = true
//                        )
//                    }
//                    DashboardNavItem.CONSTRUCTORS -> {
//                        ConstructorStandingsScreen(
//                            season = item.season,
//                            menuClicked = menuClicked
//                        )
//                    }
//                }
//            }
//        },
//        subContent = {
//            when (val result = subContentState.value) {
//                SideContentView.Constructor -> {}
//                SideContentView.Driver -> {}
//                is SideContentView.Race -> {
//                    WeekendScreen(
//                        model = result.overviewRace,
//                        isCompact = false,
//                        backClicked = null
//                    )
//                }
//                null -> {
//                    Box(contentAlignment = Alignment.Center) {
//                        TextHeadline2("Welcome to Flashback")
//                    }
//                }
//            }
//        }
//    )
}

private fun launchRaceActivity(context: Context, overview: OverviewRace) {
//    val intent = WeekendActivity.intent(context, overview)
//    startActivity(context, intent, null)
}