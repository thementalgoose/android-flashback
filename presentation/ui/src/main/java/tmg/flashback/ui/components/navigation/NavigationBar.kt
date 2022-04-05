package tmg.flashback.ui.components.navigation

import androidx.compose.material.BottomAppBar
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview

@Composable
fun NavigationBar(
    list: List<NavigationItem>,
    itemClicked: (NavigationItem) -> Unit,
    modifier: Modifier = Modifier,
) {
    BottomAppBar(
        modifier = modifier,
        backgroundColor = AppTheme.colors.backgroundNav
    ) {
        list.forEach { item ->
            BottomNavigationItem(
                selected = item.isSelected ?: false,
                onClick = {
                    itemClicked(item)
                },
                icon = {
                    Icon(painter = painterResource(id = item.icon), contentDescription = item.label?.let { stringResource(id = it) })
                },
                label = {
                    item.label?.let { label ->
                        Text(text = stringResource(id = label), fontSize = 9.sp)
                    }
                },
                selectedContentColor = AppTheme.colors.primary,
                unselectedContentColor = AppTheme.colors.contentTertiary,
                alwaysShowLabel = true,
            )
        }
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

