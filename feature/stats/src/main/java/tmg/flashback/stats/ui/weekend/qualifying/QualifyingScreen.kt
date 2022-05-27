package tmg.flashback.stats.ui.weekend.qualifying

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.faltenreich.skeletonlayout.Skeleton
import org.koin.androidx.compose.viewModel
import org.threeten.bp.LocalDate
import tmg.flashback.formula1.model.DriverConstructor
import tmg.flashback.formula1.model.LapTime
import tmg.flashback.formula1.model.Race
import tmg.flashback.formula1.model.RaceQualifyingResult
import tmg.flashback.formula1.utils.getFlagResourceAlpha3
import tmg.flashback.providers.DriverConstructorProvider
import tmg.flashback.providers.RaceProvider
import tmg.flashback.stats.R
import tmg.flashback.stats.ui.weekend.WeekendInfo
import tmg.flashback.stats.ui.weekend.fakeWeekendInfo
import tmg.flashback.stats.ui.weekend.info.RaceInfoHeader
import tmg.flashback.stats.ui.weekend.shared.DriverInfo
import tmg.flashback.stats.ui.weekend.shared.NotAvailable
import tmg.flashback.stats.ui.weekend.shared.NotAvailableYet
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.annotations.PreviewTheme
import tmg.flashback.style.text.TextBody1
import tmg.flashback.style.text.TextBody2
import tmg.flashback.style.text.TextSection
import tmg.flashback.style.text.TextTitle
import tmg.flashback.ui.components.loading.SkeletonView
import tmg.flashback.ui.utils.isInPreview
import tmg.utilities.extensions.pxToDp

private val lapTimeWidth: Dp = 64.dp

@Composable
fun QualifyingScreenVM(
    info: WeekendInfo,
    actionUpClicked: () -> Unit
) {
    val viewModel by viewModel<QualifyingViewModel>()
    viewModel.inputs.load(
        season = info.season,
        round = info.round
    )

    val qualifying = viewModel.outputs.list.observeAsState(emptyList())
    val qualifyingHeader = viewModel.outputs.headersToShow.observeAsState(
        QualifyingHeader(
            first = false,
            second = false,
            third = false
        )
    )
    QualifyingScreen(
        info = info,
        actionUpClicked = actionUpClicked,
        list = qualifying.value,
        header = qualifyingHeader.value,
    )
}

@Composable
fun QualifyingScreen(
    info: WeekendInfo,
    actionUpClicked: () -> Unit,
    list: List<QualifyingModel>,
    header: QualifyingHeader
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
            if (list.any { it.isResult }) {
                item("qheader") {
                    Spacer(Modifier.height(AppTheme.dimensions.paddingMedium))
                    Header(
                        showQ1 = header.first,
                        showQ2 = header.second,
                        showQ3 = header.third,
                    )
                }
            }
            items(list, key = { it.id }) {
                when (it) {
                    is QualifyingModel.Q1 -> Qualifying(it)
                    is QualifyingModel.Q1Q2 -> Qualifying(it)
                    is QualifyingModel.Q1Q2Q3 -> Qualifying(it)
                    QualifyingModel.Loading -> {
                        SkeletonView()
                        SkeletonView()
                        SkeletonView()
                        SkeletonView()
                        SkeletonView()
                        SkeletonView()
                    }
                    QualifyingModel.NotAvailable -> {
                        NotAvailable()
                    }
                    QualifyingModel.NotAvailableYet -> {
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
private fun Header(
    modifier: Modifier = Modifier,
    showQ1: Boolean = true,
    showQ2: Boolean = true,
    showQ3: Boolean = true,
) {
    Row(modifier = modifier
        .padding(vertical = AppTheme.dimensions.paddingSmall)
    ) {
        Box(Modifier.weight(1f))

        if (showQ1) {
            TextSection(
                modifier = Modifier.width(lapTimeWidth),
                text = stringResource(id = R.string.qualifying_header_q1),
                textAlign = TextAlign.Center
            )
        }
        if (showQ2) {
            TextSection(
                modifier = Modifier.width(lapTimeWidth),
                text = stringResource(id = R.string.qualifying_header_q2),
                textAlign = TextAlign.Center
            )
        }
        if (showQ3) {
            TextSection(
                modifier = Modifier.width(lapTimeWidth),
                text = stringResource(id = R.string.qualifying_header_q3),
                textAlign = TextAlign.Center
            )
        }
        Spacer(Modifier.width(AppTheme.dimensions.paddingMedium))
    }
}

@Composable
private fun Qualifying(
    model: QualifyingModel.Q1Q2Q3,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier
        .height(IntrinsicSize.Min)
    ) {
        DriverInfo(
            modifier = Modifier.weight(1f),
            driver = model.driver,
            position = model.qualified
        )
        Time(
            modifier = Modifier.fillMaxHeight(),
            laptime = model.q1?.lapTime
        )
        Time(
            modifier = Modifier.fillMaxHeight(),
            laptime = model.q2?.lapTime
        )
        Time(
            modifier = Modifier.fillMaxHeight(),
            laptime = model.q3?.lapTime
        )
        Spacer(Modifier.width(AppTheme.dimensions.paddingMedium))
    }
}

@Composable
private fun Qualifying(
    model: QualifyingModel.Q1Q2,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier
        .height(IntrinsicSize.Min)
    ) {
        DriverInfo(
            modifier = Modifier.weight(1f),
            driver = model.driver,
            position = model.qualified
        )
        Time(
            modifier = Modifier.fillMaxHeight(),
            laptime = model.q1?.lapTime
        )
        Time(
            modifier = Modifier.fillMaxHeight(),
            laptime = model.q2?.lapTime
        )
        Spacer(Modifier.width(AppTheme.dimensions.paddingMedium))
    }
}

@Composable
private fun Qualifying(
    model: QualifyingModel.Q1,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier
        .height(IntrinsicSize.Min)
    ) {
        DriverInfo(
            modifier = Modifier.weight(1f),
            driver = model.driver,
            position = model.qualified
        )
        Time(
            modifier = Modifier.fillMaxHeight(),
            laptime = model.q1?.lapTime
        )
        Spacer(Modifier.width(AppTheme.dimensions.paddingMedium))
    }
}

@Composable
private fun Time(
    laptime: LapTime?,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier
        .width(lapTimeWidth)
    ) {
        TextBody2(
            modifier = Modifier.align(Alignment.Center),
            text = laptime?.time ?: ""
        )
    }
}

@PreviewTheme
@Composable
private fun Preview(
    @PreviewParameter(DriverConstructorProvider::class) driverConstructor: DriverConstructor
) {
    AppThemePreview {
        QualifyingScreen(
            info = fakeWeekendInfo,
            actionUpClicked = { },
            list = listOf(
                fakeQualifyingModel(driverConstructor)
            ),
            header = QualifyingHeader(true, true, true)
        )
    }
}

private fun fakeQualifyingModel(driverConstructor: DriverConstructor) = QualifyingModel.Q1Q2Q3(
    driver = driverConstructor,
    finalQualifyingPosition = 1,
    q1 = RaceQualifyingResult(driverConstructor, lapTime = LapTime(92382), position = 1),
    q2 = RaceQualifyingResult(driverConstructor, lapTime = LapTime(92293), position = 1),
    q3 = RaceQualifyingResult(driverConstructor, lapTime = LapTime(91934), position = 1)
)