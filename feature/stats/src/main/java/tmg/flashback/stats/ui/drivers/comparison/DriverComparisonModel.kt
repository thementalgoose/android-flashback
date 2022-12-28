package tmg.flashback.stats.ui.drivers.comparison

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.Color
import tmg.flashback.formula1.model.Constructor
import tmg.flashback.formula1.model.Driver
import tmg.flashback.formula1.model.DriverConstructor
import tmg.flashback.formula1.model.DriverConstructors

sealed class DriverComparisonModel(
    val key: String
) {
    data class Header(
        val year: Int,
        val driver1: Driver?,
        val driver2: Driver?
    ): DriverComparisonModel(
        key = "header"
    )

    data class ComparisonPoint(
        @StringRes
        val label: Int,
        val driver1: ComparisonResult,
        val driver2: ComparisonResult,
        val max: Int
    ): DriverComparisonModel(
        key = "comparison-$label"
    )

    object ConfigurationError: DriverComparisonModel(key = "error-config")
    object NetworkError: DriverComparisonModel(key = "error-network")
    object Loading: DriverComparisonModel(key = "loading")
}

data class ComparisonResult(
    val progress: Float,
    val label: String,
    val colour: Color?
)