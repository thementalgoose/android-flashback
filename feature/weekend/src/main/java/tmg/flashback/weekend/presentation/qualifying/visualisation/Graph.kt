package tmg.flashback.weekend.presentation.qualifying.visualisation

import android.graphics.Color
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import tmg.flashback.formula1.model.Constructor
import tmg.flashback.formula1.model.Driver
import tmg.flashback.formula1.model.DriverEntry
import tmg.flashback.providers.DriverConstructorProvider
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.annotations.PreviewTheme
import tmg.flashback.style.text.TextBody1
import tmg.flashback.style.text.TextBody2

private val widthOfASecond = 180.dp
private val sizeOfVisualisedCar = DpSize(75.dp, 25.dp)
private val rowSpacingBetweenCars = 0.dp

/**
 * 1:10                1:11                1:12
 * |                   |                   |
 * |   CAR(NUM1)   CAR(NUM4)     CAR(NUM5) |            v
 * |     CAR(NUM2)     |         CAR(NUM6) |            ^ rowSpacingBetweenCars
 * |        CAR(NUM3)  |                   |
 *                               <------->
 * <--widthOfASecond--->         sizeOfVisualisedCar
 */
@Composable
internal fun Graph(
    indicators: List<IndicatorEntry>,
    results: List<ResultEntry>
) {
    val width = widthOfASecond * (indicators.size)
    Box(modifier = Modifier
        .width(width)
        .height(IntrinsicSize.Min)
        .padding(horizontal = AppTheme.dimens.medium)
    ) {
        indicators.forEachIndexed { index, entry ->
            Indicator(
                indicatorEntry = entry,
                modifier = Modifier
                    .fillMaxHeight()
                    .offset(x = widthOfASecond * index)
            )
        }
        val indicatorTextPadding = with(LocalDensity.current) { 14.sp.toDp() }

        var indexMap = mutableMapOf<Int, Dp>()
        for (entry in results) {
            val carOffset = widthOfASecond * (entry.normalisedQualifyingMillis / 1000f)

            var rowToAddCarToo = indexMap
                .filter { it.value < carOffset }
                .minByOrNull { it.key }
                ?.key
            if (rowToAddCarToo == null) {
                // New row
                if (indexMap.isEmpty()) {
                    rowToAddCarToo = 0
                } else {
                    rowToAddCarToo = indexMap.keys.size
                }
            }

            CarLabel(
                entry = entry.driverEntry,
                modifier = Modifier.padding(
                    start = widthOfASecond * (entry.normalisedQualifyingMillis / 1000f),
                    top = (sizeOfVisualisedCar.height * rowToAddCarToo) + indicatorTextPadding
                )
            )
            indexMap.put(rowToAddCarToo, carOffset + sizeOfVisualisedCar.width)
        }
    }
}

@Composable
private fun Indicator(
    indicatorEntry: IndicatorEntry,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
    ) {
        TextBody1(
            text = indicatorEntry.label ?: "",
            modifier = Modifier.padding(start = 2.dp)
        )
        Box(modifier = Modifier
            .background(AppTheme.colors.contentTertiary)
            .width(1.dp)
            .fillMaxHeight()
        )
    }
}

@PreviewTheme
@Composable
private fun Preview(
    @PreviewParameter(DriverConstructorProvider::class) driverEntry: DriverEntry
) {
    AppThemePreview {
        Graph(
            indicators = fakeIndicators(),
            results = fakeResults(driverEntry.driver, driverEntry.constructor)
        )
    }
}

internal fun fakeIndicators() = listOf(
    IndicatorEntry(normalizedMillis = 0, millis = 70000, label = "1:10"),
    IndicatorEntry(normalizedMillis = 1000, millis = 71000, label = "1:11"),
    IndicatorEntry(normalizedMillis = 2000, millis = 72000, label = "1:12"),
    IndicatorEntry(normalizedMillis = 3000, millis = 73000, label = "1:13")
)

private val fakeMillisToConstructorColor = listOf(
    100 to Color.MAGENTA,
    300 to Color.YELLOW,
    500 to Color.MAGENTA,
    700 to Color.CYAN,
    701 to Color.GREEN,
    1463 to Color.RED,
    1928 to Color.YELLOW,
    2000 to Color.GREEN,
    2100 to Color.BLUE,
    2200 to Color.RED
)

internal fun fakeResults(
    driver: Driver,
    constructor: Constructor,
) = List(fakeMillisToConstructorColor.size) {
    val (millis, constructorColor) = fakeMillisToConstructorColor[it]
    ResultEntry(
        driverEntry = DriverEntry(
            driver = driver.copy(id = "$it", code = "NO$it"),
            constructor = constructor.copy(color = constructorColor)
        ),
        normalisedQualifyingMillis = millis
    )
}