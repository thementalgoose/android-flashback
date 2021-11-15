package tmg.flashback.statistics.ui.race.viewholders.qualifying

import tmg.flashback.formula1.model.DriverConstructor
import tmg.flashback.formula1.utils.getFlagResourceAlpha3
import tmg.flashback.statistics.databinding.ViewRaceQualifyingDriverBinding
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

    layoutDriver.tvName.text = driver.driver.name
    layoutDriver.tvNumber.text = driver.driver.number?.toString() ?: "-"
    layoutDriver.tvNumber.colorHighlight = driver.constructor.color

    tvConstructor.text = driver.constructor.name
    constructorColor.setBackgroundColor(driver.constructor.color)

    layoutDriver.imgFlag.setImageResource(this.root.context.getFlagResourceAlpha3(driver.driver.nationalityISO))
}