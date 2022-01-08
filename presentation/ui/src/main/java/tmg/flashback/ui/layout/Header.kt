package tmg.flashback.ui.layout

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import tmg.flashback.style.AppTheme
import tmg.flashback.style.text.TextHeadline2
import tmg.flashback.style.text.TextTitle
import tmg.flashback.ui.R
import kotlin.math.roundToInt

@Composable
fun Header(
    title: String,
    modifier: Modifier = Modifier,
    @DrawableRes
    menuIcon: Int = R.drawable.ic_menu,
    menuClicked: () -> Unit = { },
) {
    val toolbarMinHeight = AppTheme.dimensions.toolbarHeight
    val toolbarMinHeightPx = with(LocalDensity.current) { toolbarMinHeight.roundToPx().toFloat() }
    val toolbarHeight = AppTheme.dimensions.toolbarHeightExpanded
    val toolbarHeightPx = with(LocalDensity.current) { toolbarHeight.roundToPx().toFloat() }
    val toolbarOffsetHeightPx = remember { mutableStateOf(0f) }
    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {

                val delta = available.y
                val newOffset = toolbarOffsetHeightPx.value + delta
                toolbarOffsetHeightPx.value = newOffset.coerceIn(-(toolbarHeightPx - toolbarMinHeightPx), 0f)

                return Offset.Zero
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(nestedScrollConnection)
    ) {
        LazyColumn(contentPadding = PaddingValues(top = toolbarHeight)) {
            items(100) {
                TextHeadline2("I'm item $it")
            }
        }

        // Original Toolbar
        Box {

        }

        // Expanded toolbar
        Box(
            modifier = Modifier
                .offset { IntOffset(x = 0, y = toolbarOffsetHeightPx.value.roundToInt()) }
                .height(toolbarHeight)
                .fillMaxWidth()
                .background(Color.Magenta)
        ) {
            TextTitle("Toolbar offset of ${toolbarOffsetHeightPx.value.roundToInt()}")
        }
    }
}

@Preview
@Composable
private fun PreviewLight() {
    AppTheme(isLight = true) {
        Header(
            title = "2021"
        )
    }
}

@Preview
@Composable
private fun PreviewLightCollapsed() {
    AppTheme(isLight = true) {
        Header(
            title = "2021"
        )
    }
}

@Preview
@Composable
private fun PreviewDark() {
    AppTheme(isLight = false) {
        Header(
            title = "2021"
        )
    }
}