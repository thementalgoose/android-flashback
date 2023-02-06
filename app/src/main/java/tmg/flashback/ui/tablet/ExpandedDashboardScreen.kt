package tmg.flashback.ui.tablet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import tmg.flashback.style.AppTheme
import tmg.flashback.ui.dashboard.MenuSeasonItem
import tmg.flashback.ui.dashboard2.compact.menu.MenuViewModel

@Composable
fun ExpandedContainerVM(
    showSeasonPicker: Boolean
) {
    val viewModel = hiltViewModel<MenuViewModel>()

//    ExpandedContainer(
////        seasons = showSeasonPicker
//    )
}

@Composable
fun ExpandedContainer(
    seasons: List<MenuSeasonItem>,
    seasonClicked: (Int) -> Unit
) {

    Column(
        Modifier
            .fillMaxSize()
            .background(AppTheme.colors.backgroundPrimary)
    ) {
//        NavigationColumn(
//            list = ,
//            itemClicked = {
//
//            },
//            timelineList = seasons.map {
//                NavigationTimelineItem(
//                    id = it.season.toString(),
//                    pipeType = when {
//                        it.isFirst -> PipeType.START
//                        it.isLast -> PipeType.END
//                        else -> PipeType.START_END
//                    },
//                    label = it.season.toString(),
//                    color = it.colour,
//                    isSelected = it.isSelected,
//                )
//            },
//            timelineItemClicked = {
//                seasonClicked(it.label.toInt())
//            }
//        )
    }
}