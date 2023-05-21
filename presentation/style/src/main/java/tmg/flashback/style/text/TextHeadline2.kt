package tmg.flashback.style.text

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.R
import tmg.flashback.style.annotations.PreviewTheme

@Composable
fun TextHeadline2(
    text: String,
    modifier: Modifier = Modifier,
    maxLines: Int = Int.MAX_VALUE,
    brand: Boolean = false
) {
    Text(
        text,
        modifier = modifier
            .fillMaxWidth(),
        maxLines = maxLines,
        style = AppTheme.typography.h2.copy(
            color = when (brand) {
                true -> AppTheme.colors.primary
                false -> AppTheme.colors.contentPrimary
            }
        )
    )
}

@Composable
fun TextHeadline2WithIcon(
    text: String,
    icon: Painter?,
    modifier: Modifier = Modifier,
    iconModifier: Modifier = Modifier,
    maxLines: Int = 1,
    brand: Boolean = false
) {
    Box {
        Row {
            Text(
                text,
                modifier = modifier,
                maxLines = maxLines,
                style = AppTheme.typography.h2.copy(
                    color = when (brand) {
                        true -> AppTheme.colors.primary
                        false -> AppTheme.colors.contentPrimary
                    }
                )
            )
            Spacer(Modifier.width(10.dp))
        }
        icon?.let {
            Image(
                painter = it,
                contentDescription = null,
                modifier = iconModifier
                    .align(Alignment.TopEnd),
            )
        }
    }
}

@PreviewTheme
@Composable
private fun Preview() {
    AppThemePreview {
        TextHeadline2(
            text = "Headline 2"
        )
    }
}

@PreviewTheme
@Composable
private fun PreviewIcon() {
    AppThemePreview {
        TextHeadline2WithIcon(
            text = "Headline 2",
            icon = painterResource(id = androidx.core.R.drawable.ic_call_answer_low),
            iconModifier = Modifier.rotate(40f)
        )
    }
}

@PreviewTheme
@Composable
private fun PreviewBrand() {
    AppThemePreview {
        TextHeadline2(
            text = "Headline 2 Brand",
            brand = true
        )
    }
}