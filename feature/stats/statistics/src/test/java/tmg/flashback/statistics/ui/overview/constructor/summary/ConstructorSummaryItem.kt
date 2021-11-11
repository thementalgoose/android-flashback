package tmg.flashback.statistics.ui.overview.constructor.summary

import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import tmg.flashback.formula1.model.ConstructorHistorySeasonDriver
import tmg.flashback.formula1.model.model
import tmg.flashback.statistics.R
import tmg.flashback.statistics.ui.overview.driver.summary.PipeType
import tmg.flashback.statistics.ui.shared.sync.SyncDataItem

fun ConstructorSummaryItem.Companion.headerModel(
    constructorName: String = "name",
    constructorColor: Int = 0,
    constructorNationality: String = "nationality",
    constructorNationalityISO: String = "nationalityISO",
    constructorWikiUrl: String? = "wikiUrl"
): ConstructorSummaryItem.Header = ConstructorSummaryItem.Header(
    constructorName = constructorName,
    constructorColor = constructorColor,
    constructorNationality = constructorNationality,
    constructorNationalityISO = constructorNationalityISO,
    constructorWikiUrl = constructorWikiUrl
)

fun ConstructorSummaryItem.Companion.statModel(
    @AttrRes
    tint: Int = R.attr.contentSecondary,
    @DrawableRes
    icon: Int = 0,
    @StringRes
    label: Int = 0,
    value: String = "value"
): ConstructorSummaryItem.Stat = ConstructorSummaryItem.Stat(
    tint = tint,
    icon = icon,
    label = label,
    value = value
)

fun ConstructorSummaryItem.Companion.historyModel(
    pipe: PipeType = PipeType.SINGLE,
    season: Int = 2020,
    isInProgress: Boolean = true,
    championshipPosition: Int? = 1,
    points: Double = 1.0,
    @ColorInt
    colour: Int = 0,
    drivers: List<ConstructorHistorySeasonDriver> = listOf(
        ConstructorHistorySeasonDriver.model()
    )
): ConstructorSummaryItem.History = ConstructorSummaryItem.History(
    pipe = pipe,
    season = season,
    isInProgress = isInProgress,
    championshipPosition = championshipPosition,
    points = points,
    colour = colour,
    drivers = drivers
)

fun ConstructorSummaryItem.Companion.errorItemModel(
    item: SyncDataItem = SyncDataItem.InternalError
): ConstructorSummaryItem.ErrorItem = ConstructorSummaryItem.ErrorItem(
    item = item
)