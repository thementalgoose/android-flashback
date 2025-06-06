package tmg.flashback.style.input

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.annotations.PreviewTheme

@Composable
fun InputRadio(
    isChecked: Boolean,
    modifier: Modifier = Modifier
) {
    RadioButton(
        modifier = modifier,
        colors = RadioButtonDefaults.colors(
            selectedColor = AppTheme.colors.primary,
            disabledUnselectedColor = AppTheme.colors.primaryDark.copy(alpha = 0.5f),
            unselectedColor = AppTheme.colors.primaryDark.copy(alpha = 0.5f),
            disabledSelectedColor = AppTheme.colors.backgroundTertiary
        ),
        selected = isChecked,
        onClick = null
    )
}

@PreviewTheme
@Composable
private fun Preview() {
    AppThemePreview {
        Row(Modifier.fillMaxWidth()) {
            Column(Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
                InputRadio(isChecked = true)
            }
            Column(Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
                InputRadio(isChecked = false)
            }
        }
    }
}