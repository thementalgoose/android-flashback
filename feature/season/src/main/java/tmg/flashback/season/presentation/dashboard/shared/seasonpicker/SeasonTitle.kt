package tmg.flashback.season.presentation.dashboard.shared.seasonpicker

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties
import androidx.hilt.navigation.compose.hiltViewModel
import tmg.flashback.season.R
import tmg.flashback.strings.R.string
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.annotations.PreviewTheme
import tmg.flashback.style.badge.Badge
import tmg.flashback.style.badge.BadgeView
import tmg.flashback.style.text.TextBody1
import tmg.flashback.style.text.TextHeadline1
import tmg.flashback.style.text.TextHeadline1Inline
import tmg.flashback.style.text.TextTitle

@Composable
fun SeasonTitleVM(
    subtitle: String?,
    viewModel: SeasonTitleViewModel = hiltViewModel()
) {
    val seasons = viewModel.outputs.supportedSeasons.collectAsState()
    val currentSeason = viewModel.outputs.currentSeason.collectAsState()
    val newSeasonAvailable = viewModel.outputs.newSeasonAvailable.collectAsState()
    SeasonTitle(
        subtitle = subtitle,
        currentSeason = currentSeason.value,
        supportedSeasons = seasons.value,
        newSeasonAvailable = newSeasonAvailable.value,
        currentSeasonUpdated = viewModel.inputs::currentSeasonUpdate
    )
}

@Composable
fun SeasonTitle(
    subtitle: String?,
    currentSeason: Int,
    supportedSeasons: List<Int>,
    newSeasonAvailable: Boolean,
    currentSeasonUpdated: (season: Int) -> Unit
) {
    val expanded = remember { mutableStateOf(false)  }
    Column(
        modifier = Modifier
            .padding(vertical = AppTheme.dimens.medium)
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded.value = true }
                .padding(horizontal = AppTheme.dimens.medium),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextHeadline1Inline(text = currentSeason.toString())
            Spacer(Modifier.width(AppTheme.dimens.nsmall))
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = null,
                tint = AppTheme.colors.contentTertiary
            )
            if (newSeasonAvailable) {
                Spacer(Modifier.width(AppTheme.dimens.small))
                BadgeView(model = Badge(label = stringResource(id = string.dashboard_new_season_available)))
            }
            DropdownMenu(
                offset = DpOffset(AppTheme.dimens.medium, 0.dp),
                modifier = Modifier.background(AppTheme.colors.backgroundContainer),
                expanded = expanded.value,
                onDismissRequest = { expanded.value = false },
                content = {
                    supportedSeasons.forEach { season ->
                        DropdownMenuItem(
                            text = {
                                TextTitle(
                                    text = season.toString(),
                                    bold = true
                                )
                            },
                            onClick = {
                                currentSeasonUpdated(season)
                                expanded.value = false
                            }
                        )
                    }
                }
            )
        }
        if (subtitle != null) {
            TextHeadline1(
                text = subtitle,
                modifier = Modifier.padding(horizontal = AppTheme.dimens.medium)
            )
        }
    }
}

@PreviewTheme
@Composable
private fun PreviewWithNewSeason() {
    AppThemePreview {
        SeasonTitle(
            subtitle = "Subtitle",
            currentSeason = 2023,
            supportedSeasons = listOf(2023, 2024),
            newSeasonAvailable = true,
            currentSeasonUpdated = { }
        )
    }
}

@PreviewTheme
@Composable
private fun Preview() {
    AppThemePreview {
        SeasonTitle(
            subtitle = "Subtitle",
            currentSeason = 2023,
            supportedSeasons = listOf(2023, 2024),
            newSeasonAvailable = false,
            currentSeasonUpdated = { }
        )
    }
}