package tmg.flashback.statistics.ui.race.viewholders.qualifying

import tmg.flashback.formula1.model.DriverConstructor
import tmg.flashback.statistics.R
import tmg.flashback.statistics.databinding.ViewRaceQualifyingDriverBinding
import tmg.flashback.statistics.extensions.bindDriver
import tmg.utilities.extensions.views.gone

fun ViewRaceQualifyingDriverBinding.bind(
    driver: DriverConstructor,
    qualified: Int?
) {
    when (qualified) {
        null -> tvPosition.text = "P"
        -1 -> tvPosition.text = "P"
        0 -> tvPosition.text = "P"
        else -> tvPosition.text = qualified.toString()
    }

    penalty.gone()

    layoutDriver.bindDriver(driver)

    tvConstructor.text = driver.constructor.name
    constructorColor.setBackgroundColor(driver.constructor.color)

    // Accessibility
    val contentDescription = when {
        else -> qualiOverviewContainer.context.getString(
            R.string.ab_qualifying_overview,
            driver.driver.name,
            driver.constructor.name,
            qualified ?: "Unknown"
        )
    }
    qualiOverviewContainer.contentDescription = contentDescription
}