package tmg.flashback.weekend.presentation.shared

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import tmg.flashback.formula1.enums.RaceStatus.FINISHED
import tmg.flashback.formula1.extensions.pointsDisplay
import tmg.flashback.formula1.model.RaceResult
import tmg.flashback.providers.RaceRaceResultProvider
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.annotations.PreviewTheme
import tmg.flashback.style.lightColours
import tmg.flashback.style.text.TextBody1
import tmg.flashback.style.text.TextBody2
import tmg.flashback.style.text.TextTitle
import tmg.flashback.ui.components.flag.Flag
import tmg.flashback.ui.utils.pluralResource
import tmg.flashback.weekend.R
import tmg.flashback.strings.R.plurals
import tmg.flashback.strings.R.string
import tmg.flashback.formula1.R.drawable
import tmg.flashback.weekend.presentation.race.RaceModel
import tmg.utilities.extensions.ordinalAbbreviation

private val p1Height = 165.dp
private val p2Height = 120.dp
private val p3Height = 90.dp

@Composable
internal fun Podium(
    model: RaceModel.DriverPodium,
    driverClicked: (RaceResult) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier
        .height(IntrinsicSize.Min)
        .padding(
            horizontal = AppTheme.dimens.xsmall,
            vertical = AppTheme.dimens.xsmall
        )
    ) {
        Column(Modifier.weight(1f), verticalArrangement = Arrangement.Bottom) {
            PodiumResult(
                model = model.p2,
                modifier = Modifier.padding(
                    top = p1Height - p2Height
                ),
                driverClicked = driverClicked
            )
            PodiumBar(
                position = 2,
                points = model.p2.points,
                height = p2Height,
                color = AppTheme.colors.f1Podium2,
                grid = model.p2.grid,
                fastestLap = model.p2.fastestLap?.rank == 1,
                finish = model.p2.finish
            )
        }
        Column(Modifier.weight(1f), verticalArrangement = Arrangement.Bottom) {
            PodiumResult(
                model = model.p1,
                driverClicked = driverClicked
            )
            PodiumBar(
                position = 1,
                points = model.p1.points,
                height = p1Height,
                color = AppTheme.colors.f1Podium1,
                grid = model.p1.grid,
                fastestLap = model.p1.fastestLap?.rank == 1,
                finish = model.p1.finish
            )
        }
        Column(Modifier.weight(1f), verticalArrangement = Arrangement.Bottom) {
            PodiumResult(
                driverClicked = driverClicked,
                model = model.p3,
                modifier = Modifier.padding(
                    top = p1Height - p3Height
                )
            )
            PodiumBar(
                position = 3,
                points = model.p3.points,
                height = p3Height,
                color = AppTheme.colors.f1Podium3,
                grid = model.p3.grid,
                fastestLap = model.p3.fastestLap?.rank == 1,
                finish = model.p3.finish
            )
        }
    }
}

@Composable
private fun PodiumBar(
    position: Int,
    points: Double,
    height: Dp,
    color: Color,
    grid: Int?,
    finish: Int,
    fastestLap: Boolean,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            .padding(AppTheme.dimens.small)
            .clip(RoundedCornerShape(AppTheme.dimens.radiusSmall))
            .background(color)
    ) {
        Column(Modifier.align(Alignment.TopCenter)) {
            Box(modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = AppTheme.dimens.small,
                    start = AppTheme.dimens.small,
                    end = AppTheme.dimens.small
                ),
            ) {
                TextBody1(
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    textColor = lightColours.contentPrimary,
                    text = position.ordinalAbbreviation,
                    bold = true
                )
                if (fastestLap) {
                    FastestLap(Modifier.align(Alignment.CenterEnd))
                }
            }
            TextBody1(
                textAlign = TextAlign.Center,
                textColor = lightColours.contentPrimary,
                text = pluralResource(resId = plurals.race_points, quantity = points.toInt(), points.pointsDisplay()),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = AppTheme.dimens.xsmall)
            )
        }
    }
}


@Composable
internal fun FastestLap(
    modifier: Modifier = Modifier
) {
    Icon(
        painter = painterResource(id = drawable.ic_fastest_lap),
        contentDescription = stringResource(id = string.ab_fastest_lap),
        modifier = modifier
            .padding(top = 4.dp)
            .size(14.dp),
        tint = AppTheme.colors.f1FastestSector
    )
}

@Composable
private fun PodiumResult(
    model: RaceResult,
    driverClicked: (RaceResult) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            Modifier
                .size(80.dp)
                .clip(RoundedCornerShape(AppTheme.dimens.radiusSmall))
                .align(Alignment.CenterHorizontally)
                .background(model.entry.constructor.colour)
                .clickable(onClick = { driverClicked(model) })
        ) {
            AsyncImage(
                modifier = Modifier
                    .padding(4.dp)
                    .clip(RoundedCornerShape(AppTheme.dimens.radiusSmall)),
                contentScale = ContentScale.Crop,
                model = model.entry.driver.photoUrl,
                contentDescription = null,
                error = painterResource(id = tmg.flashback.ui.R.drawable.unknown_avatar)
            )
        }
        TextTitle(
            text = model.entry.driver.firstName,
            bold = true,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = AppTheme.dimens.small,
                    start = 2.dp,
                    end = 2.dp
                )
        )
        TextTitle(
            text = model.entry.driver.lastName,
            bold = true,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = AppTheme.dimens.xxsmall,
                    start = 2.dp,
                    end = 2.dp
                )
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = AppTheme.dimens.xsmall,
                    end = AppTheme.dimens.xsmall,
                    start = AppTheme.dimens.xsmall
                ),
            horizontalArrangement = Arrangement.Center
        ) {
            Flag(
                iso = model.entry.driver.nationalityISO,
                nationality = model.entry.driver.nationality,
                modifier = Modifier
                    .size(16.dp)
                    .align(Alignment.CenterVertically),
            )
            Spacer(Modifier.width(AppTheme.dimens.xsmall))
            Delta(grid = model.grid, finish = model.finish)
            Spacer(Modifier.width(AppTheme.dimens.xxsmall))
            TextBody2(text = model.entry.constructor.name)
        }
        TextBody2(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = AppTheme.dimens.xsmall,
                    end = AppTheme.dimens.xsmall,
                    start = AppTheme.dimens.xsmall
                ),
            textAlign = TextAlign.Center,
            text = when (model.finish == 1) {
                true -> model.time?.time ?: FINISHED.label
                false -> model.time?.time?.let { "+${it}" } ?: FINISHED.label
            }
        )
    }
}

@PreviewTheme
@Composable
private fun Preview(
    @PreviewParameter(RaceRaceResultProvider::class) result: RaceResult
) {
    AppThemePreview {
        Podium(
            RaceModel.DriverPodium(
                p1 = result,
                p2 = result,
                p3 = result
            ),
            driverClicked = { }
        )
    }
}