package tmg.flashback.drivers.presentation.overview

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import org.threeten.bp.LocalDate
import tmg.flashback.drivers.R
import tmg.flashback.strings.R.string
import tmg.flashback.formula1.model.Constructor
import tmg.flashback.formula1.model.model
import tmg.flashback.ui.components.navigation.PipeType

fun DriverOverviewModel.Companion.statModel(
    isWinning: Boolean = false,
    @DrawableRes
    icon: Int = 0,
    @StringRes
    label: Int = 0,
    value: String = "value"
): DriverOverviewModel.Stat = DriverOverviewModel.Stat(
    isWinning = isWinning,
    icon = icon,
    label = label,
    value = value
)

fun DriverOverviewModel.Companion.racedForModel(
    season: Int = 2020,
    constructors: List<Constructor> = listOf(
        Constructor.model()
    ),
    type: PipeType = PipeType.SINGLE,
    isChampionship: Boolean = true
): DriverOverviewModel.RacedFor = DriverOverviewModel.RacedFor(
    season = season,
    constructors = constructors,
    type = type,
    isChampionship = isChampionship
)

fun DriverOverviewModel.Companion.messageModel(
    @StringRes
    label: Int = string.results_accurate_for_year,
    args: List<Any> = listOf(2020, "raceName", 1)
): DriverOverviewModel.Message = DriverOverviewModel.Message(
    label = label,
    args = args
)