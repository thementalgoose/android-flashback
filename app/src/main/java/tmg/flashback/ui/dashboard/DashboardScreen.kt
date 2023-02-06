@file:OptIn(ExperimentalMaterial3Api::class)

package tmg.flashback.ui.dashboard

import android.annotation.SuppressLint
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScopeInstance.weight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LiveData
import tmg.flashback.style.text.TextBody1
import tmg.flashback.ui.HomeScreen
import tmg.flashback.ui.components.layouts.OverlappingPanels
import tmg.flashback.ui.components.layouts.OverlappingPanelsValue
import tmg.flashback.ui.components.layouts.rememberOverlappingPanelsState
import tmg.flashback.ui.components.navigation.NavigationBar
import tmg.flashback.ui.components.navigation.NavigationColumn
import tmg.flashback.ui.components.navigation.NavigationTimelineItem
import tmg.flashback.ui.components.navigation.appBarHeight
import tmg.flashback.ui.dashboard2.compact.DashboardNavItem
import tmg.utilities.extensions.toEnum

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun DashboardScreen(
    windowSize: WindowSizeClass,
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val panelsState = rememberOverlappingPanelsState(OverlappingPanelsValue.Closed)
    val selectedItem = viewModel.outputs.selectedItem.observeAsState(MenuItem.Calendar)
    Scaffold(
        bottomBar = {
            if (windowSize.widthSizeClass == WindowWidthSizeClass.Compact) {
                val bottomBarItems = viewModel.outputs.bottomBarItems.observeAsState(emptyList())
                val position = animateDpAsState(targetValue = if (panelsState.isStartPanelOpen) appBarHeight else 0.dp)
                NavigationBar(
                    modifier = Modifier.offset(y = position.value),
                    list = bottomBarItems.value.map { it.toNavigationItem(selectedItem == it) },
                    itemClicked = {

                    }
                )
            }
        },
        content = {
            OverlappingPanels(
                gesturesEnabled = windowSize.widthSizeClass == WindowWidthSizeClass.Compact,
                panelStart = {
                    TextBody1(text = "MENU COMPACT")
                },
                panelCenter = {
                    Row(Modifier.fillMaxSize()) {
                        if (windowSize.widthSizeClass == WindowWidthSizeClass.Medium) {
                            Box(modifier = Modifier
                                .width(72.dp)
                                .background(Color.Green)
                            ) {
                                TextBody1(text = "MENU MEDIUM")
                            }
                        }
                        if (windowSize.widthSizeClass == WindowWidthSizeClass.Expanded) {
                            Box(modifier = Modifier
                                .width(72.dp)
                                .background(Color.Green)
                            ) {
                                TextBody1(text = "MENU EXPANDED")
                            }
                        }
                        Box(modifier = Modifier.weight(1f)) {
                            Box()
                        }
                    }
                }
            )
        }
    )
}