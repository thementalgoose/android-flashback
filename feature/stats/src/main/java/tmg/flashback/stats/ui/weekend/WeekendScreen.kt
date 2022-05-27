package tmg.flashback.stats.ui.weekend

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import org.koin.androidx.compose.viewModel
import tmg.flashback.stats.ui.weekend.constructor.ConstructorScreenVM
import tmg.flashback.stats.ui.weekend.qualifying.QualifyingScreenVM
import tmg.flashback.stats.ui.weekend.race.RaceScreenVM
import tmg.flashback.stats.ui.weekend.schedule.ScheduleScreenVM
import tmg.flashback.stats.ui.weekend.sprint.SprintScreenVM
import tmg.flashback.style.AppTheme
import tmg.flashback.ui.components.loading.Fade
import tmg.flashback.ui.components.navigation.NavigationBar
import tmg.flashback.ui.components.navigation.NavigationItem
import tmg.utilities.extensions.toEnum

data class WeekendScreenState(
    val tab: WeekendNavItem,
    val isSelected: Boolean
)

@Composable
fun WeekendScreenEmbedded(
    weekendInfo: WeekendInfo
) {

}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun WeekendScreen(
    weekendInfo: WeekendInfo,
    actionUpClicked: () -> Unit,
) {
    val viewModel by viewModel<WeekendViewModel>()

    val tabState = viewModel.outputs.tabs.observeAsState(listOf(
        WeekendScreenState(tab = WeekendNavItem.SCHEDULE, isSelected = true),
        WeekendScreenState(tab = WeekendNavItem.QUALIFYING, isSelected = false),
        WeekendScreenState(tab = WeekendNavItem.RACE, isSelected = false),
        WeekendScreenState(tab = WeekendNavItem.CONSTRUCTOR, isSelected = false)
    ))
    Scaffold(
        bottomBar = {
            NavigationBar(
                list = tabState.value.toNavigationItems(),
                itemClicked = {
                    val tab = it.id.toEnum() ?: WeekendNavItem.SCHEDULE
                    viewModel.inputs.clickTab(tab)
                }
            )
        },
        content = {

            // Background box
            Box(modifier = Modifier
                .fillMaxSize()
                .background(AppTheme.colors.backgroundPrimary)
            )

            for (x in tabState.value) {
                when (x.tab) {
                    WeekendNavItem.SCHEDULE -> {
                        Fade(visible = x.isSelected) {
                            ScheduleScreenVM(
                                info = weekendInfo,
                                actionUpClicked = actionUpClicked
                            )
                        }
                    }
                    WeekendNavItem.QUALIFYING -> {
                        Fade(visible = x.isSelected) {
                            QualifyingScreenVM(
                                info = weekendInfo,
                                actionUpClicked = actionUpClicked
                            )
                        }
                    }
                    WeekendNavItem.SPRINT -> {
                        Fade(visible = x.isSelected) {
                            SprintScreenVM(
                                info = weekendInfo,
                                actionUpClicked = actionUpClicked
                            )
                        }
                    }
                    WeekendNavItem.RACE -> {
                        Fade(visible = x.isSelected) {
                            RaceScreenVM(
                                info = weekendInfo,
                                actionUpClicked = actionUpClicked
                            )
                        }
                    }
                    WeekendNavItem.CONSTRUCTOR -> {
                        Fade(visible = x.isSelected) {
                            ConstructorScreenVM(
                                info = weekendInfo,
                                actionUpClicked = actionUpClicked
                            )
                        }
                    }
                }
            }
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