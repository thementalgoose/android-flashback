package tmg.flashback.stats.ui.weekend.race

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import org.koin.androidx.compose.viewModel
import tmg.flashback.formula1.enums.RaceStatus
import tmg.flashback.formula1.enums.isStatusFinished
import tmg.flashback.formula1.enums.raceStatusFinish
import tmg.flashback.formula1.enums.raceStatusFinished
import tmg.flashback.formula1.extensions.pointsDisplay
import tmg.flashback.formula1.model.FastestLap
import tmg.flashback.formula1.model.LapTime
import tmg.flashback.formula1.model.RaceRaceResult
import tmg.flashback.formula1.utils.getFlagResourceAlpha3
import tmg.flashback.providers.RaceRaceResultProvider
import tmg.flashback.stats.R
import tmg.flashback.stats.ui.weekend.WeekendInfo
import tmg.flashback.stats.ui.weekend.fakeWeekendInfo
import tmg.flashback.stats.ui.weekend.info.RaceInfoHeader
import tmg.flashback.stats.ui.weekend.qualifying.QualifyingScreen
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
import tmg.flashback.ui.components.loading.SkeletonView
import tmg.flashback.ui.utils.isInPreview
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
    actionUpClicked: () -> Unit
) {
    val viewModel by viewModel<RaceViewModel>()
    viewModel.inputs.load(
        season = info.season,
        round = info.round
    )

    val results = viewModel.outputs.list.observeAsState(listOf(RaceModel.Loading))

    RaceScreen(
        info = info,
        list = results.value,
        actionUpClicked = actionUpClicked
    )
}

@Composable
fun RaceScreen(
    info: WeekendInfo,
    list: List<RaceModel>,
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
            items(list, key = { it.id }) {
                when (it) {
                    is RaceModel.Podium -> {
                        Podium(model = it)
                    }
                    is RaceModel.Result -> {
                        Result(model = it.result)
                    }
                    RaceModel.Loading -> {
                        SkeletonView()
                        SkeletonView()
                        SkeletonView()
                        SkeletonView()
                        SkeletonView()
                        SkeletonView()
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
    )
}

@Composable
private fun Podium(
    model: RaceModel.Podium,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier
        .height(IntrinsicSize.Min)
        .padding(
            horizontal = AppTheme.dimensions.paddingXSmall,
            vertical = AppTheme.dimensions.paddingXSmall
        )
    ) {
        Column(Modifier.weight(1f), verticalArrangement = Arrangement.Bottom) {
            PodiumResult(
                model = model.p2,
                modifier = Modifier.padding(
                    top = p1Height - p2Height
                )
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
                model = model.p1
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
            .padding(AppTheme.dimensions.paddingSmall)
            .clip(RoundedCornerShape(AppTheme.dimensions.radiusSmall))
            .background(color)
    ) {
        Column(Modifier.align(Alignment.TopCenter)) {
            Box(modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = AppTheme.dimensions.paddingSmall,
                    start = AppTheme.dimensions.paddingSmall,
                    end = AppTheme.dimensions.paddingSmall
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
                    .padding(vertical = AppTheme.dimensions.paddingXSmall)
            )
        }
    }
}

@Composable
private fun PodiumResult(
    model: RaceRaceResult,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            Modifier
                .size(80.dp)
                .clip(RoundedCornerShape(AppTheme.dimensions.radiusSmall))
                .align(Alignment.CenterHorizontally)
                .background(model.driver.constructor.colour)
        ) {
            AsyncImage(
                modifier = Modifier
                    .padding(4.dp)
                    .clip(RoundedCornerShape(AppTheme.dimensions.radiusSmall)),
                contentScale = ContentScale.Crop,
                model = model.driver.driver.photoUrl,
                contentDescription = null,
                error = painterResource(id = R.drawable.unknown_avatar)
            )
        }
        TextTitle(
            text = model.driver.driver.name,
            bold = true,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = AppTheme.dimensions.paddingSmall,
                    start = 2.dp,
                    end = 2.dp
                )
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = AppTheme.dimensions.paddingXSmall,
                    end = AppTheme.dimensions.paddingXSmall,
                    start = AppTheme.dimensions.paddingXSmall
                ),
            horizontalArrangement = Arrangement.Center
        ) {
            val resourceId = when (isInPreview()) {
                true -> R.drawable.gb
                false -> LocalContext.current.getFlagResourceAlpha3(model.driver.driver.nationalityISO)
            }
            Image(
                modifier = Modifier
                    .size(16.dp)
                    .align(Alignment.CenterVertically),
                painter = painterResource(id = resourceId),
                contentDescription = null,
                contentScale = ContentScale.Fit,
            )
            Spacer(Modifier.width(AppTheme.dimensions.paddingXSmall))
            Delta(grid = model.grid, finish = model.finish)
            Spacer(Modifier.width(AppTheme.dimensions.paddingXXSmall))
            TextBody2(text = model.driver.constructor.name)
        }
        TextBody2(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = AppTheme.dimensions.paddingXSmall,
                    end = AppTheme.dimensions.paddingXSmall,
                    start = AppTheme.dimensions.paddingXSmall
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
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.height(IntrinsicSize.Min),
        horizontalArrangement = Arrangement.Center
    ) {
        DriverInfo(
            modifier = Modifier.weight(1f),
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
            .size(12.dp),
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
            actionUpClicked = { }
        )
    }
}