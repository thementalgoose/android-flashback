@file:OptIn(ExperimentalMaterial3WindowSizeClassApi::class)

package tmg.flashback.ui.components.layouts

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import tmg.flashback.style.AppTheme
import tmg.flashback.style.text.TextBody1
import tmg.flashback.ui.R
import tmg.flashback.ui.components.navigation.NavigationItem

private val sideMenuWidth = 72.dp
private val expandedContentWidth = 420.dp
private val expandedSlideInMenuWidth = 400.dp

@Composable
fun Dashboard(
    windowSize: WindowSizeClass,
    drawerMenuItems: List<NavigationItem>,
    bottombarMenuItems: List<NavigationItem>,
    bottombarSuppress: Boolean,
    expandedMenuItems: List<NavigationItem> = bottombarMenuItems + drawerMenuItems,
    clickMenuItem: (NavigationItem) -> Unit,
    content: @Composable () -> Unit,
) {
    val panelsState = rememberOverlappingPanelsState(OverlappingPanelsValue.Closed)
    AppTheme {
        Scaffold(
            bottomBar = {

                if (windowSize.widthSizeClass == WindowWidthSizeClass.Compact) {
                    BottomAppBar(backgroundColor = AppTheme.colors.backgroundNav) {
                        menuItems.forEach {
                            BottomNavigationItem(
                                selected = true,
                                onClick = { clickMenuItem(it) },
                                icon = {
                                    Icon(
                                        painter = painterResource(id = it.icon),
                                        contentDescription = it.label?.let { stringResource(id = it) }
                                    )
                                },
                                alwaysShowLabel = true,
                                label = {
                                    TextBody1(text = it.label?.let { stringResource(id = it) } ?: "")
                                }
                            )
                        }
                    }
                }
            },
            content = {
                when (windowSize.widthSizeClass) {
                    WindowWidthSizeClass.Compact -> {
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
                    WindowWidthSizeClass.Medium -> {
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
                    else -> {
                        val showMenu = remember { mutableStateOf(false) }
                        Row(modifier = Modifier.fillMaxSize()) {
                            Box(
                                modifier = Modifier
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

                            Row(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxHeight()
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxHeight()
                                        .width(expandedContentWidth)
                                ) {
                                    content(null)
                                }
                                Box(
                                    modifier = Modifier
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
        )
    }
}

private val menuItemClickWidth = 48.dp
private val menuItemIconWidth = 24.dp

@Composable
private fun VerticalMenuBar(
    menuItems: List<NavigationItem>,
    menuClicked: () -> Unit,
    menuItemClicked: (NavigationItem) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxHeight()
    ) {
        Spacer(modifier = Modifier.height(AppTheme.dimens.medium))
        MenuIcon(
            item = NavigationItem(
                id = "menu",
                label = R.string.ab_menu,
                icon = R.drawable.ic_menu,
                isSelected = false
            ),
            onClick = menuClicked,
            backgroundColor = Color.Transparent
        )
        Spacer(modifier = Modifier.height(AppTheme.dimens.medium))
        menuItems.forEach {
            MenuIcon(it, onClick = {
                menuItemClicked(it)
            })
            Spacer(modifier = Modifier.height(AppTheme.dimens.medium))
        }
    }
}

@Composable
private fun MenuIcon(
    item: NavigationItem,
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
            contentDescription = item.label?.let { stringResource(id = it) }
        )
    }
}

@Preview
@Composable
private fun PreviewCompact() {
    Dashboard(
        windowSize = WindowSizeClass.calculateFromSize(DpSize(400.dp, 500.dp)),
        menuItems = listOf(
            NavigationItem("",0, R.drawable.ic_nightmode_auto),
            NavigationItem("", 0, R.drawable.ic_nightmode_light),
            NavigationItem("", 0, R.drawable.ic_nightmode_dark),
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
        windowSize = WindowSizeClass.calculateFromSize(DpSize(640.dp, 500.dp)),
        menuItems = listOf(
            NavigationItem("",0, R.drawable.ic_nightmode_auto),
            NavigationItem("", 0, R.drawable.ic_nightmode_light),
            NavigationItem("", 0, R.drawable.ic_nightmode_dark),
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
        windowSize = WindowSizeClass.calculateFromSize(DpSize(900.dp, 500.dp)),
        menuItems = listOf(
            NavigationItem("",0, R.drawable.ic_nightmode_auto),
            NavigationItem("", 0, R.drawable.ic_nightmode_light),
            NavigationItem("", 0, R.drawable.ic_nightmode_dark),
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