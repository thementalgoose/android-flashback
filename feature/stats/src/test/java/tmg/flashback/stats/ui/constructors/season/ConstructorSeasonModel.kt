package tmg.flashback.stats.ui.constructors.season

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import tmg.flashback.formula1.model.ConstructorHistorySeasonDriver
import tmg.flashback.formula1.model.model

fun ConstructorSeasonModel.Companion.headerModel(
    constructorName: String = "name",
    constructorPhotoUrl: String? = "photoUrl",
    constructorColor: Int = 0,
    constructorNationality: String = "nationality",
    constructorNationalityISO: String = "nationalityISO",
    constructorWikiUrl: String? = "wikiUrl"
): ConstructorSeasonModel.Header = ConstructorSeasonModel.Header(
    constructorName = constructorName,
    constructorPhotoUrl = constructorPhotoUrl,
    constructorColor = constructorColor,
    constructorNationality = constructorNationality,
    constructorNationalityISO = constructorNationalityISO,
    constructorWikiUrl = constructorWikiUrl
)

fun ConstructorSeasonModel.Companion.statModel(
    isWinning: Boolean = false,
    @DrawableRes
    icon: Int = 0,
    @StringRes
    label: Int = 0,
    value: String = "value"
): ConstructorSeasonModel.Stat = ConstructorSeasonModel.Stat(
    isWinning = isWinning,
    icon = icon,
    label = label,
    value = value
)

fun ConstructorSeasonModel.Companion.driverModel(
    driver: ConstructorHistorySeasonDriver = ConstructorHistorySeasonDriver.model()
): ConstructorSeasonModel.Driver = ConstructorSeasonModel.Driver(
    data = driver
)