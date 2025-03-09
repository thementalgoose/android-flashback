package tmg.flashback.season.presentation.tyres

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.rememberNestedScrollInteropConnection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import tmg.flashback.googleanalytics.constants.AnalyticsConstants.analyticsSeason
import tmg.flashback.formula1.enums.SeasonTyres
import tmg.flashback.formula1.enums.TyreLabel
import tmg.flashback.formula1.enums.getBySeason
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.annotations.PreviewTheme
import tmg.flashback.style.text.TextBody1
import tmg.flashback.style.text.TextHeadline2
import tmg.flashback.googleanalytics.presentation.ScreenView
import tmg.flashback.strings.R.string
import tmg.flashback.ui.components.layouts.BottomSheetContainer

@Composable
fun TyreCompounds(
    season: Int,
    modifier: Modifier = Modifier,
    actionUpClicked: (() -> Unit)? = null,
) {
    ScreenView(screenName = "Tyre Compounds", args = mapOf(
        analyticsSeason to season.toString()
    ))

    val tyres = SeasonTyres.getBySeason(season)
    BottomSheetContainer(
        modifier = modifier.background(AppTheme.colors.backgroundPrimary),
        title = stringResource(id = string.tyres_list_title, season.toString()),
        subtitle = stringResource(id = string.tyres_list_subtitle),
        backClicked = actionUpClicked
    ) {
        val dry = tyres?.tyres?.filter { it.tyre.isDry } ?: emptyList()
        val wet = tyres?.tyres?.filter { !it.tyre.isDry } ?: emptyList()
        LazyColumn(
            modifier = Modifier.nestedScroll(rememberNestedScrollInteropConnection()),
            content = {
                if (dry.isNotEmpty()) {
                    item(key = "title_dry") {
                        Header(stringResource(id = string.tyres_dry_compounds))
                    }
                }
                items(dry, key = { it.tyre.name }) {
                    TyreRow(tyreLabel = it)
                }
                if (wet.isNotEmpty()) {
                    item(key = "title_wet") {
                        Header(stringResource(id = string.tyres_wet_compounds))
                    }
                }
                items(wet, key = { it.tyre.name }) {
                    TyreRow(tyreLabel = it)
                }
            }
        )
    }
}

@Composable
private fun Header(
    title: String,
    modifier: Modifier = Modifier
) {
    TextBody1(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                horizontal = AppTheme.dimens.medium,
                vertical = AppTheme.dimens.xsmall
            ),
        text = title
    )
}

@Composable
private fun TyreRow(
    tyreLabel: TyreLabel,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier.padding(
        horizontal = AppTheme.dimens.medium,
        vertical = AppTheme.dimens.small
    )) {
        Image(
            modifier = Modifier.size(64.dp),
            painter = painterResource(id = tyreLabel.tyre.icon),
            contentDescription = null
        )
        Column(modifier = Modifier
            .weight(1f)
            .padding(
                horizontal = AppTheme.dimens.medium,
                vertical = AppTheme.dimens.xsmall
            )
        ) {
            TextHeadline2(text = stringResource(id = tyreLabel.label))
            TextBody1(text = stringResource(id = string.tyres_size, tyreLabel.tyre.size))
        }
    }
}

@PreviewTheme
@Composable
private fun Preview() {
    AppThemePreview {
        TyreCompounds(
            season = 2022,
            actionUpClicked = { }
        )
    }
}