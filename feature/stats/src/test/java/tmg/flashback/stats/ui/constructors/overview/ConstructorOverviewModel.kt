package tmg.flashback.stats.ui.constructors.overview

import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import org.junit.jupiter.api.Assertions.*
import tmg.flashback.formula1.model.ConstructorHistorySeasonDriver
import tmg.flashback.formula1.model.model
import tmg.flashback.stats.ui.drivers.overview.PipeType


fun ConstructorOverviewModel.Companion.headerModel(
    constructorName: String = "name",
    constructorColor: Int = 0,
    constructorNationality: String = "nationality",
    constructorNationalityISO: String = "nationalityISO",
    constructorWikiUrl: String? = "wikiUrl"
): ConstructorOverviewModel.Header = ConstructorOverviewModel.Header(
    constructorName = constructorName,
    constructorColor = constructorColor,
    constructorNationality = constructorNationality,
    constructorNationalityISO = constructorNationalityISO,
    constructorWikiUrl = constructorWikiUrl
)

fun ConstructorOverviewModel.Companion.statModel(
    isWinning: Boolean = false,
    @DrawableRes
    icon: Int = 0,
    @StringRes
    label: Int = 0,
    value: String = "value"
): ConstructorOverviewModel.Stat = ConstructorOverviewModel.Stat(
    isWinning = isWinning,
    icon = icon,
    label = label,
    value = value
)

fun ConstructorOverviewModel.Companion.historyModel(
    pipe: PipeType = PipeType.SINGLE,
    season: Int = 2020,
    isInProgress: Boolean = true,
    championshipPosition: Int? = 1,
    points: Double = 1.0,
    drivers: List<ConstructorHistorySeasonDriver> = listOf(
        tmg.flashback.formula1.model.ConstructorHistorySeasonDriver.model()
    )
): ConstructorOverviewModel.History = ConstructorOverviewModel.History(
    pipe = pipe,
    season = season,
    isInProgress = isInProgress,
    championshipPosition = championshipPosition,
    points = points,
    drivers = drivers
)