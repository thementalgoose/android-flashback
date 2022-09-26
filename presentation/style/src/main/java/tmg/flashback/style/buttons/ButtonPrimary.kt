package tmg.flashback.style.buttons

import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.FlashbackTheme
import tmg.flashback.style.annotations.PreviewTheme

@Composable
fun ButtonPrimary(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    return Button(
        modifier = modifier
            .focusable(true)
            .fillMaxWidth(),
        colors = ButtonDefaults.textButtonColors(
            backgroundColor = FlashbackTheme.colors.primary,
            contentColor = FlashbackTheme.colors.contentPrimaryInverse
        ),
        shape = RoundedCornerShape(6.dp),
        onClick = onClick
    ) {
        Text(
            text,
            modifier = Modifier
                .padding(
                    start = AppTheme.dimens.medium,
                    top = AppTheme.dimens.small,
                    end = AppTheme.dimens.medium,
                    bottom = AppTheme.dimens.small
                )
        )
    }
}

@PreviewTheme
@Composable
private fun Preview() {
    AppThemePreview {
        ButtonPrimary(
            text = "Primary Button",
            onClick = { }
        )
    }
}