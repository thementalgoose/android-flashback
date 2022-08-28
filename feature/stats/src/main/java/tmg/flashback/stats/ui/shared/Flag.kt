package tmg.flashback.stats.ui.shared

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import tmg.flashback.formula1.utils.getFlagResourceAlpha3
import tmg.flashback.stats.R
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.annotations.PreviewTheme
import tmg.flashback.ui.utils.isInPreview

@Composable
fun Flag(
    iso: String,
    modifier: Modifier = Modifier,
    nationality: String? = null,
) {
    val resourceId = when (isInPreview()) {
        true -> R.drawable.gb
        false -> LocalContext.current.getFlagResourceAlpha3(iso)
    }

    Image(
        modifier = modifier,
        painter = painterResource(id = resourceId),
        contentDescription = nationality,
        contentScale = ContentScale.Fit,
    )
}

@PreviewTheme
@Composable
private fun Preview() {
    AppThemePreview {
        Flag("GB")
    }
}