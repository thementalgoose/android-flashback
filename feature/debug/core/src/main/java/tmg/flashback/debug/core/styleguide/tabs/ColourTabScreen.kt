package tmg.flashback.debug.core.styleguide.tabs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.annotations.PreviewTheme
import tmg.flashback.style.text.TextBody1

@Composable
internal fun ColourTabScreen() {
    val list = listOf(
        "Primary" to AppTheme.colors.primary,
        "primaryDark" to AppTheme.colors.primaryDark,
        "primaryLight" to AppTheme.colors.primaryLight,
        "accent" to AppTheme.colors.accent,
        "error" to AppTheme.colors.error,
        "contentPrimary" to AppTheme.colors.contentPrimary,
        "contentSecondary" to AppTheme.colors.contentSecondary,
        "contentTertiary" to AppTheme.colors.contentTertiary,
        "contentPrimaryInverse" to AppTheme.colors.contentPrimaryInverse,
        "contentSecondaryInverse" to AppTheme.colors.contentSecondaryInverse,
        "contentTertiaryInverse" to AppTheme.colors.contentTertiaryInverse,
        "systemStatusBarColor" to AppTheme.colors.systemStatusBarColor,
        "systemNavigationBarColor" to AppTheme.colors.systemNavigationBarColor,
        "backgroundContainer" to AppTheme.colors.backgroundContainer,
        "backgroundPrimary" to AppTheme.colors.backgroundPrimary,
        "backgroundSecondary" to AppTheme.colors.backgroundSecondary,
        "backgroundTertiary" to AppTheme.colors.backgroundTertiary,
        "backgroundPrimaryInverse" to AppTheme.colors.backgroundPrimaryInverse,
        "backgroundSecondaryInverse" to AppTheme.colors.backgroundSecondaryInverse,
        "backgroundTertiaryInverse" to AppTheme.colors.backgroundTertiaryInverse,
        "backgroundNav" to AppTheme.colors.backgroundNav,
        "backgroundSplash" to AppTheme.colors.backgroundSplash,
        "f1Podium1" to AppTheme.colors.f1Podium1,
        "f1Podium2" to AppTheme.colors.f1Podium2,
        "f1Podium3" to AppTheme.colors.f1Podium3,
        "f1DeltaPositive" to AppTheme.colors.f1DeltaPositive,
        "f1DeltaNeutral" to AppTheme.colors.f1DeltaNeutral,
        "f1DeltaNegative" to AppTheme.colors.f1DeltaNegative,
        "f1ResultsFull" to AppTheme.colors.f1ResultsFull,
        "f1ResultsNeutral" to AppTheme.colors.f1ResultsNeutral,
        "f1ResultsPartial" to AppTheme.colors.f1ResultsPartial,
        "f1ResultsUpcoming" to AppTheme.colors.f1ResultsUpcoming,
        "f1FastestSector" to AppTheme.colors.f1FastestSector,
        "f1FavouriteSeason" to AppTheme.colors.f1FavouriteSeason,
        "f1Championship" to AppTheme.colors.f1Championship,
        "f1PipeColor" to AppTheme.colors.f1PipeColor,
        "rssAdd" to AppTheme.colors.rssAdd,
        "rssRemove" to AppTheme.colors.rssRemove,
        "rssNewsBar" to AppTheme.colors.rssNewsBar,
    )

    LazyVerticalGrid(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = AppTheme.dimens.medium),
        horizontalArrangement = Arrangement.spacedBy(AppTheme.dimens.nsmall),
        verticalArrangement = Arrangement.spacedBy(AppTheme.dimens.nsmall),
        columns = GridCells.Fixed(3)
    ) {

        items(list) {
            Item(it.second, it.first)
        }
    }
}

@Composable
private fun Item(colour: Color, name: String) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Box(modifier = Modifier
            .height(100.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(6.dp))
            .background(colour)
        )
        TextBody1(
            text = name,
            modifier = Modifier
                .align(Alignment.Start)
                .padding(all = 8.dp)
        )
    }
}

@PreviewTheme
@Composable
private fun Preview() {
    AppThemePreview {
        ColourTabScreen()
    }
}