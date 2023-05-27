package tmg.flashback.formula1.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import tmg.flashback.formula1.R
import tmg.flashback.formula1.model.DriverEntry
import tmg.flashback.formula1.model.QualifyingResult
import tmg.flashback.formula1.model.RaceResult
import tmg.flashback.formula1.model.SprintRaceResult
import tmg.utilities.extensions.ordinalAbbreviation
import kotlin.math.abs

object AccessibilityUtils {

    @Composable
    fun delta(started: Int, finished: Int) = when {
        started < finished -> stringResource(R.string.ab_positions_lost, started, finished, abs(finished - started))
        started > finished -> stringResource(R.string.ab_positions_gained, started, finished, abs(started - finished))
        else -> stringResource(R.string.ab_positions_neutral)
    }

    @Composable
    fun RaceResult.overview(): String {
        val fastestLap = if (this.fastestLap?.rank == 1) {
            ". ${stringResource(R.string.ab_result_fastest_lap)}"
        } else {
            "."
        }

        return stringResource(R.string.ab_result_race_overview,
            this.finish.ordinalAbbreviation,
            this.driver.driver.name,
            this.driver.constructor.name
        ) + fastestLap
    }

    @Composable
    fun SprintRaceResult.overview(): String {
        return stringResource(R.string.ab_result_race_overview,
            this.finish.ordinalAbbreviation,
            this.driver.driver.name,
            this.driver.constructor.name
        )
    }

    @Composable
    fun String.by(name: String): String {
        return stringResource(R.string.ab_scored, name, this)
    }

    @Composable
    fun DriverEntry.qualified(qualified: Int?) = if (qualified == null) {
        stringResource(R.string.ab_result_qualifying_overview_dnq,
            this.driver.name,
            this.constructor.name
        )
    } else {
        stringResource(R.string.ab_result_qualifying_overview,
            this.driver.name,
            this.driver.name,
            qualified.ordinalAbbreviation
        )
    }
}