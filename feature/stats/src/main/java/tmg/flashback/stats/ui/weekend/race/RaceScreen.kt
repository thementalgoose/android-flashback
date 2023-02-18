package tmg.flashback.stats.ui.weekend.race

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import tmg.flashback.formula1.enums.RaceStatus
import tmg.flashback.formula1.enums.isStatusFinished
import tmg.flashback.formula1.enums.raceStatusFinish
import tmg.flashback.formula1.extensions.pointsDisplay
import tmg.flashback.formula1.model.FastestLap
import tmg.flashback.formula1.model.LapTime
import tmg.flashback.formula1.model.RaceRaceResult
import tmg.flashback.providers.RaceRaceResultProvider
import tmg.flashback.stats.R
import tmg.flashback.stats.ui.shared.Flag
import tmg.flashback.stats.ui.weekend.WeekendInfo
import tmg.flashback.stats.ui.weekend.fakeWeekendInfo
import tmg.flashback.stats.ui.weekend.info.RaceInfoHeader
import tmg.flashback.stats.ui.weekend.shared.Delta
import tmg.flashback.stats.ui.weekend.shared.DriverInfo
import tmg.flashback.stats.ui.weekend.shared.NotAvailable
import tmg.flashback.stats.ui.weekend.shared.NotAvailableYet
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.annotations.PreviewTheme
import tmg.flashback.style.lightColours
import tmg.flashback.style.text.TextBody1
import tmg.flashback.style.text.TextBody2
import tmg.flashback.style.text.TextTitle
import tmg.flashback.ui.components.loading.SkeletonViewList
import tmg.flashback.ui.utils.pluralResource
import tmg.utilities.extensions.ordinalAbbreviation

private val timeWidth = 80.dp
private val pointsWidth = 80.dp

private val p1Height = 165.dp
private val p2Height = 120.dp
private val p3Height = 90.dp

@Composable
fun RaceScreenVM(
    info: WeekendInfo,
    actionUpClicked: () -> Unit,
    viewModel: RaceViewModel = hiltViewModel()
) {
    viewModel.inputs.load(
        season = info.season,
        round = info.round
    )

    val results = viewModel.outputs.list.observeAsState(listOf(RaceModel.Loading))

    RaceScreen(
        info = info,
        list = results.value,
        driverClicked = viewModel.inputs::clickDriver,
        actionUpClicked = actionUpClicked
    )
}

@Composable
fun RaceScreen(
    info: WeekendInfo,
    list: List<RaceModel>,
    driverClicked: (RaceRaceResult) -> Unit,
    actionUpClicked: () -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        content = {
            item("header") {
                RaceInfoHeader(
                    model = info,
                    actionUpClicked = actionUpClicked
                )
            }

            this.race(
                list = list,
                driverClicked = driverClicked
            )
        }
    )
}

internal fun LazyListScope.race(
    list: List<RaceModel>,
    driverClicked: (RaceRaceResult) -> Unit,
) {
    items(list, key = { it.id }) {
        when (it) {
            is RaceModel.Podium -> {
                Podium(
                    model = it,
                    driverClicked = driverClicked
                )
            }
            is RaceModel.Result -> {
                Result(
                    model = it.result,
                    driverClicked = driverClicked
                )
            }
            RaceModel.Loading -> {
                SkeletonViewList()
            }
            RaceModel.NotAvailable -> {
                NotAvailable()
            }
            RaceModel.NotAvailableYet -> {
                NotAvailableYet()
            }
        }
    }
    item(key = "footer") {
        Spacer(Modifier.height(72.dp))
    }
}

@Composable
private fun Podium(
    model: RaceModel.Podium,
    driverClicked: (RaceRaceResult) -> Unit,
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
                text = pluralResource(resId = R.plurals.race_points, quantity = points.toInt(), points.pointsDisplay()),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = AppTheme.dimens.xsmall)
            )
        }
    }
}

@Composable
private fun PodiumResult(
    model: RaceRaceResult,
    driverClicked: (RaceRaceResult) -> Unit,
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
                .background(model.driver.constructor.colour)
                .clickable(onClick = { driverClicked(model) })
        ) {
            AsyncImage(
                modifier = Modifier
                    .padding(4.dp)
                    .clip(RoundedCornerShape(AppTheme.dimens.radiusSmall)),
                contentScale = ContentScale.Crop,
                model = model.driver.driver.photoUrl,
                contentDescription = null,
                error = painterResource(id = R.drawable.unknown_avatar)
            )
        }
        TextTitle(
            text = model.driver.driver.firstName,
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
            text = model.driver.driver.lastName,
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
                iso = model.driver.driver.nationalityISO,
                nationality = model.driver.driver.nationality,
                modifier = Modifier
                    .size(16.dp)
                    .align(Alignment.CenterVertically),
            )
            Spacer(Modifier.width(AppTheme.dimens.xsmall))
            Delta(grid = model.grid, finish = model.finish)
            Spacer(Modifier.width(AppTheme.dimens.xxsmall))
            TextBody2(text = model.driver.constructor.name)
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
                true -> model.time?.time ?: raceStatusFinish
                false -> model.time?.time?.let { "+${it}" } ?: raceStatusFinish
            }
        )
    }
}

@Composable
private fun Result(
    model: RaceRaceResult,
    driverClicked: (RaceRaceResult) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .height(IntrinsicSize.Min)
            .alpha(
                when (model.status.isStatusFinished()) {
                    true -> 1.0f
                    false -> 0.7f
                }
            )
            .background(
                when (model.status.isStatusFinished()) {
                    true -> AppTheme.colors.backgroundPrimary
                    false -> AppTheme.colors.backgroundSecondary
                }
            ),
        horizontalArrangement = Arrangement.Center
    ) {
        DriverInfo(
            modifier = Modifier
                .weight(1f)
                .clickable(onClick = { driverClicked(model) }),
            driver = model.driver,
            position = model.finish,
            extraContent = {
                Delta(
                    grid = model.grid,
                    finish = model.finish
                )
                if (model.fastestLap?.rank == 1) {
                    Spacer(Modifier.width(4.dp))
                    FastestLap(Modifier.align(Alignment.CenterVertically))
                }
            }
        )
        Points(
            modifier = Modifier.align(Alignment.CenterVertically),
            points = model.points
        )
        Time(
            modifier = Modifier.align(Alignment.CenterVertically),
            lapTime = model.time,
            status = model.status
        )
    }
}

@Composable
private fun Time(
    lapTime: LapTime?,
    status: RaceStatus,
    modifier: Modifier = Modifier
) {
    TextBody2(
        modifier = modifier
            .width(timeWidth),
        textAlign = TextAlign.Center,
        text = when {
            lapTime?.noTime == false -> "+${lapTime.time}"
            status.isStatusFinished() -> status
            else -> stringResource(id = R.string.race_status_retired)
        },
    )
}

@Composable
private fun Points(
    points: Double,
    modifier: Modifier = Modifier
) {
    TextBody1(
        modifier = modifier.width(pointsWidth),
        bold = true,
        textAlign = TextAlign.Center,
        text = points.takeIf { it != 0.0 }?.pointsDisplay() ?: "",
    )
}

@Composable
private fun FastestLap(
    modifier: Modifier = Modifier
) {
    Icon(
        painter = painterResource(id = R.drawable.ic_fastest_lap),
        contentDescription = stringResource(id = R.string.ab_fastest_lap),
        modifier = modifier
            .padding(top = 4.dp)
            .size(14.dp),
        tint = AppTheme.colors.f1FastestSector
    )
}

@PreviewTheme
@Composable
private fun Preview(
    @PreviewParameter(RaceRaceResultProvider::class) result: RaceRaceResult
) {
    AppThemePreview {
        RaceScreen(
            info = fakeWeekendInfo,
            list = listOf(
                RaceModel.Podium(
                    p1 = result.copy(fastestLap = FastestLap(1, LapTime(0,1,2,3))),
                    p2 = result,
                    p3 = result,
                ),
                RaceModel.Result(
                    result = result.copy(fastestLap = FastestLap(1, LapTime(0,1,2,3)))
                )
            ),
            driverClicked = { },
            actionUpClicked = { }
        )
    }
}