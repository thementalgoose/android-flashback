package tmg.flashback.stats.ui.weekend.race

import androidx.annotation.DrawableRes
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
import tmg.flashback.style.text.TextBody1
import tmg.utilities.extensions.toEmptyIfZero
import kotlin.math.abs

private val timeWidth = 80.dp
private val pointsWidth = 80.dp

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

}

@Composable
private fun Result(
    model: RaceModel.Result,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.height(IntrinsicSize.Min),
        horizontalArrangement = Arrangement.Center
    ) {
        DriverInfo(
            modifier = Modifier.weight(1f),
            driver = model.result.driver,
            position = model.result.finish,
            extraContent = {
                val diff = (model.result.grid ?: 0) - (model.result.finish)
                when {
                    diff == 0 || model.result.grid == null -> { // Equal
                        Delta(
                            diff = diff,
                            color = AppTheme.colors.f1DeltaNeutral,
                            icon = R.drawable.ic_pos_neutral,
                            contentDescription = "" // stringResource(id = R.string.ab_positions_neutral, model.result.grid ?: "unknown", model.result.finish)
                        )
                    }
                    diff > 0 -> { // Gained
                        Delta(
                            diff = diff,
                            color = AppTheme.colors.f1DeltaNegative,
                            icon = R.drawable.ic_pos_up,
                            contentDescription = "" // stringResource(id = R.string.ab_positions_gained, model.result.grid ?: "unknown", model.result.finish, abs(diff))
                        )
                    }
                    else -> { // Lost
                        Delta(
                            diff = diff,
                            color = AppTheme.colors.f1DeltaPositive,
                            icon = R.drawable.ic_pos_down,
                            contentDescription = "" // stringResource(id = R.string.ab_positions_lost, model.result.grid ?: "unknown", model.result.finish, abs(diff))
                        )
                    }
                }
            }
        )
        Points(
            modifier = Modifier.align(Alignment.CenterVertically),
            points = model.result.points
        )
        Time(
            modifier = Modifier.align(Alignment.CenterVertically),
            lapTime = model.result.time,
            status = model.result.status
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
private fun Preview(
    @PreviewParameter(RaceRaceResultProvider::class) result: RaceRaceResult
) {
    AppThemePreview {
        Result(
            model = RaceModel.Result(result)
        )
    }
}