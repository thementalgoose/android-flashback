package tmg.flashback.statistics.ui.overview.driver.summary

import androidx.annotation.AttrRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import org.threeten.bp.LocalDate
import tmg.flashback.formula1.model.Constructor
import tmg.flashback.formula1.model.model
import tmg.flashback.statistics.R
import tmg.flashback.statistics.ui.shared.sync.SyncDataItem

fun DriverSummaryItem.Companion.headerModel(
    driverFirstname: String = "firstName",
    driverSurname: String = "lastName",
    driverNumber: Int = 23,
    driverImg: String = "photoUrl",
    driverBirthday: LocalDate = LocalDate.of(1995, 10, 12),
    driverWikiUrl: String = "wikiUrl",
    driverNationalityISO: String = "nationalityISO"
): DriverSummaryItem.Header = DriverSummaryItem.Header(
    driverFirstname = driverFirstname,
    driverSurname = driverSurname,
    driverNumber = driverNumber,
    driverImg = driverImg,
    driverBirthday = driverBirthday,
    driverWikiUrl = driverWikiUrl,
    driverNationalityISO = driverNationalityISO
)

fun DriverSummaryItem.Companion.statModel(
    @AttrRes
    tint: Int = R.attr.contentSecondary,
    @DrawableRes
    icon: Int = 0,
    @StringRes
    label: Int = 0,
    value: String = "value"
): DriverSummaryItem.Stat = DriverSummaryItem.Stat(
    tint = tint,
    icon = icon,
    label = label,
    value = value
)

fun DriverSummaryItem.Companion.racedForModel(
    season: Int = 2020,
    constructors: List<Constructor> = listOf(
        Constructor.model()
    ),
    type: PipeType = PipeType.SINGLE,
    isChampionship: Boolean = true
): DriverSummaryItem.RacedFor = DriverSummaryItem.RacedFor(
    season = season,
    constructors = constructors,
    type = type,
    isChampionship = isChampionship
)

fun DriverSummaryItem.Companion.errorItemModel(
    item: SyncDataItem = SyncDataItem.InternalError
): DriverSummaryItem.ErrorItem = DriverSummaryItem.ErrorItem(
    item = item
)