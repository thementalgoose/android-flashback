package tmg.flashback.ui.components.layouts

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.BottomAppBar
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import tmg.flashback.style.AppTheme
import tmg.flashback.style.text.TextBody1
import tmg.flashback.style.utils.WindowSize
import tmg.flashback.ui.R

private val sideMenuWidth = 72.dp
private val expandedContentWidth = 420.dp
private val expandedSlideInMenuWidth = 400.dp

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
    AppTheme {
        Scaffold(
            bottomBar = {
                if (windowSize == WindowSize.Compact) {
                    BottomAppBar(backgroundColor = AppTheme.colors.backgroundNav) {
                        menuItems.forEach {
                            BottomNavigationItem(
                                selected = true,
                                onClick = { clickMenuItem(it) },
                                icon = {
                                    Icon(
                                        painter = painterResource(id = it.icon),
                                        contentDescription = stringResource(id = it.label)
                                    )
                                },
                                alwaysShowLabel = true,
                                label = {
                                    TextBody1(text = stringResource(id = it.label))
                                }
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
                                content(
                                    menuClicked = {
                                        coroutineScope.launch {
                                            panelsState.openStartPanel()
                                        }
                                    }
                                )
                            }
                        )
                    }
                    WindowSize.Medium -> {
                        val showMenu = remember { mutableStateOf(false) }
                        Row(modifier = Modifier
                            .fillMaxSize()
                        ) {
                            Box(modifier = Modifier
                                .width(sideMenuWidth)
                                .fillMaxHeight()
                                .background(AppTheme.colors.backgroundNav)
                            ) {
                                VerticalMenuBar(
                                    menuItems = menuItems,
                                    menuClicked = {
                                        showMenu.value = !showMenu.value
                                    },
                                    menuItemClicked = clickMenuItem,
                                    modifier = Modifier.align(Alignment.Center)
                                )
                            }

                            val menuOffset = animateDpAsState(targetValue = when (showMenu.value) {
                                false -> 0.dp
                                true -> expandedSlideInMenuWidth
                            })
                            val menuFade = animateFloatAsState(targetValue = when (showMenu.value) {
                                false -> 1.0f
                                true -> 0.6f
                            })

                            Box(modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                            ) {
                                Box(modifier = Modifier
                                    .fillMaxHeight()
                                    .width(expandedSlideInMenuWidth)
                                ) {
                                    menuContent()
                                }
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .fillMaxHeight()
                                        .offset(x = menuOffset.value)
                                        .alpha(menuFade.value),
                                    content = {
                                        content(null)
                                    }
                                )
                            }
                        }
                    }
                    WindowSize.Expanded -> {
                        val showMenu = remember { mutableStateOf(false) }
                        Row(modifier = Modifier.fillMaxSize()) {
                            Box(modifier = Modifier
                                .fillMaxHeight()
                                .width(sideMenuWidth)
                                .background(AppTheme.colors.backgroundNav)
                            ) {
                                VerticalMenuBar(
                                    menuItems = menuItems,
                                    menuClicked = { showMenu.value = !showMenu.value },
                                    menuItemClicked = clickMenuItem,
                                    modifier = Modifier.align(Alignment.Center)
                                )
                            }

                            val menuOffset = animateDpAsState(targetValue = when (showMenu.value) {
                                false -> 0.dp
                                true -> expandedSlideInMenuWidth
                            })
                            val menuFade = animateFloatAsState(targetValue = when (showMenu.value) {
                                false -> 1.0f
                                true -> 0.6f
                            })

                            Box(modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                            ) {
                                Box(modifier = Modifier
                                    .fillMaxHeight()
                                    .width(expandedSlideInMenuWidth)
                                ) {
                                    menuContent()
                                }
                                Row(modifier = Modifier
                                    .offset(x = menuOffset.value)
                                    .alpha(menuFade.value)
                                ) {
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
                }
            }
        )
    }
}

private object DrawerShape: Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val leftSpaceWidth = size.width * 1 / 3
        return Outline.Rectangle(Rect(left = 0f, top = 0f, right = size.width * 2 / 3, bottom = size.height))
    }
}

private val menuItemClickWidth = 48.dp
private val menuItemIconWidth = 24.dp

@Composable
private fun VerticalMenuBar(
    menuItems: List<DashboardMenuItem>,
    menuClicked: () -> Unit,
    menuItemClicked: (DashboardMenuItem) -> Unit,
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
            onClick = menuClicked,
            backgroundColor = Color.Transparent
        )
        Spacer(modifier = Modifier.height(AppTheme.dimensions.paddingMedium))
        menuItems.forEach {
            MenuIcon(it, onClick = {
                menuItemClicked(it)
            })
            Spacer(modifier = Modifier.height(AppTheme.dimensions.paddingMedium))
        }
    }
}

@Composable
private fun MenuIcon(
    item: DashboardMenuItem,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = { },
    backgroundColor: Color = AppTheme.colors.backgroundSecondary
) {
    Box(modifier = modifier
        .size(menuItemClickWidth)
        .clip(CircleShape)
        .background(backgroundColor)
        .clickable(onClick = onClick)
    ) {
        Icon(
            modifier = Modifier
                .size(menuItemIconWidth)
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