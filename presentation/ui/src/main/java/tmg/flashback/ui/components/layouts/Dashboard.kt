package tmg.flashback.ui.components.layouts

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import tmg.flashback.style.AppTheme
import tmg.flashback.style.utils.WindowSize
import tmg.flashback.ui.R

private val sideMenuWidth = 96.dp
private val expandedContentWidth = 400.dp

@Composable
fun Dashboard(
    windowSize: WindowSize,
    menuItems: List<DashboardMenuItem>,
    clickMenuItem: (DashboardMenuItem) -> Unit,
    menuContent: @Composable () -> Unit,
    content: @Composable (menuClicked: (() -> Unit)?) -> Unit,
    subContent: @Composable () -> Unit,
) {
    val panelsState = rememberOverlappingPanelsState(OverlappingPanelsValue.Closed)
    Scaffold(
        bottomBar = {
            if (windowSize == WindowSize.Compact) {
                BottomAppBar(backgroundColor = AppTheme.colors.backgroundNav) {
                    menuItems.forEach {
                        BottomNavigationItem(
                            selected = true,
                            onClick = { clickMenuItem(it) },
                            icon = { Icon(
                                painter = painterResource(id = it.icon),
                                contentDescription = stringResource(id = it.label)
                            ) }
                        )
                    }
                }
            }
        },
        content = {
            when (windowSize) {
                WindowSize.Compact -> {
                    OverlappingPanels(
                        panelsState = panelsState,
                        panelStart = { menuContent() },
                        panelCenter = {
                            val coroutineScope = rememberCoroutineScope()
                            content(menuClicked = {
                                coroutineScope.launch {
                                    panelsState.openStartPanel()
                                }
                            })
                        }
                    )
                }
                WindowSize.Medium -> {
                    Row(modifier = Modifier.fillMaxSize()) {
                        Box(modifier = Modifier
                            .width(sideMenuWidth)
                            .fillMaxHeight()
                            .background(AppTheme.colors.backgroundNav)
                        ) {
                            VerticalMenuBar(
                                menuItems = menuItems,
                                modifier = Modifier.align(Alignment.Center)
                            )
                        }
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight(),
                            content = {
                                content(null)
                            }
                        )
                    }
                }
                WindowSize.Expanded -> {
                    Row(modifier = Modifier.fillMaxSize()) {
                        Box(modifier = Modifier
                            .fillMaxHeight()
                            .width(sideMenuWidth)
                            .background(AppTheme.colors.backgroundNav)
                        ) {
                            VerticalMenuBar(
                                menuItems = menuItems,
                                modifier = Modifier.align(Alignment.Center)
                            )
                        }
                        Box(modifier = Modifier
                            .fillMaxHeight()
                            .width(expandedContentWidth)
                        ) {
                            content(null)
                        }
                        Box(modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                        ) {
                            subContent()
                        }
                    }
                }
            }
        }
    )
}

@Composable
private fun VerticalMenuBar(
    menuItems: List<DashboardMenuItem>,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxHeight()
    ) {
        Spacer(modifier = Modifier.height(AppTheme.dimensions.paddingMedium))
        MenuIcon(
            item = DashboardMenuItem(
                id = "menu",
                label = R.string.ab_menu,
                icon = R.drawable.ic_menu,
                isSelected = false
            ),
            backgroundColor = Color.Transparent
        )
        Spacer(modifier = Modifier.height(AppTheme.dimensions.paddingMedium))
        menuItems.forEach {
            MenuIcon(it)
            Spacer(modifier = Modifier.height(AppTheme.dimensions.paddingMedium))
        }
    }
}

@Composable
private fun MenuIcon(
    item: DashboardMenuItem,
    modifier: Modifier = Modifier,
    backgroundColor: Color = AppTheme.colors.backgroundSecondary
) {
    Box(modifier = modifier
        .size(64.dp)
        .clip(CircleShape)
        .background(backgroundColor)
    ) {
        Icon(
            modifier = Modifier
                .size(32.dp)
                .align(Alignment.Center),
            painter = painterResource(id = item.icon),
            contentDescription = stringResource(id = item.label)
        )
    }
}

@Preview
@Composable
private fun PreviewCompact() {
    Dashboard(
        windowSize = WindowSize.Compact,
        menuItems = listOf(
            DashboardMenuItem("",0, R.drawable.ic_bar_animation_slow),
            DashboardMenuItem("", 0, R.drawable.ic_bar_animation_medium),
            DashboardMenuItem("", 0, R.drawable.ic_bar_animation_quick),
        ),
        clickMenuItem = { },
        menuContent = { Box(modifier = Modifier
            .fillMaxSize()
            .background(Color.Red)) },
        content = { Box(modifier = Modifier
            .fillMaxSize()
            .background(Color.Green)) },
        subContent = { Box(modifier = Modifier
            .fillMaxSize()
            .background(Color.Blue)) },
    )
}

@Preview(device = Devices.NEXUS_7)
@Composable
private fun PreviewMedium() {
    Dashboard(
        windowSize = WindowSize.Medium,
        menuItems = listOf(
            DashboardMenuItem("",0, R.drawable.ic_bar_animation_slow),
            DashboardMenuItem("",0, R.drawable.ic_bar_animation_medium),
            DashboardMenuItem("",0, R.drawable.ic_bar_animation_quick),
        ),
        clickMenuItem = { },
        menuContent = { Box(modifier = Modifier
            .fillMaxSize()
            .background(Color.Red)) },
        content = { Box(modifier = Modifier
            .fillMaxSize()
            .background(Color.Green)) },
        subContent = { Box(modifier = Modifier
            .fillMaxSize()
            .background(Color.Blue)) },
    )
}

@Preview(device = Devices.PIXEL_C)
@Composable
private fun PreviewExpanded() {
    Dashboard(
        windowSize = WindowSize.Expanded,
        menuItems = listOf(
            DashboardMenuItem("",0, R.drawable.ic_bar_animation_slow),
            DashboardMenuItem("",0, R.drawable.ic_bar_animation_medium),
            DashboardMenuItem("",0, R.drawable.ic_bar_animation_quick),
        ),
        clickMenuItem = { },
        menuContent = { Box(modifier = Modifier
            .fillMaxSize()
            .background(Color.Red)) },
        content = { Box(modifier = Modifier
            .fillMaxSize()
            .background(Color.Green)) },
        subContent = { Box(modifier = Modifier
            .fillMaxSize()
            .background(Color.Blue)) },
    )
}