package tmg.flashback.style.text

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.R
import tmg.flashback.style.annotations.PreviewTheme

private val RainbowColors = listOf(
    Color(0xff9c4f96),
    Color(0xffff6355),
    Color(0xfffba949),
    Color(0xffdac422),
    Color(0xff8bd448),
    Color(0xff2aa8f2)
)

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

enum class ColourType {
    DEFAULT,
    BRAND,
    RAINBOW
}

@Composable
fun TextHeadline2WithIcon(
    text: String,
    icon: Painter?,
    modifier: Modifier = Modifier,
    iconModifier: Modifier = Modifier,
    maxLines: Int = 1,
    colourType: ColourType = ColourType.DEFAULT
) {
    Box {
        Row {
            val style = when (colourType) {
                ColourType.DEFAULT -> AppTheme.typography.h2.copy(
                    color = AppTheme.colors.contentPrimary
                )
                ColourType.BRAND -> AppTheme.typography.h2.copy(
                    color = AppTheme.colors.contentPrimary
                )
                ColourType.RAINBOW -> AppTheme.typography.h2.copy(
                    brush = Brush.horizontalGradient(RainbowColors)
                )
            }
            Text(
                text,
                modifier = modifier,
                maxLines = maxLines,
                style = style
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
            iconModifier = Modifier.rotate(40f),
            colourType = ColourType.DEFAULT
        )
    }
}

@PreviewTheme
@Composable
private fun PreviewIconRainbow() {
    AppThemePreview {
        TextHeadline2WithIcon(
            text = "Headline 2",
            icon = painterResource(id = androidx.core.R.drawable.ic_call_answer_low),
            iconModifier = Modifier.rotate(40f),
            colourType = ColourType.RAINBOW
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