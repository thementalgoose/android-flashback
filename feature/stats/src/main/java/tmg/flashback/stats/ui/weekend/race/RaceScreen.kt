package tmg.flashback.stats.ui.weekend.race

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.viewModel
import tmg.flashback.formula1.enums.RaceStatus
import tmg.flashback.formula1.enums.isStatusFinished
import tmg.flashback.formula1.extensions.pointsDisplay
import tmg.flashback.formula1.model.LapTime
import tmg.flashback.formula1.model.RaceRaceResult
import tmg.flashback.providers.RaceRaceResultProvider
import tmg.flashback.stats.ui.weekend.WeekendInfo
import tmg.flashback.stats.ui.weekend.info.RaceInfoHeader
import tmg.flashback.stats.ui.weekend.qualifying.QualifyingViewModel
import tmg.flashback.stats.ui.weekend.shared.DriverInfo
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.text.TextBody2
import tmg.flashback.ui.extensions.getColor
import tmg.flashback.stats.R
import tmg.flashback.style.annotations.PreviewTheme
import tmg.flashback.style.lightColours
import tmg.flashback.style.text.TextBody1
import tmg.flashback.ui.utils.pluralResource
import tmg.utilities.extensions.ordinalAbbreviation
import tmg.utilities.extensions.toEmptyIfZero
import kotlin.math.abs

private val timeWidth = 80.dp
private val pointsWidth = 80.dp

private val p1Height = 120.dp
private val p2Height = 100.dp
private val p3Height = 80.dp

@Composable
fun RaceScreenVM(
    info: WeekendInfo,
    actionUpClicked: () -> Unit
) {
    val viewModel by viewModel<RaceViewModel>()
    RaceScreen(
        info = info,
        actionUpClicked
    )
}

@Composable
fun RaceScreen(
    info: WeekendInfo,
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
            item("content") {
                TextBody2(text = "Race")
            }
        }
    )
}

@Composable
private fun Podium(
    model: RaceModel.Podium,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier) {
        Column(Modifier.weight(1f), verticalArrangement = Arrangement.Bottom) {
            PodiumResult(
                model = model.p2
            )
            PodiumBar(
                position = 2,
                points = model.p2.points,
                height = p2Height,
                color = AppTheme.colors.f1Podium2
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
                color = AppTheme.colors.f1Podium1
            )
        }
        Column(Modifier.weight(1f), verticalArrangement = Arrangement.Bottom) {
            PodiumResult(
                model = model.p3
            )
            PodiumBar(
                position = 3,
                points = model.p3.points,
                height = p3Height,
                color = AppTheme.colors.f1Podium3
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
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            .background(color)
    ) {
        Column(Modifier.align(Alignment.TopCenter)) {
            TextBody1(
                textAlign = TextAlign.Center,
                textColor = lightColours.contentPrimary,
                text = position.ordinalAbbreviation,
                bold = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = AppTheme.dimensions.paddingSmall)
            )
            TextBody1(
                textAlign = TextAlign.Center,
                textColor = lightColours.contentPrimary,
                text = pluralResource(resId = R.plurals.race_points, quantity = points.toInt(), points.pointsDisplay()),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = AppTheme.dimensions.paddingSmall)
            )
        }
    }
}

@Composable
private fun PodiumResult(
    model: RaceRaceResult,
    modifier: Modifier = Modifier
) {

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
                val diff = (model.grid ?: 0) - (model.finish)
                when {
                    diff == 0 || model.grid == null -> { // Equal
                        Delta(
                            diff = diff,
                            color = AppTheme.colors.f1DeltaNeutral,
                            icon = R.drawable.ic_pos_neutral,
                            contentDescription = "" // stringResource(id = R.string.ab_positions_neutral, model.grid ?: "unknown", model.finish)
                        )
                    }
                    diff > 0 -> { // Gained
                        Delta(
                            diff = diff,
                            color = AppTheme.colors.f1DeltaNegative,
                            icon = R.drawable.ic_pos_up,
                            contentDescription = "" // stringResource(id = R.string.ab_positions_gained, model.grid ?: "unknown", model.finish, abs(diff))
                        )
                    }
                    else -> { // Lost
                        Delta(
                            diff = diff,
                            color = AppTheme.colors.f1DeltaPositive,
                            icon = R.drawable.ic_pos_down,
                            contentDescription = "" // stringResource(id = R.string.ab_positions_lost, model.grid ?: "unknown", model.finish, abs(diff))
                        )
                    }
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
private fun Delta(
    diff: Int,
    color: Color,
    @DrawableRes
    icon: Int,
    contentDescription: String
) {
    Row(horizontalArrangement = Arrangement.Center) {
        Icon(
            painter = painterResource(id = icon),
            modifier = Modifier.align(Alignment.CenterVertically),
            contentDescription = contentDescription,
            tint = color,
        )
        TextBody2(
            text = abs(diff).toString(),
            textColor = color,
            modifier = Modifier.padding(horizontal = AppTheme.dimensions.paddingXSmall)
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
            lapTime?.noTime == true -> lapTime.time
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
        text = points.pointsDisplay().takeIf { it.isNotEmpty() } ?: "",
    )
}

@PreviewTheme
@Composable
private fun PreviewResult(
    @PreviewParameter(RaceRaceResultProvider::class) result: RaceRaceResult
) {
    AppThemePreview {
        Result(
            model = result
        )
    }
}

@PreviewTheme
@Composable
private fun PreviewPodium(
    @PreviewParameter(RaceRaceResultProvider::class) result: RaceRaceResult
) {
    AppThemePreview {
        Podium(
            model = RaceModel.Podium(
                p1 = result.copy(result.driver.copy(driver = result.driver.driver.copy(id = "1"))),
                p2 = result.copy(result.driver.copy(driver = result.driver.driver.copy(id = "2"))),
                p3 = result.copy(result.driver.copy(driver = result.driver.driver.copy(id = "3")))
            )
        )
    }
}