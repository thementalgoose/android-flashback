package tmg.flashback.ui.components.list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import tmg.flashback.style.AppTheme

@Composable
fun LazyColumnEdgeToEdge(
    modifier: Modifier = Modifier,
    state: LazyListState = rememberLazyListState(),
    content: LazyListScope.() -> Unit,
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(AppTheme.colors.backgroundPrimary),
        state = state,
        content = {
            item("edgetoedge-padding-top") {
                Spacer(Modifier.statusBarsPadding())
            }
            this.content()
            item("edgetoedge-padding-bottom") {
                Spacer(Modifier.navigationBarsPadding())
            }
        }
    )
}