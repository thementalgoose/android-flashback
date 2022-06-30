package tmg.flashback.stats.ui.tyres

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import tmg.flashback.formula1.enums.SeasonTyres
import tmg.flashback.formula1.enums.Tyre
import tmg.flashback.formula1.enums.TyreLabel
import tmg.flashback.formula1.enums.getBySeason
import tmg.flashback.stats.R
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.annotations.PreviewTheme
import tmg.flashback.style.text.TextBody1
import tmg.flashback.style.text.TextHeadline2
import tmg.flashback.ui.components.layouts.BottomSheet

@Composable
fun TyreCompounds(
    season: Int,
    modifier: Modifier = Modifier
) {
    val tyres = SeasonTyres.getBySeason(season)
    BottomSheet(
        modifier = modifier,
        title = stringResource(id = R.string.tyres_list_title, season.toString()),
        subtitle = stringResource(id = R.string.tyres_list_subtitle)
    ) {
        val dry = tyres?.tyres?.filter { it.tyre.isDry } ?: emptyList()
        if (dry.isNotEmpty()) {
            Header(stringResource(id = R.string.tyres_dry_compounds))
            dry.forEach {
                TyreRow(tyreLabel = it)
            }
        }

        val wet = tyres?.tyres?.filter { !it.tyre.isDry } ?: emptyList()
        if (wet.isNotEmpty()) {
            Header(stringResource(id = R.string.tyres_wet_compounds))
            wet.forEach {
                TyreRow(tyreLabel = it)
            }
        }
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
                horizontal = AppTheme.dimensions.paddingMedium,
                vertical = AppTheme.dimensions.paddingXSmall
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
        horizontal = AppTheme.dimensions.paddingMedium,
        vertical = AppTheme.dimensions.paddingSmall
    )) {
        Image(
            modifier = Modifier.size(64.dp),
            painter = painterResource(id = tyreLabel.tyre.icon),
            contentDescription = null
        )
        Column(modifier = Modifier
            .weight(1f)
            .padding(
                horizontal = AppTheme.dimensions.paddingMedium,
                vertical = AppTheme.dimensions.paddingXSmall
            )
        ) {
            TextHeadline2(text = stringResource(id = tyreLabel.label))
            TextBody1(text = stringResource(id = R.string.tyres_size, tyreLabel.tyre.size))
        }
    }
}

@PreviewTheme
@Composable
private fun Preview() {
    AppThemePreview {
        TyreCompounds(season = 2022)
    }
}