package tmg.flashback.ui.components.navigation

import androidx.annotation.DrawableRes
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomAppBar
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview

private val selectedPillWidth: Dp = 64.dp
private val pillHeight: Dp = 28.dp
private val iconSize: Dp = 24.dp
private val appBarHeight: Dp = 62.dp

private val fontSize: TextUnit = 12.sp

@Composable
fun NavigationBar(
    list: List<NavigationItem>,
    itemClicked: (NavigationItem) -> Unit,
    modifier: Modifier = Modifier,
) {
    BottomAppBar(
        modifier = modifier
            .height(appBarHeight),
        backgroundColor = AppTheme.colors.backgroundNav
    ) {
        list.forEach { item ->
            BottomNavigationItem(
                selected = item.isSelected ?: false,
                onClick = {
                    itemClicked(item)
                },
                icon = {
                    SelectedIcon(
                        isSelected = item.isSelected == true,
                        icon = item.icon
                    )
                },
                label = {
                    item.label?.let { label ->
                        Text(
                            text = stringResource(id = label),
                            fontSize = fontSize,
                            textAlign = TextAlign.Center,
                            style = AppTheme.typography.body1.copy(
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                },
                selectedContentColor = AppTheme.colors.primary,
                unselectedContentColor = AppTheme.colors.contentTertiary,
                alwaysShowLabel = true,
            )
        }
    }
}

@Composable
private fun SelectedIcon(
    isSelected: Boolean,
    @DrawableRes
    icon: Int,
    modifier: Modifier = Modifier
) {
    val selectedWidth = animateDpAsState(targetValue = when (isSelected) {
        true -> selectedPillWidth
        false -> iconSize
    })
    val backgroundColor = animateColorAsState(targetValue = when (isSelected) {
        true -> AppTheme.colors.primary.copy(alpha = 0.2f)
        false -> Color.Transparent
    })

    Box(modifier = modifier
        .width(selectedPillWidth)
        .height(pillHeight)
    ) {
        Box(modifier = Modifier
            .width(selectedWidth.value)
            .height(pillHeight)
            .offset(x = (selectedPillWidth - selectedWidth.value) / 2)
            .clip(RoundedCornerShape(pillHeight / 2))
            .background(backgroundColor.value))
        Icon(
            modifier = Modifier
                .size(iconSize)
                .align(Alignment.Center),
            painter = painterResource(id = icon),
            contentDescription = null,
        )
    }
}

@Preview
@Composable
private fun PreviewLight() {
    AppThemePreview(isLight = true) {
        NavigationBar(
            list = fakeNavigationItems,
            itemClicked = {}
        )
    }
}


@Preview
@Composable
private fun PreviewDark() {
    AppThemePreview(isLight = false) {
        NavigationBar(
            list = fakeNavigationItems,
            itemClicked = {}
        )
    }
}

