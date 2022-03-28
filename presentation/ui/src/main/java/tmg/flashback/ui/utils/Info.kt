package tmg.flashback.ui.utils

import androidx.compose.runtime.Composable

@Composable
fun isInPreview(): Boolean {
    return true
//    return LocalInspectionMode.current
}