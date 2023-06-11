package tmg.flashback.drivers.ui.overview

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import org.threeten.bp.LocalDate
import tmg.flashback.drivers.R
import tmg.flashback.formula1.model.Constructor
import tmg.flashback.formula1.model.model
import tmg.flashback.ui.components.navigation.PipeType

fun DriverOverviewModel.Companion.headerModel(
    driverId: String = "driverId",
    driverName: String = "firstName lastName",
    driverNumber: Int = 23,
    driverCode: String = "code",
    driverImg: String = "photoUrl",
    driverBirthday: LocalDate = LocalDate.of(1995, 10, 12),
    driverWikiUrl: String = "wikiUrl",
    driverNationalityISO: String = "nationalityISO",
    driverNationality: String = "nationality",
    constructors: List<Constructor> = listOf()
): DriverOverviewModel.Header = DriverOverviewModel.Header(
    driverId = driverId,
    driverName = driverName,
    driverNumber = driverNumber,
    driverImg = driverImg,
    driverCode = driverCode,
    driverBirthday = driverBirthday,
    driverWikiUrl = driverWikiUrl,
    driverNationalityISO = driverNationalityISO,
    driverNationality = driverNationality,
    constructors = constructors
)

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
    label: Int = R.string.results_accurate_for_year,
    args: List<Any> = listOf(2020, "raceName", 1)
): DriverOverviewModel.Message = DriverOverviewModel.Message(
    label = label,
    args = args
)