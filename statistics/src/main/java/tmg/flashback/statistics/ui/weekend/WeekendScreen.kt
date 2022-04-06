package tmg.flashback.statistics.ui.weekend

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import tmg.flashback.formula1.model.OverviewRace
import tmg.flashback.formula1.model.Race
import tmg.flashback.formula1.model.RaceInfo
import tmg.flashback.providers.RaceProvider
import tmg.flashback.statistics.R
import tmg.flashback.statistics.ui.weekend.info.RaceInfoHeader
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview
import tmg.flashback.ui.components.layouts.ContainerCollapsing
import tmg.flashback.ui.components.layouts.Container

@Composable
fun WeekendScreen(
    model: OverviewRace,
    isCompact: Boolean,
    backClicked: (() -> Unit)? = null
) {
    WeekendScreenImpl(
        model = model.toRaceInfo(),
        isCompact = false
    )
}

@Composable
private fun WeekendScreenImpl(
    model: RaceInfo,
    isCompact: Boolean,

) {
    if (isCompact) {
        WeekendScreenCompact(
            model = model,
            tabSelected = null
        )
    } else {
        WeekendScreenEmbedded(
            model = model
        )
    }
}

@Composable
private fun WeekendScreenCompact(
    model: RaceInfo,
    tabSelected: WeekendTabs?,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier
        .verticalScroll(rememberScrollState())
    ) {
        BackBar(backClicked = { })
        RaceInfoHeader(
            model = model,
            largeTrack = false,
            modifier = Modifier.padding(horizontal = AppTheme.dimensions.paddingMedium)
        )

    }

}

@Composable
private fun WeekendScreenEmbedded(
    model: RaceInfo
) {
    Column {
        Container {
            RaceInfoHeader(
                model = model,
                largeTrack = true,
                modifier = Modifier
                    .padding(vertical = AppTheme.dimensions.paddingMedium)
            )
        }

        ContainerCollapsing(
            title = "Test",
            modifier = Modifier.padding(horizontal = AppTheme.dimensions.paddingMedium)
        ) {
            Box(Modifier
                .background(Color.Green)
                .size(100.dp)
            )
        }
    }
}

@Composable
private fun BackBar(
    backClicked: () -> Unit
) {
    IconButton(onClick = backClicked) {
        Icon(
            painter = painterResource(id = R.drawable.ic_back),
            contentDescription = stringResource(id = R.string.ab_back)
        )
    }
}

@Preview
@Composable
private fun PreviewLightCompact(
    @PreviewParameter(RaceProvider::class) race: Race
) {
    AppThemePreview(isLight = true) {
        WeekendScreenImpl(
            model = race.raceInfo,
            isCompact = true,
        )
    }
}

@Preview
@Composable
private fun PreviewDarkCompact(
    @PreviewParameter(RaceProvider::class) race: Race
) {
    AppThemePreview(isLight = false) {
        WeekendScreenImpl(
            model = race.raceInfo,
            isCompact = true,
        )
    }
}

@Preview
@Composable
private fun PreviewLightEmbedded(
    @PreviewParameter(RaceProvider::class) race: Race
) {
    AppThemePreview(isLight = true) {
        WeekendScreenImpl(
            model = race.raceInfo,
            isCompact = false,
        )
    }
}

@Preview
@Composable
private fun PreviewDarkEmbedded(
    @PreviewParameter(RaceProvider::class) race: Race
) {
    AppThemePreview(isLight = false) {
        WeekendScreenImpl(
            model = race.raceInfo,
            isCompact = false,
        )
    }
}