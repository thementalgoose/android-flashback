package tmg.flashback.ui.components.settings

import android.view.RoundedCorner
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview
import tmg.flashback.ui.R
import tmg.flashback.style.SupportedTheme
import tmg.flashback.style.text.TextBody1
import tmg.flashback.style.text.TextCaption
import tmg.flashback.ui.model.Theme

@Composable
internal fun Category(
    text: String,
    modifier: Modifier = Modifier,
    beta: Boolean = false,
) {
    Row {
        TextBody1(
            text = text,
            bold = true,
            textColor = AppTheme.colors.primary,
            modifier = modifier
                .weight(1f)
                .align(Alignment.CenterVertically)
                .padding(
                    start = AppTheme.dimensions.paddingMedium,
                    end = AppTheme.dimensions.paddingMedium,
                    top = AppTheme.dimensions.paddingXSmall,
                    bottom = AppTheme.dimensions.paddingXSmall
                )
        )
        if (beta) {
            ExperimentalLabel(
                modifier = Modifier.align(CenterVertically)
            )
        }
    }
}

@Preview
@Composable
private fun PreviewLight() {
    AppThemePreview(isLight = true) {
        Category(
            text = "Appearance",
            beta = true
        )
    }
}

@Preview
@Composable
private fun PreviewDark() {
    AppThemePreview(isLight = false) {
        Column {
            Category(
                text = "Appearance"
            )
        }
    }
}