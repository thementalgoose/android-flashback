package tmg.flashback.ui.components.navigation

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.annotations.PreviewTheme
import tmg.flashback.style.text.TextBody1

private val selectedPillWidth: Dp = 64.dp
private val pillHeight: Dp = 28.dp
private val iconSize: Dp = 24.dp
val appBarHeight: Dp = 80.dp

@Composable
fun NavigationBar(
    list: List<NavigationItem>,
    itemClicked: (NavigationItem) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .height(appBarHeight)
            .shadow(elevation = 8.dp)
            .background(AppTheme.colors.backgroundNav),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        list.forEach { item ->
            Item(
                item = item,
                itemClicked = itemClicked,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun Item(
    item: NavigationItem,
    itemClicked: (NavigationItem) -> Unit,
    modifier: Modifier = Modifier
) {
    val selectedWidth = animateDpAsState(targetValue = when (item.isSelected ?: false) {
        true -> selectedPillWidth
        false -> iconSize
    })
    val backgroundColor = animateColorAsState(targetValue = when (item.isSelected ?: false) {
        true -> AppTheme.colors.primary.copy(alpha = 0.2f)
        false -> Color.Transparent
    })
    val colour = animateColorAsState(targetValue = when (item.isSelected ?: false) {
        true -> AppTheme.colors.primary
        false -> AppTheme.colors.contentPrimary
    })

    Column(
        modifier = modifier
            .clickable(onClick = { itemClicked(item) }),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(modifier = modifier
            .width(selectedPillWidth)
            .height(pillHeight)
        ) {
            Box(modifier = Modifier
                .width(selectedWidth.value)
                .height(pillHeight)
                .align(Alignment.Center)
                .offset(x = (selectedPillWidth - selectedWidth.value) / 2)
                .clip(RoundedCornerShape(pillHeight / 2))
                .background(backgroundColor.value))
            Icon(
                modifier = Modifier
                    .size(iconSize)
                    .align(Alignment.Center),
                painter = painterResource(id = item.icon),
                tint = colour.value,
                contentDescription = null,
            )
        }
        TextBody1(
            textAlign = TextAlign.Center,
            maxLines = 1,
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    bottom = 10.dp,
                    start = 2.dp,
                    end = 2.dp
                ),
            text = item.label?.let { stringResource(id = it) } ?: "",
            textColor = colour.value,
            bold = true
        )
    }
}

@PreviewTheme
@Composable
private fun Preview() {
    AppThemePreview {
        NavigationBar(
            list = fakeNavigationItems,
            itemClicked = {}
        )
    }
}