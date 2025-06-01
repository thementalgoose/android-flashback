@file:OptIn(ExperimentalMaterial3Api::class)

package tmg.flashback.ui.bottomsheets

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import tmg.flashback.style.AppTheme

@Composable
fun AppBottomSheet(
    visibility: MutableState<Boolean>,
    onDismissRequest: () -> Unit = { },
    content: @Composable ColumnScope.() -> Unit,
) {
    if (visibility.value) {
        AppBottomSheet(
            onDismissRequest = {
                visibility.value = false
                onDismissRequest()
            },
            content = content
        )
    }
}

@Composable
fun AppBottomSheet(
    onDismissRequest: () -> Unit = { },
    content: @Composable ColumnScope.() -> Unit,
) {
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    ModalBottomSheet(
        sheetState = bottomSheetState,
        containerColor = AppTheme.colors.backgroundPrimary,
        onDismissRequest = {
            onDismissRequest()
        },
        content = content
    )
}