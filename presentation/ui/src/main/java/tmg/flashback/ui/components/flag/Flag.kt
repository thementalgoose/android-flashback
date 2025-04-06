package tmg.flashback.ui.components.flag

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import com.murgupluoglu.flagkit.R.drawable
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.annotations.PreviewTheme
import tmg.flashback.ui.utils.DrawableUtils.getFlagResourceAlpha3
import tmg.flashback.ui.utils.isInPreview

@Composable
fun Flag(
    iso: String,
    modifier: Modifier = Modifier,
    nationality: String? = null,
) {
    val context = LocalContext.current
    val resourceId = when (isInPreview()) {
        true ->  drawable.gb
        false -> context.getFlagResourceAlpha3(iso)
    }

    if (resourceId != 0) {
        Image(
            modifier = modifier,
            painter = painterResource(id = resourceId),
            contentDescription = nationality,
            contentScale = ContentScale.Fit,
        )
    } else {
        Box(
            modifier = modifier
                .background(AppTheme.colors.primary.copy(alpha = 0.3f))
                .clearAndSetSemantics {
                    this.contentDescription = nationality ?: iso
                }
        )
    }
}

@PreviewTheme
@Composable
private fun Preview() {
    AppThemePreview {
        Flag("GB")
    }
}