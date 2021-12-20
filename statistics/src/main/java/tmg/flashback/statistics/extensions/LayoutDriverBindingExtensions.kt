package tmg.flashback.statistics.extensions

import tmg.flashback.formula1.model.DriverConstructor
import tmg.flashback.formula1.utils.getFlagResourceAlpha3
import tmg.flashback.statistics.databinding.LayoutDriverBinding
import tmg.utilities.utils.ColorUtils.Companion.darken

fun LayoutDriverBinding.bindDriver(driver: DriverConstructor) {
    this.tvName.text = driver.driver.name
    this.tvNumber.text = driver.driver.number.toString()
    this.tvNumber.colorHighlight = darken(driver.constructor.color)
    this.imgFlag.setImageResource(this.root.context.getFlagResourceAlpha3(driver.driver.nationalityISO))

    this.driverContainer.contentDescription = driver.driver.name
}