package tmg.flashback.style.input

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.annotations.PreviewTheme

@Composable
fun InputSwitch(
    isChecked: Boolean,
    modifier: Modifier = Modifier
) {
    Switch(
        modifier = modifier,
        colors = SwitchDefaults.colors(
            checkedThumbColor = AppTheme.colors.primary,
            checkedTrackColor = AppTheme.colors.primaryDark.copy(alpha = 0.5f),
            uncheckedBorderColor = AppTheme.colors.primaryDark.copy(alpha = 0.5f),
            uncheckedThumbColor = AppTheme.colors.primaryDark.copy(alpha = 0.5f),
            uncheckedTrackColor = AppTheme.colors.backgroundTertiary
        ),
        checked = isChecked,
        onCheckedChange = null
    )
}

@PreviewTheme
@Composable
private fun Preview() {
    AppThemePreview {
        Row(Modifier.fillMaxWidth()) {
            Column(Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
                InputSwitch(isChecked = true)
            }
            Column(Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
                InputSwitch(isChecked = false)
            }
        }
    }
}